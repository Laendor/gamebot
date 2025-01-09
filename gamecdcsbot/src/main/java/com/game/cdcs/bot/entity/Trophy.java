package com.game.cdcs.bot.entity;

public class Trophy extends ItemEffect {

	private final City city;

	public Trophy(Long id, City city) {
		super(id);
		this.city = city;
	}

	@Override
	public void use(PlayerProfile player) {
	}

	@Override
	public String getName() {
		return "Trofeo";
	}

	@Override
	public String getDescription() {
		return "Trofeo di " + city.getName();
	}

	public City getCity() {
		return city;
	}

}
