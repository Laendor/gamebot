package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Mission;

@Component
public class MissionRepository {

	private final Map<String, Mission> missions = new HashMap<>();

	public Optional<Mission> get(String name) {
		return Optional.ofNullable(missions.get(name));
	}

	public void put(String string, Mission mission) {
		missions.put(string, mission);
	}
}
