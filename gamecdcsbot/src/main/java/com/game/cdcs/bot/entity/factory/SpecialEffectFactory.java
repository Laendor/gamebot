package com.game.cdcs.bot.entity.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.GoldMultiplierEffect;
import com.game.cdcs.bot.entity.RadarEffect;
import com.game.cdcs.bot.entity.SpecialEffect;
import com.game.cdcs.bot.entity.TravelDiscountEffect;

@Component
public class SpecialEffectFactory {

	public SpecialEffect createSpecialEffect(String type, Map<String, Object> params) {
		switch (type.toLowerCase()) {
		case "radar":
			int durationDays = (int) params.get("durationDays");
			return new RadarEffect(durationDays);
		case "traveldiscount":
			int discountPercentage = (int) params.get("discountPercentage");
			int discountDuration = (int) params.get("durationDays");
			return new TravelDiscountEffect(discountPercentage, discountDuration);
		case "goldmultiplier":
			double multiplier = (double) params.get("multiplier");
			int multiplierDuration = (int) params.get("durationDays");
			return new GoldMultiplierEffect(multiplier, multiplierDuration);
		default:
			throw new IllegalArgumentException("Tipo di effetto speciale non supportato: " + type);
		}
	}
}