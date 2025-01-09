package com.game.cdcs.bot.entity;

public class Radar extends ItemEffect {
	private final int durationDays;

	public Radar(Long id, int durationDays) {
		super(id);
		this.durationDays = durationDays;
	}

	@Override
	public void use(PlayerProfile player) {
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