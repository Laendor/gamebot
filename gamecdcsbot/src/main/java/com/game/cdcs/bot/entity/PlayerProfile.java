package com.game.cdcs.bot.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlayerProfile {
	private final Long chatId;
	private final String name;
	private final Set<String> trophyCities;
	private final List<MissionRecord> missionRecords;
	private String currentCity;
	private double goldMultiplier;
	private boolean radarActive;
	private int gold;
	private int trophies;
	private int radarDuration;
	private int travelDiscount;
	private int discountDuration;
	private int multiplierDuration;
	private String missionNameAwatingPhoto;

	public PlayerProfile(Long chatId, String name, int gold, String currentCity) {
		this.chatId = chatId;
		this.name = name;
		this.gold = gold;
		this.currentCity = currentCity;
		this.trophyCities = new HashSet<>();
		this.missionRecords = new ArrayList<>();
		this.radarActive = false;
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

	public Set<String> getTrophyCities() {
		return trophyCities;
	}

	public int getTrophies() {
		return trophies;
	}

	public void addTrophy() {
		this.trophies++;
		trophyCities.add(currentCity);
	}

	public void addTrophy(String city) {
		this.trophies++;
		trophyCities.add(city);
	}

	public boolean hasTrophyFrom(String city) {
		return trophyCities.contains(city);
	}

	public String getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	public boolean isRadarActive() {
		return radarActive;
	}

	public void setRadarActive(boolean radarActive) {
		this.radarActive = radarActive;
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
			radarActive = false;

		if (discountDuration > 0)
			discountDuration--;
		if (discountDuration == 0)
			travelDiscount = 0;

		if (multiplierDuration > 0)
			multiplierDuration--;
		if (multiplierDuration == 0)
			goldMultiplier = 1.0;
	}

	public List<MissionRecord> getMissionRecords() {
		return missionRecords;
	}

	public boolean hasMissionInRecords(Mission mission) {
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

}
