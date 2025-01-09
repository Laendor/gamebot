package com.game.cdcs.bot.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.ItemEffect;
import com.game.cdcs.bot.entity.Trophy;

@Component
public class ItemEffectRepository {

	private final Map<Long, ItemEffect> items = new HashMap<>();

	public Optional<ItemEffect> get(Long id) {
		return Optional.ofNullable(items.get(id));
	}

	public Long nextId() {
		var maxOpt = items.entrySet().stream().mapToLong(item -> item.getKey()).max();
		return maxOpt.isEmpty() ? 1 : maxOpt.getAsLong() + 1;
	}

	public void put(ItemEffect item) {
		items.put(nextId(), item);
	}

	public Optional<ItemEffect> getRandom() {
		if (getSize() == 0) {
			return Optional.empty();
		}
		List<ItemEffect> values = new ArrayList<>(getItemEffectsForRandomAccess());
		return Optional.of(values.get(new Random().nextInt(values.size())));
	}

	private List<ItemEffect> getItemEffectsForRandomAccess() {
		return items.values().stream().filter(itemEffect -> !itemEffect.getName().equals("Trofeo")).toList();
	}

	public int getSize() {
		return items.size();
	}

	public Optional<ItemEffect> getTrophyOfCity(City city) {
		var trophyOpt = items.values().stream()//
				.filter(itemEffect -> itemEffect.getName().equals("Trofeo"))//
				.map(itemEffect -> (Trophy) itemEffect)//
				.filter(trophy -> trophy.getCity().equals(city))//
				.findFirst();
		if (trophyOpt.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(trophyOpt.get());
	}

}
