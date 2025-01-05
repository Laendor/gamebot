package com.game.cdcs.bot.repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;

@Component
public class CityRepository {

	private final Map<String, City> cities = new HashMap<>();

	public Optional<City> get(String name) {
		return Optional.ofNullable(cities.get(name));
	}

	public void put(String string, City city) {
		cities.put(string, city);
	}

	public Collection<City> getAll() {
		return cities.values();
	}

}
