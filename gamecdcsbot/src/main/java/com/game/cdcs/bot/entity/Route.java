package com.game.cdcs.bot.entity;

public class Route {

	private final City from;

	private final City to;

	private final int cost;

	private boolean isLocked;

	public Route(City from, City to, int cost) {
		this.from = from;
		this.to = to;
		this.cost = cost;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public City getFrom() {
		return from;
	}

	public City getTo() {
		return to;
	}

	public int getCost() {
		return cost;
	}

}
