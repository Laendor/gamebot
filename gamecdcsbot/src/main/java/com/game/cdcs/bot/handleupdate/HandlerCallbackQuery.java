package com.game.cdcs.bot.handleupdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.service.AskPhotoForMission;
import com.game.cdcs.bot.service.ShowPhotoToApprove;
import com.game.cdcs.bot.service.GenerateCityMission;
import com.game.cdcs.bot.service.ShowAvailableCities;
import com.game.cdcs.bot.service.ShowCityMissions;
import com.game.cdcs.bot.service.ShowPhotosToApprove;
import com.game.cdcs.bot.service.ShowPlayerMenu;
import com.game.cdcs.bot.service.ShowPlayerMissionDetails;
import com.game.cdcs.bot.service.ShowPlayerMissions;
import com.game.cdcs.bot.service.ShowPublicPlayerProfile;
import com.game.cdcs.bot.service.TravelToCity;
import com.game.cdcs.bot.service.ValidatePhotoForMission;

@Component
public class HandlerCallbackQuery {

	@Autowired
	public ShowPublicPlayerProfile publicPlayerProfile;

	@Autowired
	public ShowPlayerMenu playMenu;

	@Autowired
	public TravelToCity changeCity;

	@Autowired
	public ShowCityMissions cityMissions;

	@Autowired
	public ShowPlayerMissions showPlayerMissions;

	@Autowired
	public ShowPlayerMissionDetails showPlayeMissionDetails;

	@Autowired
	public ShowAvailableCities availableCities;

	@Autowired
	public ShowPhotosToApprove showPhotosToApprove;

	@Autowired
	public GenerateCityMission generateCityMission;

	@Autowired
	public AskPhotoForMission askPhotoForMission;

	@Autowired
	public ShowPhotoToApprove checkPhotoForMission;

	@Autowired
	public ValidatePhotoForMission validatePhotoForMission;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildHandleCallbackQuerySendMessage(String callbackData, Long chatId) {

		if (callbackData.equals(CallbackCommand.PROFILE.name())) {
			return publicPlayerProfile.buildSendResult(chatId);
		}

		if (callbackData.equals(CallbackCommand.CITY_MISSIONS.name())) {
			return cityMissions.buildSendResult(chatId);
		}

		if (callbackData.equals(CallbackCommand.CHANGE_CITY.name())) {
			return availableCities.buildSendResult(chatId);
		}

		if (callbackData.equals(CallbackCommand.GENERATE_CITY_MISSIONS.name())) {
			return generateCityMission.buildSendResult(chatId);
		}

		if (callbackData.equals(CallbackCommand.PLAYER_MISSIONS.name())) {
			return showPlayerMissions.buildSendResult(chatId);
		}
		if (callbackData.equals(CallbackCommand.PHOTOS_TO_APPROVE.name())) {
			return showPhotosToApprove.buildSendResult(chatId);
		}

		if (callbackData.startsWith(CallbackCommand.MOVE_TO_.name())) {
			return changeCity.buildSendResult(chatId, callbackData.replace(CallbackCommand.MOVE_TO_.name(), ""));
		}
		if (callbackData.startsWith(CallbackCommand.ASK_PHOTO_FOR_MISSION_.name())) {
			return askPhotoForMission.buildSendResult(chatId,
					callbackData.replace(CallbackCommand.ASK_PHOTO_FOR_MISSION_.name(), ""));
		}
		if (callbackData.startsWith(CallbackCommand.PLAYER_MISSION_DETAILS_.name())) {
			return showPlayeMissionDetails.buildSendResult(chatId,
					callbackData.replace(CallbackCommand.PLAYER_MISSION_DETAILS_.name(), ""));
		}
		if (callbackData.startsWith(CallbackCommand.CHECK_PHOTO_.name())) {
			Long photoId = Long.parseLong(callbackData.replace(CallbackCommand.CHECK_PHOTO_.name(), ""));
			return checkPhotoForMission.buildSendResult(chatId, photoId);
		}
		if (callbackData.startsWith(CallbackCommand.ACCEPT_PHOTO_.name())) {
			Long photoId = Long.parseLong(callbackData.replace(CallbackCommand.ACCEPT_PHOTO_.name(), ""));
			return validatePhotoForMission.buildSendResult(chatId, photoId, true);
		}
		if (callbackData.startsWith(CallbackCommand.REJECT_PHOTO_.name())) {
			Long photoId = Long.parseLong(callbackData.replace(CallbackCommand.REJECT_PHOTO_.name(), ""));
			return validatePhotoForMission.buildSendResult(chatId, photoId, false);
		}

		return new SendResult(telegramHelper.buildSendTextMessage(chatId, "Opzione non valida."));

	}

}
