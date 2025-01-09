package com.game.cdcs.bot.entity;

public class CityMission {

	private final String name;
	private final Goal goal;
	private final City originCity;
	private final Reward reward;

	public CityMission(String name, Goal goal, City originCity, Reward reward) {
		this.name = name;
		this.goal = goal;
		this.originCity = originCity;
		this.reward = reward;
	}

	public String getName() {
		return name;
	}

	public Goal getGoal() {
		return goal;
	}

	public City getOriginCity() {
		return originCity;
	}

	public Reward getReward() {
		return reward;
	}

}
