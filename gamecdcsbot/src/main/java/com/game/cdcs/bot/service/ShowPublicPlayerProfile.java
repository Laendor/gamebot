package com.game.cdcs.bot.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowPublicPlayerProfile {

	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}

		PlayerProfile profile = profileOpt.get();
		String profileInfo = buildPublicPlayerProfileText(profile);
		return new SendResult(telegramHelper.buildSendTextMessage(chatId, profileInfo));
	}

	private String buildPublicPlayerProfileText(PlayerProfile profile) {
		return String.format("Profilo Giocatore:\n" //
				+ "Nome: %s\n" //
				+ "Posizione: %s\n" //
				+ "Oro: %d\n" //
				+ "Trofei: %d %s", //
				profile.getName(), profile.getCurrentCity(), profile.getGold(), profile.getTrophies(),
				profile.getTrophyCities().stream().collect(Collectors.joining(",")));
	}

}
