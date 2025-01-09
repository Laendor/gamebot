package com.game.cdcs.bot.entity;

public class GoldMultiplier extends ItemEffect {
	private final double multiplier;
	private final int durationDays;

	public GoldMultiplier(Long id, double multiplier, int durationDays) {
		super(id);
		this.multiplier = multiplier;
		this.durationDays = durationDays;
	}

	@Override
	public void use(PlayerProfile player) {
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
