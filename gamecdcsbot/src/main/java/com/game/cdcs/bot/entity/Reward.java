package com.game.cdcs.bot.entity;

import java.util.Optional;

public class Reward {

	private final int goldReward;
	private final Optional<ItemEffect> specialEffectReward;

	public Reward(int goldReward, Optional<ItemEffect> specialEffectReward) {
		this.goldReward = goldReward;
		this.specialEffectReward = specialEffectReward;
	}

	public int getGoldReward() {
		return goldReward;
	}

	public Optional<ItemEffect> getSpecialEffectReward() {
		return specialEffectReward;
	}

	public boolean isTrophy() {
		return specialEffectReward.isPresent() && specialEffectReward.get() instanceof Trophy;
	}

	public boolean isNoTrophy() {
		return specialEffectReward.isPresent() && !(specialEffectReward.get() instanceof Trophy);
	}

}
