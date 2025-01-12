package com.game.cdcs.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.Route;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;

@Component
public class GenerateCities {

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {

		var roma = new City("Roma");
		cityRepository.put("Roma", roma);

		var milano = new City("Milano");
		cityRepository.put("Milano", milano);

		var napoli = new City("Napoli");
		cityRepository.put("Napoli", napoli);

		roma.addDoubleRoute(new Route(roma, milano, 30));
		roma.addDoubleRoute(new Route(roma, napoli, 20));
		milano.addDoubleRoute(new Route(milano, napoli, 40));

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Generate 3 Città"));
	}
}
