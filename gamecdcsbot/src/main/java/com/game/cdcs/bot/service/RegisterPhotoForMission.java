package com.game.cdcs.bot.service;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.game.cdcs.bot.entity.CityMission;
import com.game.cdcs.bot.entity.Photo;
import com.game.cdcs.bot.entity.PhotoState;
import com.game.cdcs.bot.entity.CityMissionRecord;
import com.game.cdcs.bot.entity.PlayerProfile;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.MissionPhotoRepository;
import com.game.cdcs.bot.repository.MissionRecordRepository;
import com.game.cdcs.bot.repository.MissionRepository;
import com.game.cdcs.bot.repository.PlayerProfileRepository;

@Component
public class RegisterPhotoForMission {

	@Autowired
	public PlayerProfileRepository playerProfileRepository;

	@Autowired
	public MissionRepository missionRepository;

	@Autowired
	public MissionRecordRepository missionRecordRepository;

	@Autowired
	public MissionPhotoRepository missionPhotoRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Update update) {
		Long playerId = update.getMessage().getChatId();
		Optional<PlayerProfile> profileOpt = playerProfileRepository.get(playerId);
		if (profileOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(playerId, "Profile non trovato"));
		}
		var profile = profileOpt.get();

		String missionName = profile.getMissionNameAwatingPhoto();
		if (Strings.isBlank(missionName)) {
			return new SendResult(telegramHelper.buildSendTextMessage(playerId,
					"Il profilo non è in attesa di nessuna foto da inviare."));
		}

		var missionOpt = missionRepository.get(missionName);
		if (missionOpt.isEmpty()) {
			return new SendResult(telegramHelper.buildSendTextMessage(playerId, "Missione non trovata"));
		}
		var mission = missionOpt.get();

		String photoTelegramId = update.getMessage().getPhoto().get(0).getFileId();
		var missionPhoto = new Photo(missionPhotoRepository.nextId(), photoTelegramId, null, LocalDate.now());
		missionPhotoRepository.put(missionPhoto);

		var missionRecord = getExistingMissionRecordOrCreateNew(mission, profile, missionPhoto);
		missionPhoto.setMissionRecord(missionRecord);

		profile.clearMissionNameAwatingPhoto();
		return new SendResult(telegramHelper.buildSendTextMessage(playerId, "Foto ricevuta per la missione: "
				+ mission.getGoal().getDescription() + ". L'amministratore valuterà la tua foto."));
	}

	private CityMissionRecord getExistingMissionRecordOrCreateNew(CityMission mission, PlayerProfile profile,
			Photo missionPhoto) {
		var missionRecordOpt = profile.getMissionRecords().stream()
				.filter(mr -> mr.getMission().getName().equals(mission.getName())).findFirst();
		if (missionRecordOpt.isPresent()) {
			var missionRecord = missionRecordOpt.get();
			var oldMissionPhoto = missionRecord.getMissionPhoto();
			oldMissionPhoto.setState(PhotoState.REPLACED);
			missionRecord.setMissionPhoto(missionPhoto);
			return missionRecord;
		}
		var missionRecord = new CityMissionRecord(mission, LocalDate.now(), profile, missionPhoto);
		profile.getMissionRecords().add(missionRecord);
		missionRecordRepository.put(missionRecord);
		return missionRecord;
	}
}
