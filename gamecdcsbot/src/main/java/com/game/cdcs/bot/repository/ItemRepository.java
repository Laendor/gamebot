package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Item;

@Component
public class ItemRepository {

	private final Map<Long, Item> items = new HashMap<>();

	public Optional<Item> get(Long id) {
		return Optional.ofNullable(items.get(id));
	}

	public Long nextId() {
		var maxOpt = items.entrySet().stream().mapToLong(item -> item.getKey()).max();
		return maxOpt.isEmpty() ? 1 : maxOpt.getAsLong() + 1;
	}

	public void put(Item item) {
		items.put(nextId(), item);
	}

	public int getSize() {
		return items.size();
	}

}
