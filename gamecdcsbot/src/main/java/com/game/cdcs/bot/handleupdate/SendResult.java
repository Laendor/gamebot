package com.game.cdcs.bot.handleupdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

public class SendResult {

	private final List<SendMessage> sendMessages = new ArrayList<>();
	private final Optional<SendPhoto> sendPhoto;

	public SendResult(SendMessage sendMessage) {
		this(new SendMessage[] { sendMessage });
	}

	public SendResult(SendMessage[] sendMessages) {
		this.sendMessages.addAll(Arrays.asList(sendMessages));
		sendPhoto = Optional.empty();
	}

	public SendResult(SendPhoto sendPhoto) {
		this.sendPhoto = Optional.of(sendPhoto);
	}

	public List<SendMessage> getSendMessages() {
		return sendMessages;
	}

	public Optional<SendPhoto> getSendPhoto() {
		return sendPhoto;
	}

}
