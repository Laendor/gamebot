package com.game.cdcs.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Goal;
import com.game.cdcs.bot.handleupdate.SendResult;
import com.game.cdcs.bot.helper.TelegramHelper;
import com.game.cdcs.bot.repository.GoalRepository;

@Component
public class GenerateGoals {

	@Autowired
	public GoalRepository goalRepository;

	@Autowired
	public TelegramHelper telegramHelper;

	public SendResult buildSendResult(Long chatId) {
		var idGoal = 0L;

		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa tre oggetti rossi"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa cinque oggetti rossi"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa venti persone"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa un monumento raffigurante un cavallo"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa una coccinella sopra una foglia"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa tre auto della stessa casa automobilistica"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa la punta dell'edificio pià alto della tua città"));
		goalRepository.put(++idGoal, new Goal(idGoal, "Fotografa un palloncino in aria"));

		return new SendResult(
				telegramHelper.buildSendTextMessage(chatId, "Generati " + idGoal + " goal per missioni."));
	}
}
