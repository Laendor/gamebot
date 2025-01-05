package com.game.cdcs.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.MissionPhotoRepository;

@Component
public class ShowPhotoToApprove {

	@Autowired
	public MissionPhotoRepository missionPhotoRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId, Long missionPhotoId) {
		var missionPhotoOpt = missionPhotoRepository.get(missionPhotoId);
		if (missionPhotoOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Foto di Missione non trovata"));
		}
		var missionPhoto = missionPhotoOpt.get();

		var missionRecord = missionPhoto.getMissionRecord();
		var mission = missionRecord.getMission();
		var profile = missionRecord.getPlayerProfile();

		var sendPhoto = telegramHelper.buildSendPhoto(chatId,
				"Goal: " + mission.getGoal().getDescription() + "\n" + "Giocatore: " + profile.getName(),
				missionPhoto.getTelegramId());

		sendPhoto.setReplyMarkup(
				telegramHelper.buildOkCancelButtons(CallbackCommand.ACCEPT_PHOTO_.name() + missionPhoto.getId(),
						CallbackCommand.REJECT_PHOTO_.name() + missionPhoto.getId()));

		return new SendResult(sendPhoto);
	}
}
