package com.game.cdcs.bot.entity;

public class GoldMultiplierEffect implements SpecialEffect {
	private final double multiplier;
	private final int durationDays;

	public GoldMultiplierEffect(double multiplier, int durationDays) {
		this.multiplier = multiplier;
		this.durationDays = durationDays;
	}

	@Override
	public void applyEffect(PlayerProfile player) {
		player.setGoldMultiplier(multiplier);
		player.setMultiplierDuration(durationDays);
	}

	@Override
	public String getDescription() {
		return "Moltiplicatore di oro x" + multiplier + " per " + durationDays + " giorni";
	}

	@Override
	public String getName() {
		return "Moltiplicatore d'oro";
	}
}
