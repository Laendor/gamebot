package com.game.cdcs.bot.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Component
public class TelegramHelper {

	public InlineKeyboardButton createButton(String text, String callbackData) {
		InlineKeyboardButton button = new InlineKeyboardButton();
		button.setText(text);
		button.setCallbackData(callbackData);
		return button;
	}

	public SendMessage buildSendTextMessage(Long chatId, String text) {
		SendMessage message = new SendMessage();
		message.setChatId(chatId.toString());
		message.setText(text);

		return message;
	}

	public SendPhoto buildSendPhoto(Long chatId, String text, String photoTelegramId) {
		SendPhoto sendPhoto = new SendPhoto();
		sendPhoto.setChatId(chatId);
		sendPhoto.setPhoto(new InputFile(photoTelegramId));
		sendPhoto.setCaption(text);
		return sendPhoto;
	}

	public InlineKeyboardMarkup buildOkCancelButtons(String okCallbackData, String cancelCallbackData) {
		InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> rows = new ArrayList<>();

		InlineKeyboardButton acceptButton = new InlineKeyboardButton();
		acceptButton.setText("✅ Ok");
		acceptButton.setCallbackData(okCallbackData);

		InlineKeyboardButton rejectButton = new InlineKeyboardButton();
		rejectButton.setText("❌ Cancel");
		rejectButton.setCallbackData(cancelCallbackData);

		List<InlineKeyboardButton> row = List.of(acceptButton, rejectButton);
		rows.add(row);

		markup.setKeyboard(rows);
		return markup;
	}

}
