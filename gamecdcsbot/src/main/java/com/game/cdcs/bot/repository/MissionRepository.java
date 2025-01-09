package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.CityMission;

@Component
public class MissionRepository {

	private final Map<String, CityMission> missions = new HashMap<>();

	public Optional<CityMission> get(String name) {
		return Optional.ofNullable(missions.get(name));
	}

	public void put(String string, CityMission mission) {
		missions.put(string, mission);
	}
}
