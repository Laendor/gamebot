package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;

@Component
public class ShowPlayerMenu {

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {
		SendMessage message = new SendMessage();
		message.setChatId(chatId.toString());
		message.setText("Menu di gioco: scegli un'opzione");

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		// player
		keyboard.add(List.of(telegramHelper.createButton("Vedi profilo", CallbackCommand.PROFILE.name())));
		keyboard.add(List
				.of(telegramHelper.createButton("Vedi le missioni cittadine", CallbackCommand.CITY_MISSIONS.name())));
		keyboard.add(
				List.of(telegramHelper.createButton("Vedi le tue missioni", CallbackCommand.PLAYER_MISSIONS.name())));
		keyboard.add(List.of(telegramHelper.createButton("Cambia città", CallbackCommand.CHANGE_CITY.name())));
		keyboard.add(List.of(telegramHelper.createButton("Vedi mappa", CallbackCommand.MAP.name())));
		keyboard.add(List.of(telegramHelper.createButton("Vedi inventario", CallbackCommand.INVENTORY.name())));

		// admin
		keyboard.add(List.of(telegramHelper.createButton("Genera nuove missioni cittadine [ADMIN]",
				CallbackCommand.GENERATE_CITY_MISSIONS.name())));
		keyboard.add(List.of(telegramHelper.createButton("Vedi foto da approvare [ADMIN]",
				CallbackCommand.PHOTOS_TO_APPROVE.name())));

		keyboardMarkup.setKeyboard(keyboard);
		message.setReplyMarkup(keyboardMarkup);

		return new SendResult(message);
	}

}
