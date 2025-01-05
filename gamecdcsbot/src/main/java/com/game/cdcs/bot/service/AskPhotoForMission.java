package com.game.cdcs.bot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.MissionRecordRepository;
import com.game.cdcs.bot.repository.MissionRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class AskPhotoForMission {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public MissionRepository missionRepository;

	@Autowired
	public MissionRecordRepository missionRecordRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId, String missionName) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		var profile = profileOpt.get();

		var missionOpt = missionRepository.get(missionName);
		if (missionOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Missione non trovata."));
		}
		var mission = missionOpt.get();

		profile.setMissionNameAwatingPhoto(missionName);

		return new SendResult(telegramHelper.buildSendTextMessage(chatId,
				"La tua missione è la seguente: " + mission.getGoal().getDescription()
						+ ". Per favore invia una foto che soddisfi i requisiti della missione."));
	}
}
