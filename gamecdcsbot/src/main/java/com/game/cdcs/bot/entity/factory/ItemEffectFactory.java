package com.game.cdcs.bot.entity.factory;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.GoldMultiplier;
import com.game.cdcs.bot.entity.ItemEffect;
import com.game.cdcs.bot.entity.Radar;
import com.game.cdcs.bot.entity.TravelDiscount;

@Component
public class ItemEffectFactory {

	public enum ItemEffectFactoryType {
		Radar, TravelDiscount, GoldMultiplier
	};

	public final Map<ItemEffectFactoryType, Map<Integer, String>> itemEffectLevelNaming = Map.of(//
			ItemEffectFactoryType.Radar, //
			Map.of(//
					1, "Radar", //
					2, "Mega-radar", //
					3, "Master-Radar"//
			)//
			, ItemEffectFactoryType.GoldMultiplier, //
			Map.of(//
					1, "Amuleto d'oro", //
					2, "Mega-amuleto d'oro", //
					3, "Master-amuleto d'oro"//
			)//
			, ItemEffectFactoryType.TravelDiscount, //
			Map.of(//
					1, "Sconto viaggiatore", //
					2, "Mega-sconto viaggiatore", //
					3, "Master-Sconto viaggiatore")//
	);

	public ItemEffect createSpecialEffectByLevel(Long id, ItemEffectFactoryType type, int level) {
		return switch (type) {
		case Radar -> createRadar(id, level, itemEffectLevelNaming.get(type).get(level));
		case TravelDiscount -> createTravelDiscount(id, level, itemEffectLevelNaming.get(type).get(level));
		case GoldMultiplier -> createGoldMultiplier(id, level, itemEffectLevelNaming.get(type).get(level));
		default -> throw new IllegalArgumentException("Tipo di effetto speciale non supportato: " + type);
		};
	}

	public Radar createRadar(Long id, int level, String name) {
		return switch (level) {
		case 1 -> new Radar(id, name, 1);
		case 2 -> new Radar(id, name, 2);
		case 3 -> new Radar(id, name, 3);
		default -> throw new IllegalArgumentException("Livello di radar non supportato: " + level);
		};
	}

	public GoldMultiplier createGoldMultiplier(Long id, int level, String name) {
		return switch (level) {
		case 1 -> new GoldMultiplier(id, name, 1.2, 1);
		case 2 -> new GoldMultiplier(id, name, 1.3, 1);
		case 3 -> new GoldMultiplier(id, name, 1.45, 2);
		default -> throw new IllegalArgumentException("Livello di radar non supportato: " + level);
		};
	}

	public TravelDiscount createTravelDiscount(Long id, int level, String name) {
		return switch (level) {
		case 1 -> new TravelDiscount(id, name, 15, 1);
		case 2 -> new TravelDiscount(id, name, 20, 1);
		case 3 -> new TravelDiscount(id, name, 30, 2);
		default -> throw new IllegalArgumentException("Livello di radar non supportato: " + level);
		};
	}

}