package com.game.cdcs.bot.entity;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MapGraph {
	private final Map<String, Map<String, Integer>> adjacencyMap = new HashMap<>();

	public void addCity(String city) {
		adjacencyMap.putIfAbsent(city, new HashMap<>());
	}

	public void addPath(String fromCity, String toCity, int cost) {
		adjacencyMap.get(fromCity).put(toCity, cost);
		adjacencyMap.get(toCity).put(fromCity, cost);
	}

	public Map<String, Integer> getNeighbors(String city) {
		return adjacencyMap.getOrDefault(city, new HashMap<>());
	}
}
