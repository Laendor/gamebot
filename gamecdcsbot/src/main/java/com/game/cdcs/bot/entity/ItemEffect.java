package com.game.cdcs.bot.entity;

public abstract class ItemEffect {

	protected final Long id;
	protected final String name;

	public ItemEffect(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public abstract String getDescription();

}
