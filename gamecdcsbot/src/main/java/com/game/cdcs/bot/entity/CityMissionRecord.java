package com.game.cdcs.bot.entity;

import java.time.LocalDate;
import java.util.Objects;

public class CityMissionRecord {

	private final CityMission mission;
	private final PlayerProfile playerProfile;
	private CityMissionRecordState state;
	private final LocalDate startDate;
	private LocalDate completionDate;
	private Photo missionPhoto;

	public CityMissionRecord(CityMission mission, LocalDate startDate, PlayerProfile playerProfile, Photo missionPhoto) {
		this.mission = mission;
		this.playerProfile = playerProfile;
		this.startDate = startDate;
		this.missionPhoto = missionPhoto;
		this.state = CityMissionRecordState.STARTED;
	}

	public CityMission getMission() {
		return mission;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getCompletionDate() {
		return completionDate;
	}

	public PlayerProfile getPlayerProfile() {
		return playerProfile;
	}

	public CityMissionRecordState getState() {
		return state;
	}

	public void setState(CityMissionRecordState state) {
		this.state = state;
	}

	public void setCompletionDate(LocalDate completionDate) {
		this.completionDate = completionDate;
	}

	public Photo getMissionPhoto() {
		return missionPhoto;
	}

	public void setMissionPhoto(Photo missionPhoto) {
		this.missionPhoto = missionPhoto;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mission, playerProfile);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CityMissionRecord other = (CityMissionRecord) obj;
		return Objects.equals(mission.getName(), other.getMission().getName())
				&& Objects.equals(playerProfile.getName(), other.getPlayerProfile().getName());
	}

}
