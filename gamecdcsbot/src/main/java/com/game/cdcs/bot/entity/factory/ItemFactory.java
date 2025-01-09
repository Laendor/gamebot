package com.game.cdcs.bot.entity.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.City;
import com.game.cdcs.bot.entity.GoldMultiplier;
import com.game.cdcs.bot.entity.ItemEffect;
import com.game.cdcs.bot.entity.Radar;
import com.game.cdcs.bot.entity.TravelDiscount;
import com.game.cdcs.bot.entity.Trophy;
import com.game.cdcs.bot.repository.ItemEffectRepository;

@Component
public class ItemFactory {

	@Autowired
	public ItemEffectRepository itemRepository;

	public ItemEffect createSpecialEffect(Long id, String type, Map<String, Object> params) {
		switch (type.toLowerCase()) {
		case "radar":
			int durationDays = (int) params.get("durationDays");
			return new Radar(id, durationDays);
		case "traveldiscount":
			int discountPercentage = (int) params.get("discountPercentage");
			int discountDuration = (int) params.get("durationDays");
			return new TravelDiscount(id, discountPercentage, discountDuration);
		case "goldmultiplier":
			double multiplier = (double) params.get("multiplier");
			int multiplierDuration = (int) params.get("durationDays");
			return new GoldMultiplier(id, multiplier, multiplierDuration);
		case "trophy":
			City city = (City) params.get("city");
			return new Trophy(id, city);
		default:
			throw new IllegalArgumentException("Tipo di effetto speciale non supportato: " + type);
		}
	}
}