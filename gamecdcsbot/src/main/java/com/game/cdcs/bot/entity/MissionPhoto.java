package com.game.cdcs.bot.entity;

import java.time.LocalDate;

public class MissionPhoto {

	private final Long id;
	private final String telegramId;
	private MissionRecord missionRecord;
	private final LocalDate uploadedAt;
	private MissionPhotoState state;

	public MissionPhoto(Long id, String telegramId, MissionRecord missionRecord, LocalDate uploadedAt) {
		this.id = id;
		this.telegramId = telegramId;
		this.missionRecord = missionRecord;
		this.uploadedAt = uploadedAt;
		this.state = MissionPhotoState.CREATED;
	}

	public Long getId() {
		return id;
	}

	public String getTelegramId() {
		return telegramId;
	}

	public void setMissionRecord(MissionRecord missionRecord) {
		this.missionRecord = missionRecord;
	}

	public MissionRecord getMissionRecord() {
		return missionRecord;
	}

	public LocalDate getUploadedAt() {
		return uploadedAt;
	}

	public MissionPhotoState getState() {
		return state;
	}

	public void setState(MissionPhotoState state) {
		this.state = state;
	}
}
