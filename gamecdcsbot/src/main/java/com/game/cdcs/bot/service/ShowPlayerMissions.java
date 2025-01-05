package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.Mission;
import com.game.cdcs.bot.entity.MissionRecord;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPlayerMissions {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}

		PlayerProfile profile = profileOpt.get();
		var missionRecords = new ArrayList<>(profile.getMissionRecords());
		if (missionRecords.isEmpty()) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Nessuna missione trovata per il profilo."));
		}

		StringBuilder missions = new StringBuilder("Record missioni di " + profile.getName() + ":\n");

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (int indexMission = 0; indexMission < missionRecords.size(); indexMission++) {
			MissionRecord missionRecord = missionRecords.get(indexMission);
			Mission mission = missionRecord.getMission();

			missions//
					.append(indexMission + 1)//
					.append(": ")//
					.append(mission.getGoal().getDescription())//
					.append(" [" + missionRecord.getState() + "] ")//
					.append("\n");

			InlineKeyboardButton detailMissionButton = telegramHelper.createButton(
					"Mostra dettagli missione" + (indexMission + 1),
					CallbackCommand.PLAYER_MISSION_DETAILS_.name() + mission.getName());

			keyboard.add(List.of(detailMissionButton));
		}
		keyboardMarkup.setKeyboard(keyboard);

		var sendMessage = telegramHelper.buildSendTextMessage(chatId, missions.toString());
		sendMessage.setReplyMarkup(keyboardMarkup);

		return new SendResult(sendMessage);
	}
}
