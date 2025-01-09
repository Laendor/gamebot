package com.game.cdcs.bot.entity;

public class Item {

	private final Long id;

	private final ItemEffect itemEffect;

	public Item(Long id, ItemEffect itemEffect) {
		this.id = id;
		this.itemEffect = itemEffect;
	}

	public Long getId() {
		return id;
	}

	public ItemEffect getItemEffect() {
		return itemEffect;
	}

}
