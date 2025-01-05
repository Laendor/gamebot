package com.game.cdcs.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.service.GenerateCities;
import com.game.cdcs.bot.service.GenerateCityMission;
import com.game.cdcs.bot.service.GenerateGoals;

@Component
public class GameBotInitializer {

	@Autowired
	public GenerateCityMission generateCityMission;

	@Autowired
	public GenerateCities generateCities;

	@Autowired
	public GenerateGoals generateGoals;

	public void run() {
		generateGoals.buildSendResult(0L);

		generateCities.buildSendResult(0L);

		generateCityMission.buildSendResult(0L);
	}
}
