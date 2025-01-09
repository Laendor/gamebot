package com.game.cdcs.bot.entity;

public abstract class ItemEffect {

	protected final Long id;

	public ItemEffect(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public abstract void use(PlayerProfile player);

	public abstract String getName();

	public abstract String getDescription();

}
