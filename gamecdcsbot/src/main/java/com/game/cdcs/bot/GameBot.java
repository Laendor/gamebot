package com.game.cdcs.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.game.cdcs.bot.handleupdate.HandlerOnUpdateReceived;
import com.game.cdcs.bot.handleupdate.SendResult;

import jakarta.annotation.PostConstruct;

@SuppressWarnings("deprecation")
@Component
public class GameBot extends TelegramLongPollingBot {

	private static final String BOT_TOKEN = "7563233028:AAF9-hoAPPcJmjGQc5e9V32AvlBNPIDIjFA";

	private static final String BOT_USERNAME = "gamecdcs_bot";

	@Autowired
	public GameBotInitializer initializer;

	@Autowired
	public HandlerOnUpdateReceived handlerOnUpdateReceiver;

	@PostConstruct
	public void init() {
		initializer.run();
	}

	@Override
	public void onUpdateReceived(Update update) {
		var sendResultOpt = handlerOnUpdateReceiver.handleOnUpdateReceived(update);
		if (sendResultOpt.isEmpty()) {
			return;
		}
		sendResult(sendResultOpt.get());
	}

	private void sendResult(SendResult sendResult) {
		sendResult.getSendMessages().forEach(this::sendTextMessage);
		var sendPhotoOpt = sendResult.getSendPhoto();
		if (sendPhotoOpt.isEmpty()) {
			return;
		}
		var sendPhoto = sendPhotoOpt.get();
		sendPhotoMessage(sendPhoto);
	}

	private void sendTextMessage(SendMessage sendMessage) {
		try {
			execute(sendMessage);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	private void sendPhotoMessage(SendPhoto sendPhoto) {
		try {
			execute(sendPhoto);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return BOT_TOKEN;
	}
}
