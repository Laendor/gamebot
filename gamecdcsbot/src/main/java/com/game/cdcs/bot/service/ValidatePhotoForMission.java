package com.game.cdcs.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.game.cdcs.bot.entity.MissionPhotoState;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.MissionPhotoRepository;

@Component
public class ValidatePhotoForMission {

	@Autowired
	public MissionPhotoRepository missionPhotoRepository;

	@Autowired
	public CompleteMission completeMission;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId, Long missionPhotoId, boolean isPhotoApproved) {
		var missionPhotoOpt = missionPhotoRepository.get(missionPhotoId);
		if (missionPhotoOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Foto di Missione non trovata"));
		}
		var missionPhoto = missionPhotoOpt.get();

		missionPhoto.setState(isPhotoApproved ? MissionPhotoState.APPROVED : MissionPhotoState.NOT_APPROVED);
		if (isPhotoApproved) {
			return completeMission.buildSendResult(missionPhoto.getMissionRecord().getPlayerProfile(),
					missionPhoto.getMissionRecord());
		}
		var mission = missionPhoto.getMissionRecord().getMission();
		var profile = missionPhoto.getMissionRecord().getPlayerProfile();
		var messageToProfile = telegramHelper.buildSendTextMessage(profile.getChatId(),
				"La tua foto per la missione " + mission.getName() + " e' stata rifiutata");
		var messageToAdmin = telegramHelper.buildSendTextMessage(chatId,
				"Foto rifiutata. Si prega di informare dei motivi al giocatore "
						+ missionPhoto.getMissionRecord().getPlayerProfile().getName());

		var sendMessages = new SendMessage[] { messageToProfile, messageToAdmin };

		return new SendResult(sendMessages);
	}
}
