package com.game.cdcs.bot.entity;

public class Trophy extends ItemEffect {

	private final City city;

	public Trophy(Long id, City city) {
		super(id, "Trofeo");
		this.city = city;
	}

	@Override
	public String getDescription() {
		return "Trofeo di " + city.getName();
	}

	public City getCity() {
		return city;
	}

	public String getCityName() {
		return city.getName();
	}

}
