package com.game.cdcs.bot.service;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Trophy;
import com.game.cdcs.bot.entity.factory.ItemEffectFactory;
import com.game.cdcs.bot.entity.factory.ItemEffectFactory.ItemEffectFactoryType;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.ItemEffectRepository;

@Component
public class GenerateItemEffects {

	@Autowired
	public ItemEffectRepository itemEffectsRepository;

	@Autowired
	public ItemEffectFactory itemEffectFactory;

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {

		generateGenericItemEffect(ItemEffectFactoryType.Radar);
		generateGenericItemEffect(ItemEffectFactoryType.GoldMultiplier);
		generateGenericItemEffect(ItemEffectFactoryType.TravelDiscount);

		generateUniqueItemEffectTrophyForeachCity();

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Generati effetti d'oggetto per missioni."));

	}

	private void generateGenericItemEffect(ItemEffectFactoryType type) {
		var maxLevel = itemEffectFactory.itemEffectLevelNaming.get(type).keySet().stream().mapToInt(x -> x).max()
				.getAsInt();
		Stream.iterate(1, n -> n + 1)//
				.limit(maxLevel)//
				.map(level -> itemEffectFactory.createSpecialEffectByLevel(itemEffectsRepository.nextId(), type, level))
				.forEach(specialEffect -> itemEffectsRepository.put(specialEffect));
	}

	private void generateUniqueItemEffectTrophyForeachCity() {
		cityRepository.getAll().stream()//
				.map(city -> new Trophy(itemEffectsRepository.nextId(), city))//
				.forEach(trophy -> itemEffectsRepository.put(trophy));
	}

}
