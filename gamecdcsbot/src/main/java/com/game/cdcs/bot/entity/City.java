package com.game.cdcs.bot.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class City {
	private final String name;
	private final List<CityMission> missions = new ArrayList<>();
	private final List<Route> routes = new ArrayList<>();

	public City(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<CityMission> getMissions() {
		return missions;
	}

	public Optional<CityMission> getMissionByName(String missionName) {
		return missions.stream().filter(mission -> mission.getName().equalsIgnoreCase(missionName)).findFirst();
	}

	private void addRoute(Route route) {
		routes.add(route);
	}

	public void addDoubleRoute(Route route) {
		addRoute(route);
		route.getTo().addRoute(route);
	}

	public List<Route> getRoutes() {
		return routes;
	}

}
