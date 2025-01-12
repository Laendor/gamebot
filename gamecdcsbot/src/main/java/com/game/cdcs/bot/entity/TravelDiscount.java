package com.game.cdcs.bot.entity;

public class TravelDiscount extends ItemEffect implements ItemEffectUsableOnSelfPlayer {

	private final int discountPercentage;
	private final int durationDays;

	public TravelDiscount(Long id, String name, int discountPercentage, int durationDays) {
		super(id, name);
		this.discountPercentage = discountPercentage;
		this.durationDays = durationDays;
	}

	@Override
	public void use(PlayerProfile player) {
		player.setTravelDiscount(discountPercentage);
		player.setDiscountDuration(durationDays);
		player.setDiscountActive(true);
	}

	@Override
	public String getDescription() {
		return "Sconto del " + discountPercentage + "% sui viaggi per " + durationDays + " giorni";
	}

}
