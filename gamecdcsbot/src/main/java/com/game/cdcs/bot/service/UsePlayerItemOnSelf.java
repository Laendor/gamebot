package com.game.cdcs.bot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Item;
import com.game.cdcs.bot.entity.ItemEffectUsableOnSelfPlayer;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class UsePlayerItemOnSelf {
	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId, String playerItemIdString) {
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
		var item = itemOpt.get();
		var itemEffect = (ItemEffectUsableOnSelfPlayer) item.getItemEffect();

		itemEffect.use(profile);
		profile.removeItem(item);

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Hai usato " + item.getNameEffect()));

	}
}
