package com.game.cdcs.bot.service;

import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.entity.Route;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class TravelToCity {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public RouteRepository routeRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId, String routeId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}
		PlayerProfile profile = profileOpt.get();

		Optional<Route> routeOpt = routeRepository.get(routeId);
		if (routeOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Percorso di viaggio non trovato."));
		}
		var route = routeOpt.get();
		var destination = route.getTo();

		City currentCity = profile.getCurrentCity();
		if (!profile.getCurrentCity().equals(route.getFrom())) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Non puoi viaggiare direttamente a "
					+ destination.getName() + " perchè non sei nella giusta posizione."));
		}

		int totalCost = route.getCost();
		var discountPercent = profile.isDiscountActive() ? profile.getTravelDiscount() : 0;
		var discount = totalCost * (discountPercent / 100);
		int cost = totalCost - discount;
		if (profile.getGold() < cost) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId,
					"Non hai abbastanza oro per viaggiare a " + destination.getName() + "."));
		}

		profile.addGold(-cost);
		profile.setCurrentCity(destination);

		var resultMessage = new StringBuilder()//
				.append("Ti sei spostato a " + destination.getName() + "!")//
				.append(" Hai speso " + cost + " €")//
				.append(discount > 0 ? " (sconto applicato di " + discount + " €)" : Strings.EMPTY)//
				.append(".");

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, resultMessage.toString()));

	}
}
