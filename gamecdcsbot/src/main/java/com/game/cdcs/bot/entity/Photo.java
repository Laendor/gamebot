package com.game.cdcs.bot.entity;

import java.time.LocalDate;

public class Photo {

	private final Long id;
	private final String telegramId;
	private CityMissionRecord missionRecord;
	private final LocalDate uploadedAt;
	private PhotoState state;

	public Photo(Long id, String telegramId, CityMissionRecord missionRecord, LocalDate uploadedAt) {
		this.id = id;
		this.telegramId = telegramId;
		this.missionRecord = missionRecord;
		this.uploadedAt = uploadedAt;
		this.state = PhotoState.CREATED;
	}

	public Long getId() {
		return id;
	}

	public String getTelegramId() {
		return telegramId;
	}

	public void setMissionRecord(CityMissionRecord missionRecord) {
		this.missionRecord = missionRecord;
	}

	public CityMissionRecord getMissionRecord() {
		return missionRecord;
	}

	public LocalDate getUploadedAt() {
		return uploadedAt;
	}

	public PhotoState getState() {
		return state;
	}

	public void setState(PhotoState state) {
		this.state = state;
	}
}
