package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.MissionRecord;

@Component
public class MissionRecordRepository {

	private final Map<Object, MissionRecord> missions = new HashMap<>();

	public Optional<MissionRecord> get(String id) {
		return Optional.ofNullable(missions.get(id));
	}

	public void put(MissionRecord missionRecord) {
		missions.put(missionRecord.getMission().getName() + missionRecord.getPlayerProfile().getChatId(),
				missionRecord);
	}

}
