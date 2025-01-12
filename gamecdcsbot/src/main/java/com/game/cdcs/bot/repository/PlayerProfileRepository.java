package com.game.cdcs.bot.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.PlayerProfile;

@Component
public class PlayerProfileRepository {

	private final Map<Long, PlayerProfile> playerProfiles = new HashMap<>();

	public void save(Long chatId) {
		playerProfiles.putIfAbsent(chatId, new PlayerProfile(chatId, "Giocatore_" + chatId, 100, "Roma"));
	}

	public Optional<PlayerProfile> get(Long chatId) {
		return Optional.ofNullable(playerProfiles.get(chatId));
	}

	public void resetMissionNameAwaitingPhoto(Long chatId) {
		playerProfiles.get(chatId).setMissionNameAwatingPhoto(null);
	}

	public List<PlayerProfile> getAllProfiles() {
		return new ArrayList<PlayerProfile>(playerProfiles.values());
	}

	public List<PlayerProfile> getAllProfilesExcept(Long profilePlayerToExclude) {
		return getAllProfiles().stream().filter(profile -> profile.getChatId() != profilePlayerToExclude).toList();
	}

}
