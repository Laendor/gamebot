package com.game.cdcs.bot.entity;

public class TravelDiscount extends ItemEffect {

	private final int discountPercentage;
	private final int durationDays;

	public TravelDiscount(Long id, int discountPercentage, int durationDays) {
		super(id);
		this.discountPercentage = discountPercentage;
		this.durationDays = durationDays;
	}

	@Override
	public void use(PlayerProfile player) {
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
