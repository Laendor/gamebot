package com.game.cdcs.bot.entity;

public class RadarEffect implements SpecialEffect {
	private final int durationDays;

	public RadarEffect(int durationDays) {
		this.durationDays = durationDays;
	}

	@Override
	public void applyEffect(PlayerProfile player) {
		player.setRadarActive(true);
		player.setRadarDuration(durationDays);
	}

	@Override
	public String getDescription() {
		return "Radar attivo per " + durationDays + " giorni";
	}

	@Override
	public String getName() {
		return "Radar";
	}
}