package com.game.cdcs.bot.entity;

import java.time.LocalDate;
import java.util.Objects;

public class MissionRecord {

	private final Mission mission;
	private final PlayerProfile playerProfile;
	private MissionRecordState state;
	private final LocalDate startDate;
	private LocalDate completionDate;
	private MissionPhoto missionPhoto;

	public MissionRecord(Mission mission, LocalDate startDate, PlayerProfile playerProfile, MissionPhoto missionPhoto) {
		this.mission = mission;
		this.playerProfile = playerProfile;
		this.startDate = startDate;
		this.missionPhoto = missionPhoto;
		this.state = MissionRecordState.STARTED;
	}

	public Mission getMission() {
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

	public MissionRecordState getState() {
		return state;
	}

	public void setState(MissionRecordState state) {
		this.state = state;
	}

	public void setCompletionDate(LocalDate completionDate) {
		this.completionDate = completionDate;
	}

	public MissionPhoto getMissionPhoto() {
		return missionPhoto;
	}

	public void setMissionPhoto(MissionPhoto missionPhoto) {
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
		MissionRecord other = (MissionRecord) obj;
		return Objects.equals(mission.getName(), other.getMission().getName())
				&& Objects.equals(playerProfile.getName(), other.getPlayerProfile().getName());
	}

}
