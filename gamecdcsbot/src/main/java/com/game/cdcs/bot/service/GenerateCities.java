package com.game.cdcs.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.MapGraph;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;

@Component
public class GenerateCities {

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public MapGraph mapGraph;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {
		cityRepository.put("Roma", new City("Roma"));
		cityRepository.put("Milano", new City("Milano"));
		cityRepository.put("Napoli", new City("Napoli"));

		mapGraph.addCity("Roma");
		mapGraph.addCity("Milano");
		mapGraph.addCity("Napoli");

		mapGraph.addPath("Roma", "Milano", 30);
		mapGraph.addPath("Roma", "Napoli", 20);
		mapGraph.addPath("Milano", "Napoli", 40);

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Generate 3 Città"));
	}
}
