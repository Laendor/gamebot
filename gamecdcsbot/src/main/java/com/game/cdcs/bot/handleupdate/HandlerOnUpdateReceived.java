package com.game.cdcs.bot.handleupdate;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;
import com.game.cdcs.bot.service.RegisterPhotoForMission;
import com.game.cdcs.bot.service.ShowPlayerMenu;

@Component
public class HandlerOnUpdateReceived {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public ShowPlayerMenu playMenu;

	@Autowired
	public HandlerCallbackQuery handlerCallbackQuery;

	@Autowired
	public RegisterPhotoForMission handlerPhoto;

	@Autowired
	public TelegramHelper telegramHelper;

	public Optional<SendResult> handleOnUpdateReceived(Update update) {

		if (isOnlyPhoto(update)) {
			return Optional.of(handlerPhoto.buildSendResult(update));
		}
		resetMissionNameAwaitingPhotoOnProfilePlayer(update);

		if (isOnlyMessage(update)) {
			return buildOnlyMessageSendResult(update);
		}

		if (isCallbackMessage(update)) {
			return Optional.of(handlerCallbackQuery.buildHandleCallbackQuerySendMessage(
					update.getCallbackQuery().getData(), update.getCallbackQuery().getMessage().getChatId()));
		}

		return Optional.empty();
	}

	private boolean isOnlyMessage(Update update) {
		return update.hasMessage() //
				&& update.getMessage().hasText();
	}

	private Optional<SendResult> buildOnlyMessageSendResult(Update update) {
		String messageText = update.getMessage().getText();
		Long chatId = update.getMessage().getChatId();

		if (messageText.equals("/start")) {
			ensurePlayerProfile(chatId);
			return Optional.of(new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Benvenuto nel gioco! Usa /play per iniziare.")));
		}

		if (messageText.equals("/play")) {
			ensurePlayerProfile(chatId);
			return Optional.of(playMenu.buildSendResult(chatId));

		}

		return Optional.empty();
	}

	private boolean isOnlyPhoto(Update update) {
		return update.getMessage() != null //
				&& !update.getMessage().hasText() //
				&& update.getMessage().getPhoto() != null //
				&& !update.getMessage().getPhoto().isEmpty() //
				&& Strings.isNotBlank(update.getMessage().getPhoto().get(0).getFileId());
	}

	private boolean isCallbackMessage(Update update) {
		return update.hasCallbackQuery();
	}

	private boolean isUpdateSendFromProfilePlayer(Long chatId) {
		return playerProfileRepository.get(chatId).isPresent();
	}

	private void ensurePlayerProfile(Long chatId) {
		playerProfileRepository.save(chatId);
	}

	private void resetMissionNameAwaitingPhotoOnProfilePlayer(Update update) {
		if (update.hasMessage()) {
			Long chatId = update.getMessage().getChatId();
			if (isUpdateSendFromProfilePlayer(chatId)) {
				playerProfileRepository.resetMissionNameAwaitingPhoto(chatId);
			}
		} else if (update.hasCallbackQuery()) {
			Long chatId = update.getCallbackQuery().getMessage().getChatId();
			if (isUpdateSendFromProfilePlayer(chatId)) {
				playerProfileRepository.resetMissionNameAwaitingPhoto(chatId);
			}
		}
	}

}
