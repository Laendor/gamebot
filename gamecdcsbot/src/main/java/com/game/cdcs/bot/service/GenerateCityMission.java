package com.game.cdcs.bot.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.CityMission;
import com.game.cdcs.bot.entity.Goal;
import com.game.cdcs.bot.entity.ItemEffect;
import com.game.cdcs.bot.entity.Reward;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.GoalRepository;
import com.game.cdcs.bot.repository.ItemEffectRepository;
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
	public ItemEffectRepository itemEffectsRepository;

	public SendResult buildSendResult(Long chatId) {
		var cities = cityRepository.getAll();
		var missions = cities.stream().map(this::clearAndGenerateNewMissionsOnCity).flatMap(List::stream)
				.collect(Collectors.toList());

		return new SendResult(
				telegramHelper.buildSendTextMessage(chatId, "Generate " + missions.size() + " nuove missioni!"));
	}

	private List<CityMission> clearAndGenerateNewMissionsOnCity(City city) {
		clearMissionsOnCity(city);
		return generateNewMissionsOnCity(city);
	}

	private void clearMissionsOnCity(City city) {
		city.getMissions().clear();
	}

	private List<CityMission> generateNewMissionsOnCity(City city) {
		final int numberOfCityMissions = 4;
		final List<Goal> randomGoals = goalRepository.getRandom(numberOfCityMissions);

		return IntStream.range(0, numberOfCityMissions)
				.mapToObj(missionGeneratedCounter -> generateOneNewMissionOnCity(city,
						randomGoals.get(missionGeneratedCounter),
						getSpecialEffectForMission(missionGeneratedCounter == numberOfCityMissions - 1, city)))
				.filter(missionOpt -> missionOpt.isPresent()).map(missionOpt -> missionOpt.get())
				.collect(Collectors.toList());
	}

	private Optional<CityMission> generateOneNewMissionOnCity(City city, Goal randomGoal,
			Optional<ItemEffect> specialEffectReward) {
		var reward = new Reward(getRandomPrize(), specialEffectReward);
		var newMission = new CityMission(getRandomMissionName(), randomGoal, city, reward);

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

	private Optional<ItemEffect> getSpecialEffectForMission(boolean isTrophy, City city) {
		return isTrophy ? itemEffectsRepository.getTrophyOfCity(city)
				: (new Random().nextInt(10) < 7) ? Optional.empty() : itemEffectsRepository.getRandom();
	}

}
