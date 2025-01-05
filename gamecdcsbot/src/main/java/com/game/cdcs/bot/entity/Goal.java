package com.game.cdcs.bot.entity;

public class Goal {

	private final Long id;
	private final String description;

	public Goal(Long id, String description) {
		this.id = id;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
