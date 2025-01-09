package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.CityMissionRecord;

@Component
public class MissionRecordRepository {

	private final Map<Object, CityMissionRecord> missions = new HashMap<>();

	public Optional<CityMissionRecord> get(String id) {
		return Optional.ofNullable(missions.get(id));
	}

	public void put(CityMissionRecord missionRecord) {
		missions.put(missionRecord.getMission().getName() + missionRecord.getPlayerProfile().getChatId(),
				missionRecord);
	}

}
