package com.game.cdcs.bot.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Photo;
import com.game.cdcs.bot.entity.PhotoState;

@Component
public class MissionPhotoRepository {

	private final Map<Long, Photo> photos = new HashMap<>();

	public Optional<Photo> get(Long id) {
		return Optional.ofNullable(photos.get(id));
	}

	public void put(Photo photo) {
		photos.put(photo.getId(), photo);
	}

	public List<Photo> getPhotosAwaitingToBeApprovedOrderedByUploadedAtDesc() {
		return photos.values().stream().filter(photo -> photo.getState() == PhotoState.CREATED)//
				.sorted((f1, f2) -> f2.getUploadedAt().compareTo(f1.getUploadedAt()))//
				.toList();
	}

	public Long nextId() {
		var maxOpt = photos.entrySet().stream().mapToLong(photo -> photo.getKey()).max();
		return maxOpt.isEmpty() ? 1 : maxOpt.getAsLong() + 1;
	}

}
