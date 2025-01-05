package com.game.cdcs.bot.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.MapGraph;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class TravelToCity {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public MapGraph mapGraph;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId, String newCity) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}

		PlayerProfile profile = profileOpt.get();
		String currentCity = profile.getCurrentCity();
		Map<String, Integer> neighbors = mapGraph.getNeighbors(currentCity);
		if (!neighbors.containsKey(newCity)) {
			return new SendResult(
					telegramHelper.buildSendTextMessage(chatId, "Non puoi viaggiare direttamente a " + newCity + "."));
		}

		int cost = neighbors.get(newCity);
		if (profile.getGold() < cost) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId,
					"Non hai abbastanza oro per viaggiare a " + newCity + "."));
		}

		profile.addGold(-cost);
		profile.setCurrentCity(newCity);
		return new SendResult(telegramHelper.buildSendTextMessage(chatId,
				"Ti sei spostato a " + newCity + "! Hai speso " + cost + " oro."));

	}
}
