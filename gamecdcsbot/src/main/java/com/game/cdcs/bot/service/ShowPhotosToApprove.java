package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.MissionPhoto;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.MissionPhotoRepository;

@Component
public class ShowPhotosToApprove {

	@Autowired
	public MissionPhotoRepository missionPhotoRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {
		var missionPhotos = missionPhotoRepository.getPhotosAwaitingToBeApprovedOrderedByUploadedAtDesc();
		if (missionPhotos.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Nessuna foto da approvare."));
		}

		SendMessage message = telegramHelper.buildSendTextMessage(chatId, "Foto da approvare:");
		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (MissionPhoto photo : missionPhotos) {

			InlineKeyboardButton button = telegramHelper.createButton(
					photo.getMissionRecord().getMission().getName() + " di "
							+ photo.getMissionRecord().getPlayerProfile().getName(),
					CallbackCommand.CHECK_PHOTO_.name() + photo.getId());
			keyboard.add(List.of(button));
		}

		keyboardMarkup.setKeyboard(keyboard);
		message.setReplyMarkup(keyboardMarkup);

		return new SendResult(message);
	}
}
