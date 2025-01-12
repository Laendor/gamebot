package com.game.cdcs.bot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.ItemEffectUsableOnAnyOtherPlayer;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class UsePlayerItemOnPlayerTarget {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId, String playerItemIdString, String playerTargetIdString) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();

		Long playerItemId;
		try {
			playerItemId = Long.parseLong(playerItemIdString);
		} catch (Exception e) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Identificativo oggetto non valido."));
		}

		Optional<Item> itemOpt = profile.getItem(playerItemId);
		if (itemOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Oggetto non trovato."));
		}
		Item item = itemOpt.get();

		Long playerTargetId;
		try {
			playerTargetId = Long.parseLong(playerTargetIdString);
		} catch (Exception e) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Identificativo profilo bersaglio non valido."));
		}

		Optional<PlayerProfile> playerTargetOpt = playerProfileRepository.get(playerTargetId);
		if (playerTargetOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "profilo bersaglio non trovato."));
		}
		PlayerProfile playerTarget = playerTargetOpt.get();

		var itemEffect = (ItemEffectUsableOnAnyOtherPlayer) item.getItemEffect();
		itemEffect.use(playerTarget);
		profile.removeItem(item);

		if (playerItemId == chatId) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Hai usato " + item.getNameEffect() + " su te stesso"));
		}

		var sendMessageForUser = telegramHelper.buildSendTextMessage(chatId,
				"Hai usato " + item.getNameEffect() + " su " + playerTarget.getName());

		var sendMessageForTarget = telegramHelper.buildSendTextMessage(playerTargetId,
				profile.getName() + " ha usato " + item.getNameEffect() + " su di te!");

		return new SendResult(new SendMessage[] { sendMessageForTarget, sendMessageForUser });
	}
}
