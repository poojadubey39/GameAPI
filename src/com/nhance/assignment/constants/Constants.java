package com.nhance.assignment.constants;

public class Constants {

	public static enum GameLevel{
		EASY,
		MEDIUM,
		HARD
	}
	
	public static enum UserSkillLevel{
		BEGINNER,
		INTERMEDIATE,
		ADVANCED,
		COMPLETED
	}

	public static final String PROPERTIES_PATH = "resources/config.properties";
	public static final String GAME_IDS = "game_ids";
	
	public static final String EASY_GOOD_BONUS = "easy_good_bonus";
	public static final String HARD_GOOD_BONUS = "hard_good_bonus";
	public static final String MEDIUM_GOOD_BONUS = "medium_good_bonus";
	
	public static final String EASY_NORMAL_BONUS = "easy_normal_bonus";
	public static final String HARD_NORMAL_BONUS = "hard_normal_bonus";
	public static final String MEDIUM_NORMAL_BONUS = "medium_normal_bonus";
}
