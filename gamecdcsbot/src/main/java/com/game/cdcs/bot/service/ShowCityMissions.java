package com.game.cdcs.bot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.CityMission;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.CallbackCommand;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class ShowCityMissions {

	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public CityRepository cityRepository;

	public SendResult buildSendResult(Long chatId) {
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(chatId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Profilo non trovato."));
		}

		PlayerProfile profile = profileOpt.get();
		City city = profile.getCurrentCity();
		StringBuilder missions = new StringBuilder("Missioni disponibili a " + city.getName() + ":\n");

		InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
		List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

		for (int indexMission = 0; indexMission < city.getMissions().size(); indexMission++) {
			CityMission mission = city.getMissions().get(indexMission);

			var reward = mission.getReward();

			missions//
					.append(indexMission + 1)//
					.append(": ")//
					.append(mission.getGoal().getDescription())//
					.append(" -> ")//
					.append(" [" + reward.getGoldReward() + " €]")//
					.append(reward.isTrophy() ? " <T> " : "") //
					.append(reward.isNoTrophy() ? " {*} " : "") //
					.append("\n");

			InlineKeyboardButton button = telegramHelper.createButton("Missione " + (indexMission + 1),
					CallbackCommand.ASK_PHOTO_FOR_MISSION_.name() + mission.getName());
			keyboard.add(List.of(button));
		}

		SendMessage message = telegramHelper.buildSendTextMessage(chatId, missions.toString());

		keyboardMarkup.setKeyboard(keyboard);
		message.setReplyMarkup(keyboardMarkup);

		return new SendResult(message);
	}

}
