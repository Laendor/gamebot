package com.game.cdcs.bot.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.game.cdcs.bot.entity.Goal;

@Component
public class GoalRepository {

	private final Map<Long, Goal> goals = new HashMap<>();

	public void put(Long id, Goal goal) {
		goals.put(id, goal);
	}

	public Optional<Goal> get(Long id) {
		return Optional.ofNullable(goals.get(id));
	}

	public Optional<Goal> getRandom() {
		if (getSize() == 0) {
			return Optional.empty();
		}
		List<Goal> values = new ArrayList<>(goals.values());
		return Optional.of(values.get(new Random().nextInt(values.size())));
	}

	public List<Goal> getRandom(int numberOfRandomGoals) {
		if (goals == null || goals.isEmpty() || numberOfRandomGoals <= 0) {
			return Collections.emptyList();
		}

		List<Goal> goalsAsList = new ArrayList<>(goals.values());

		if (numberOfRandomGoals >= goalsAsList.size()) {
			Collections.shuffle(goalsAsList);
			return goalsAsList;
		}

		Set<Goal> selectedGoals = new HashSet<>();
		Random random = new Random();

		while (selectedGoals.size() < numberOfRandomGoals) {
			Goal randomValue = goalsAsList.get(random.nextInt(goalsAsList.size()));
			selectedGoals.add(randomValue);
		}

		return new ArrayList<Goal>(selectedGoals);
	}

	public int getSize() {
		return goals.size();
	}

}
