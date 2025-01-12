package com.game.cdcs.bot.entity;

public class Radar extends ItemEffect implements ItemEffectUsableOnSelfPlayer {
	private final int durationDays;

	public Radar(Long id, String name, int durationDays) {
		super(id, name);
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

}