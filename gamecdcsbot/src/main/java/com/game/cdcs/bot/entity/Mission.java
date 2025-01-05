package com.game.cdcs.bot.entity;

import java.util.Optional;

public class Mission {

	private final String name;
	private final Goal goal;
	private final City originCity;
	private final int goldReward;
	private final boolean grantsTrophy;
	private final Optional<SpecialEffect> specialEffectReward;

	public Mission(String name, Goal goal, City originCity, int goldReward, boolean grantsTrophy,
			Optional<SpecialEffect> specialEffectReward) {
		this.name = name;
		this.goal = goal;
		this.originCity = originCity;
		this.goldReward = goldReward;
		this.grantsTrophy = grantsTrophy;
		this.specialEffectReward = specialEffectReward;
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

	public int getGoldReward() {
		return goldReward;
	}

	public boolean isGrantsTrophy() {
		return grantsTrophy;
	}

	public Optional<SpecialEffect> getSpecialEffectReward() {
		return specialEffectReward;
	}

}
