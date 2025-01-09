package com.game.cdcs.bot.service;

import java.util.Collections;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;

@Component
public class GenerateAllEntities {

	@Autowired
	public GenerateCityMission generateCityMission;

	@Autowired
	public GenerateCities generateCities;

	@Autowired
	public GenerateGoals generateGoals;

	@Autowired
	public GenerateItemEffects generateItemEffects;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {

		var result = Stream.concat(Collections.emptyList().stream(),
				generateGoals.buildSendResult(chatId).getSendMessages().stream()).toList();

		result = Stream.concat(result.stream(), generateCities.buildSendResult(chatId).getSendMessages().stream())
				.toList();

		result = Stream.concat(result.stream(), generateItemEffects.buildSendResult(chatId).getSendMessages().stream())
				.toList();

		result = Stream.concat(result.stream(), generateCityMission.buildSendResult(chatId).getSendMessages().stream())
				.toList();

		return new SendResult(result.toArray(new SendMessage[result.size()]));
	}

}
