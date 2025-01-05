package com.game.cdcs.bot.entity;

public class TravelDiscountEffect implements SpecialEffect {
	private final int discountPercentage;
	private final int durationDays;

	public TravelDiscountEffect(int discountPercentage, int durationDays) {
		this.discountPercentage = discountPercentage;
		this.durationDays = durationDays;
	}

	@Override
	public void applyEffect(PlayerProfile player) {
		player.setTravelDiscount(discountPercentage);
		player.setDiscountDuration(durationDays);
	}

	@Override
	public String getDescription() {
		return "Sconto del " + discountPercentage + "% sui viaggi per " + durationDays + " giorni";
	}

	@Override
	public String getName() {
		return "Sconto del viaggiatore";
	}
}
