package com.game.cdcs.bot.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.game.cdcs.bot.entity.MissionRecord;
import com.game.cdcs.bot.entity.MissionRecordState;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class CompleteMission {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(PlayerProfile profile, MissionRecord missionRecord) {
		var profilePlayerId = profile.getChatId();
		var mission = missionRecord.getMission();

		if (missionRecord.getState() == MissionRecordState.COMPLETED) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(profilePlayerId, "Questa missione è stata già completata."));
		}

		List<SendMessage> sendMessages = new ArrayList<>();

		missionRecord.setState(MissionRecordState.COMPLETED);
		missionRecord.setCompletionDate(LocalDate.now());

		int goldReward = mission.getGoldReward();
		if (profile.getGoldMultiplier() > 1.0) {
			goldReward *= profile.getGoldMultiplier();
		}
		profile.addGold(goldReward);

		sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId, "Missione " + mission.getName()
				+ " completata! Hai guadagnato " + mission.getGoldReward() + "€ "
				+ (profile.getGoldMultiplier() > 1.0 ? " (+" + (goldReward - mission.getGoldReward()) + "€ bonus)" : "")
				+ "."));

		if (mission.isGrantsTrophy()) {
			profile.addTrophy(mission.getOriginCity().getName());
			sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId,
					"Hai ottenuto il trofeo di " + mission.getOriginCity().getName() + "!"));
		}

		if (mission.getSpecialEffectReward().isPresent()) {
			var specialEffectReward = mission.getSpecialEffectReward().get();
			specialEffectReward.applyEffect(profile);
			sendMessages.add(telegramHelper.buildSendTextMessage(profilePlayerId,
					"Effetto speciale sbloccato: " + specialEffectReward.getDescription()));
		}

		var resultSendMessages = new SendMessage[sendMessages.size()];
		sendMessages.toArray(resultSendMessages);
		return new SendResult(resultSendMessages);

	}
}
