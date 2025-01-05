package com.game.cdcs.bot.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class City {
	private final String name;
	private final List<Mission> missions = new ArrayList<>();

	public City(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<Mission> getMissions() {
		return missions;
	}

	public Optional<Mission> getMissionByName(String missionName) {
		return missions.stream().filter(mission -> mission.getName().equalsIgnoreCase(missionName)).findFirst();
	}
}
