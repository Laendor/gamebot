package com.game.cdcs.bot.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.game.cdcs.bot.entity.CityMission;
import com.game.cdcs.bot.entity.CityMissionRecord;
import com.game.cdcs.bot.entity.CityMissionRecordState;
import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.entity.Reward;
import com.game.cdcs.bot.entity.Trophy;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.ItemRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class CompleteMission {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public ItemRepository itemRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(PlayerProfile profile, CityMissionRecord missionRecord) {
		var profilePlayerId = profile.getChatId();
		var mission = missionRecord.getMission();

		if (missionRecord.getState() == CityMissionRecordState.COMPLETED) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(profilePlayerId, "Questa missione è stata già completata."));
		}

		var reward = mission.getReward();

		List<SendMessage> sendMessages = new ArrayList<>();

		updateMissionStates(missionRecord);

		earnRewardMoney(profile, profilePlayerId, mission, reward, sendMessages);

		earnRewardTrophyItem(profile, profilePlayerId, mission, reward, sendMessages);

		earnRewardNoTrophyItem(profile, profilePlayerId, reward, sendMessages);

		var result = sendMessages.toArray(new SendMessage[sendMessages.size()]);
		return new SendResult(result);

	}

	private void updateMissionStates(CityMissionRecord missionRecord) {
		missionRecord.setState(CityMissionRecordState.COMPLETED);
		missionRecord.setCompletionDate(LocalDate.now());

	}

	private void earnRewardMoney(PlayerProfile profile, Long profilePlayerId, CityMission mission, Reward reward,
			List<SendMessage> sendMessages) {
		int goldReward = reward.getGoldReward();
		if (profile.getGoldMultiplier() > 1.0) {
			goldReward *= profile.getGoldMultiplier();
		}
		profile.addGold(goldReward);

		sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId, "Missione " + mission.getName()
				+ " completata! Hai guadagnato " + reward.getGoldReward() + "€ "
				+ (profile.getGoldMultiplier() > 1.0 ? " (+" + (goldReward - reward.getGoldReward()) + "€ bonus)" : "")
				+ "."));
	}

	private void earnRewardTrophyItem(PlayerProfile profile, Long profilePlayerId, CityMission mission, Reward reward,
			List<SendMessage> sendMessages) {
		if (reward.isTrophy()) {
			var rewardSpecialEffect = (Trophy) reward.getSpecialEffectReward().get();
			var newItem = new Item(itemRepository.nextId(), rewardSpecialEffect);
			itemRepository.put(newItem);
			profile.getItems().add(newItem);
			sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId,
					"Hai ottenuto il trofeo di " + rewardSpecialEffect.getCityName() + "!"));
		}
	}

	private void earnRewardNoTrophyItem(PlayerProfile profile, Long profilePlayerId, Reward reward,
			List<SendMessage> sendMessages) {
		if (reward.isNoTrophy()) {
			var specialEffectReward = reward.getSpecialEffectReward().get();
			var newItem = new Item(itemRepository.nextId(), specialEffectReward);
			itemRepository.put(newItem);
			profile.getItems().add(newItem);
			sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId,
					"Hai ottenuto un nuovo oggetto: " + specialEffectReward.getName()));
		}
	}

}
