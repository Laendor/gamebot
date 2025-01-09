package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.CityMissionRecordState;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPlayerMissionDetails {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	public SendResult buildSendResult(Long chatId, String missionName) {
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

		var missionRecordOpt = missionRecords.stream().filter(mr -> mr.getMission().getName().equals(missionName))
				.findFirst();
		if (missionRecordOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId,
					"Nessuna missione trovata con il nome " + missionName + " per il profilo."));
		}
		var missionRecord = missionRecordOpt.get();
		var mission = missionRecord.getMission();
		var missionReward = mission.getReward();

		StringBuilder missionDetails = new StringBuilder();
		missionDetails.append("Missione: " + mission.getName() + "\n");
		missionDetails.append("Obiettivo: " + mission.getGoal().getDescription() + "\n");
		missionDetails.append("Ottenuta a: " + mission.getOriginCity().getName());
		missionDetails.append(" (" + missionRecord.getStartDate() + ")\n");

		if (missionRecord.getCompletionDate() != null) {
			missionDetails.append("Completata il: " + missionRecord.getCompletionDate() + "\n");
		}
		missionDetails.append("Stato: " + missionRecord.getState().toString() + "\n");

		if (missionReward.getGoldReward() > 0) {
			missionDetails.append("Ricompensa oro: " + missionReward.getGoldReward() + "€ \n");
		}

		var rewardOpt = missionReward.getSpecialEffectReward();
		if (rewardOpt.isPresent()) {
			missionDetails.append("Ricompensa oggetto: " + rewardOpt.get().getName() + "\n");
		}

		var sendPhoto = telegramHelper.buildSendPhoto(chatId, missionDetails.toString(),
				missionRecord.getMissionPhoto().getTelegramId());

		if (missionRecord.getState() == CityMissionRecordState.STARTED) {
			updateSendMessageWithResendPhotoButton(sendPhoto, mission.getName());
		}

		return new SendResult(sendPhoto);
	}

	private void updateSendMessageWithResendPhotoButton(SendPhoto sendPhoto, String missionName) {
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		InlineKeyboardButton resendPhotoButton = telegramHelper.createButton("Invia una nuovo foto",
				CallbackCommand.ASK_PHOTO_FOR_MISSION_.name() + missionName);

		keyboard.add(List.of(resendPhotoButton));

		keyboardMarkup.setKeyboard(keyboard);
		sendPhoto.setReplyMarkup(keyboardMarkup);
	}
}
