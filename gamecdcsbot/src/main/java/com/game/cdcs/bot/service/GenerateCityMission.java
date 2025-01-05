package com.game.cdcs.bot.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.Goal;
import com.game.cdcs.bot.entity.Mission;
import com.game.cdcs.bot.entity.SpecialEffect;
import com.game.cdcs.bot.entity.factory.SpecialEffectFactory;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.GoalRepository;
import com.game.cdcs.bot.repository.MissionRepository;

@Component
public class GenerateCityMission {

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public GoalRepository goalRepository;

	@Autowired
	public MissionRepository missionRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	@Autowired
	public SpecialEffectFactory specialEffectFactory;

	public SendResult buildSendResult(Long chatId) {
		var cities = cityRepository.getAll();
		var missions = cities.stream().map(this::clearAndGenerateNewMissionsOnCity).flatMap(List::stream)
				.collect(Collectors.toList());

		return new SendResult(
				telegramHelper.buildSendTextMessage(chatId, "Generate " + missions.size() + " nuove missioni!"));
	}

	private List<Mission> clearAndGenerateNewMissionsOnCity(City city) {
		clearMissionsOnCity(city);
		return generateNewMissionsOnCity(city);
	}

	private void clearMissionsOnCity(City city) {
		city.getMissions().clear();
	}

	private List<Mission> generateNewMissionsOnCity(City city) {
		final int numberOfCityMissions = 4;
		final List<Goal> randomGoals = goalRepository.getRandom(numberOfCityMissions);

		return IntStream.range(0, numberOfCityMissions)
				.mapToObj(missionGeneratedCounter -> generateOneNewMissionOnCity(city,
						randomGoals.get(missionGeneratedCounter), missionGeneratedCounter == numberOfCityMissions - 1,
						getRandomSpecialEffect()))
				.filter(missionOpt -> missionOpt.isPresent()).map(missionOpt -> missionOpt.get())
				.collect(Collectors.toList());
	}

	private Optional<Mission> generateOneNewMissionOnCity(City city, Goal randomGoal, boolean isTrophyReward,
			Optional<SpecialEffect> specialEffectReward) {
		var newMission = new Mission(getRandomMissionName(), randomGoal, city, getRandomPrize(), isTrophyReward,
				specialEffectReward);
		city.getMissions().add(newMission);
		missionRepository.put(newMission.getName(), newMission);

		return Optional.of(newMission);
	}

	private String getRandomMissionName() {
		var random = new Random();
		StringBuilder result = new StringBuilder(6);

		while (result.length() < 6) {
			char nextChar = (char) ('A' + random.nextInt(26));
			result.append(nextChar);
		}

		return result.toString();
	}

	private int getRandomPrize() {
		return new Random().nextInt(50) + 10;
	}

	private Optional<SpecialEffect> getRandomSpecialEffect() {
		var random = new Random();
		if (random.nextInt(10) < 7) {
			return Optional.empty();
		}
		var typeSpecialEffect = random.nextInt(3);

		switch (typeSpecialEffect) {
		case 0: {
			Map<String, Object> radarParams = new HashMap<>();
			radarParams.put("durationDays", 2);
			return Optional.of(specialEffectFactory.createSpecialEffect("radar", radarParams));
		}
		case 1: {
			Map<String, Object> travelDiscountParams = new HashMap<>();
			travelDiscountParams.put("discountPercentage", 20);
			travelDiscountParams.put("durationDays", 3);
			return Optional.of(specialEffectFactory.createSpecialEffect("traveldiscount", travelDiscountParams));
		}
		case 2: {
			Map<String, Object> multiplierParams = new HashMap<>();
			multiplierParams.put("multiplier", 1.5);
			multiplierParams.put("durationDays", 5);
			return Optional.of(specialEffectFactory.createSpecialEffect("goldmultiplier", multiplierParams));
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + typeSpecialEffect);
		}
	}

}
