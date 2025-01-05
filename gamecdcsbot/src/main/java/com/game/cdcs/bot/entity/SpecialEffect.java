package com.game.cdcs.bot.entity;

public interface SpecialEffect {

	void applyEffect(PlayerProfile player);

	String getName();

	String getDescription();
}
