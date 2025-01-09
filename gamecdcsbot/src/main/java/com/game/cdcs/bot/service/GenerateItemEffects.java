package com.game.cdcs.bot.service;

import java.util.Map;

import org.apache.commons.lang3.mutable.MutableLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.factory.ItemFactory;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.CityRepository;
import com.game.cdcs.bot.repository.ItemEffectRepository;

@Component
public class GenerateItemEffects {

	@Autowired
	public ItemEffectRepository itemEffectsRepository;

	@Autowired
	public ItemFactory itemEffectFactory;

	@Autowired
	public CityRepository cityRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {
		var idItemEffects = 0L;

		idItemEffects = generateRadars(idItemEffects);
		idItemEffects = generateGoldMultipliers(idItemEffects);
		idItemEffects = generateTravelDiscounts(idItemEffects);
		idItemEffects = generateTrophies(idItemEffects);

		return new SendResult(telegramHelper.buildSendTextMessage(chatId,
				"Generati " + idItemEffects + " effetti d'oggetto per missioni."));

	}

	private long generateRadars(long idItemEffects) {
		itemEffectsRepository
				.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "radar", Map.of("durationDays", 1)));
		itemEffectsRepository
				.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "radar", Map.of("durationDays", 2)));
		itemEffectsRepository
				.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "radar", Map.of("durationDays", 3)));
		return idItemEffects;
	}

	private long generateGoldMultipliers(long idItemEffects) {
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "goldmultiplier",
				Map.of("durationDays", 1, "multiplier", 1.2)));
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "goldmultiplier",
				Map.of("durationDays", 1, "multiplier", 1.3)));
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "goldmultiplier",
				Map.of("durationDays", 2, "multiplier", 1.45)));
		return idItemEffects;
	}

	private long generateTravelDiscounts(long idItemEffects) {
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "traveldiscount",
				Map.of("durationDays", 1, "discountPercentage", 15)));
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "traveldiscount",
				Map.of("durationDays", 1, "discountPercentage", 20)));
		itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(++idItemEffects, "traveldiscount",
				Map.of("durationDays", 2, "discountPercentage", 25)));
		return idItemEffects;
	}

	private long generateTrophies(long idItemEffects) {
		final MutableLong mutableIdItemEffects = new MutableLong(idItemEffects);

		cityRepository.getAll().forEach(city -> {
			itemEffectsRepository.put(itemEffectFactory.createSpecialEffect(mutableIdItemEffects.incrementAndGet(),
					"trophy", Map.of("city", city)));
		});

		return mutableIdItemEffects.getValue();
	}

}
