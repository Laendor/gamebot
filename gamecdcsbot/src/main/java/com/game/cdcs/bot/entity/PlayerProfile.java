package com.game.cdcs.bot.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerProfile {
	private final Long chatId;
	private final String name;
	private final List<CityMissionRecord> missionRecords;
	private final List<Item> items;
	private City currentCity;
	private double goldMultiplier;
	private boolean isRadarActive;
	private boolean isDiscountActive;
	private int gold;
	private int radarDuration;
	private int travelDiscount;
	private int discountDuration;
	private int multiplierDuration;
	private String missionNameAwatingPhoto;

	public PlayerProfile(Long chatId, String name, int gold, City currentCity) {
		this.chatId = chatId;
		this.name = name;
		this.gold = gold;
		this.currentCity = currentCity;
		this.missionRecords = new ArrayList<>();
		this.items = new ArrayList<>();
		this.isRadarActive = false;
		this.radarDuration = 0;
		this.travelDiscount = 0;
		this.discountDuration = 0;
		this.goldMultiplier = 1.0;
		this.multiplierDuration = 0;
	}

	public Long getChatId() {
		return chatId;
	}

	public String getName() {
		return name;
	}

	public int getGold() {
		return gold;
	}

	public void addGold(int amount) {
		this.gold += amount;
	}

	public List<Item> getTrophyCities() {
		return items.stream()//
				.filter(item -> item.getItemEffect() instanceof Trophy)//
				.toList();
	}

	public long getTrophies() {
		return items.stream()//
				.filter(item -> item.getItemEffect() instanceof Trophy)//
				.map(item -> ((Trophy) (item.getItemEffect())).getCityName())//
				.distinct()//
				.count();
	}

	public boolean hasTrophyFrom(String city) {
		return items.stream()//
				.filter(item -> item.getItemEffect() instanceof Trophy)//
				.map(item -> (Trophy) item.getItemEffect())//
				.anyMatch(trophy -> trophy.getCityName().equals(city));
	}

	public City getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(City currentCity) {
		this.currentCity = currentCity;
	}

	public boolean isRadarActive() {
		return isRadarActive;
	}

	public void setRadarActive(boolean radarActive) {
		this.isRadarActive = radarActive;
	}

	public int getRadarDuration() {
		return radarDuration;
	}

	public void setRadarDuration(int radarDuration) {
		this.radarDuration = radarDuration;
	}

	public int getTravelDiscount() {
		return travelDiscount;
	}

	public void setTravelDiscount(int travelDiscount) {
		this.travelDiscount = travelDiscount;
	}

	public int getDiscountDuration() {
		return discountDuration;
	}

	public void setDiscountDuration(int discountDuration) {
		this.discountDuration = discountDuration;
	}

	public double getGoldMultiplier() {
		return goldMultiplier;
	}

	public void setGoldMultiplier(double goldMultiplier) {
		this.goldMultiplier = goldMultiplier;
	}

	public int getMultiplierDuration() {
		return multiplierDuration;
	}

	public void setMultiplierDuration(int multiplierDuration) {
		this.multiplierDuration = multiplierDuration;
	}

	// Logica per decrementare i giorni rimanenti, chiamata giornalmente
	public void dailyUpdate() {
		if (radarDuration > 0)
			radarDuration--;
		if (radarDuration == 0)
			isRadarActive = false;

		if (discountDuration > 0)
			discountDuration--;
		if (discountDuration == 0)
			travelDiscount = 0;

		if (multiplierDuration > 0)
			multiplierDuration--;
		if (multiplierDuration == 0)
			goldMultiplier = 1.0;
	}

	public List<CityMissionRecord> getMissionRecords() {
		return missionRecords;
	}

	public boolean hasMissionInRecords(CityMission mission) {
		return missionRecords.stream().map(missionRecord -> missionRecord.getMission())
				.anyMatch(missionOfRecord -> missionOfRecord.equals(mission));
	}

	public String getMissionNameAwatingPhoto() {
		return missionNameAwatingPhoto;
	}

	public void setMissionNameAwatingPhoto(String missionNameAwatingPhoto) {
		this.missionNameAwatingPhoto = missionNameAwatingPhoto;
	}

	public void clearMissionNameAwatingPhoto() {
		this.missionNameAwatingPhoto = null;
	}

	public List<Item> getItems() {
		return items;
	}

	public boolean hasItems() {
		return !items.isEmpty();
	}

	public Optional<Item> getItem(Long playerItemId) {
		return items.stream().filter(item -> item.getId() == playerItemId).findFirst();
	}

	public void removeItem(Item item) {
		items.remove(item);
	}

	public boolean isDiscountActive() {
		return isDiscountActive;
	}

	public void setDiscountActive(boolean isDiscountActive) {
		this.isDiscountActive = isDiscountActive;
	}
}
