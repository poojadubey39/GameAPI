package com.nhance.assignment.datastore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.nhance.assignment.constants.Constants;
import com.nhance.assignment.constants.Constants.GameLevel;
import com.nhance.assignment.constants.Constants.UserSkillLevel;

public class DataStore {
	private static Properties prop;
	private static InputStream input;
	private static Map<GameLevel, List<GameDetails>> gameLevelMap = new HashMap<>();
	private static Map<GameLevel, Threshold> thresholdMap = new HashMap<>();
	private static Map<UserSkillLevel, Long> userSkillMap = new HashMap<>();
	private static Map<UserSkillLevel, GameLevel> skillGameMap = new HashMap<>();

	public static void init() {
		prop = new Properties();
		try {
			input = new FileInputStream(Constants.PROPERTIES_PATH);
			prop.load(input);
			intializeGameLevelMap();
			initializeGameList();
			initializeThreshold();
			initializeSkillMap();
			initializeGameSkillMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void initializeGameSkillMap() {
		skillGameMap.put(UserSkillLevel.BEGINNER, GameLevel.EASY);
		skillGameMap.put(UserSkillLevel.INTERMEDIATE, GameLevel.MEDIUM);
		skillGameMap.put(UserSkillLevel.ADVANCED, GameLevel.HARD);
		skillGameMap.put(UserSkillLevel.COMPLETED, null);
		
	}

	private static void initializeSkillMap() {
		for (UserSkillLevel skillLevel : UserSkillLevel.values()) {
			String skillLevelScore = (String) prop.get(skillLevel.toString()
					.toLowerCase());
			if(skillLevel.equals(UserSkillLevel.BEGINNER)){
				userSkillMap.put(skillLevel, (long) 0);
				continue;
			}
			userSkillMap.put(skillLevel, Long.parseLong(skillLevelScore));

		}
	}

	private static void initializeThreshold() {
		for (GameLevel gameLevel : GameLevel.values()) {
			String thresholdScore = (String) prop.get(gameLevel.toString()
					.toLowerCase());
			Threshold threshold = new Threshold();
			threshold.setGameLevel(gameLevel);
			threshold.setGoodScore(Integer.parseInt(thresholdScore));
			String goodBonus = null;
			String normalBonus = null;
			if (gameLevel.equals(GameLevel.EASY)) {
				goodBonus = (String) prop.get(Constants.EASY_GOOD_BONUS);
				normalBonus = (String) prop.get(Constants.EASY_NORMAL_BONUS);
			}
			if (gameLevel.equals(GameLevel.MEDIUM)) {
				goodBonus = (String) prop.get(Constants.MEDIUM_GOOD_BONUS);
				normalBonus = (String) prop.get(Constants.MEDIUM_NORMAL_BONUS);
			}
			if (gameLevel.equals(GameLevel.HARD)) {
				goodBonus = (String) prop.get(Constants.HARD_GOOD_BONUS);
				normalBonus = (String) prop.get(Constants.HARD_NORMAL_BONUS);
			}
			if (goodBonus != null) {
				threshold.setGoodBonus(Integer.parseInt(goodBonus));
			}
			if (normalBonus != null) {
				threshold.setNormalBonus(Integer.parseInt(normalBonus));
			}

			thresholdMap.put(gameLevel, threshold);
		}
	}

	private static void intializeGameLevelMap() {
		List<GameDetails> easyGameList = new ArrayList<>();
		List<GameDetails> mediumGameList = new ArrayList<>();
		List<GameDetails> hardGameList = new ArrayList<>();

		for (GameLevel gameLevel : GameLevel.values()) {
			if (gameLevel.equals(GameLevel.EASY))
				gameLevelMap.put(gameLevel, easyGameList);
			if (gameLevel.equals(GameLevel.MEDIUM))
				gameLevelMap.put(gameLevel, mediumGameList);
			if (gameLevel.equals(GameLevel.HARD))
				gameLevelMap.put(gameLevel, hardGameList);
		}

	}

	private static void initializeGameList() {

		String gameIds = (String) (prop.get(Constants.GAME_IDS));
		String[] idArray = gameIds.split(",");
		for (String id : idArray) {
			GameDetails game = new GameDetails();
			game.setGameId(Integer.parseInt(id));
			String gameDetails = (String) prop.get(id);
			String gameName = gameDetails.split(",")[0];
			GameLevel gameLevel = GameLevel.valueOf(gameDetails.split(",")[1]
					.toUpperCase());
			game.setName(gameName);
			game.setGameLevel(gameLevel);
			gameLevelMap.get(gameLevel).add(game);
		}
	}

	public static List<GameDetails> getGameList(GameLevel level) {
		return gameLevelMap.get(level);
	}

	public static Threshold getThreshold(GameLevel level) {
		return thresholdMap.get(level);
	}

	public static long getSkillScore(UserSkillLevel skillLevel) {
		return userSkillMap.get(skillLevel);
	}
	
	public static GameLevel getGameAgainstSkillLevel(UserSkillLevel userSkillLevel){
		return skillGameMap.get(userSkillLevel);
	}
	
	public static int getTotalGames(){
		int totalGames = 0;
		Set<GameLevel> gameLevelKeySet = gameLevelMap.keySet();
		for(GameLevel gameLevel :  gameLevelKeySet){
			totalGames = totalGames + gameLevelMap.get(gameLevel).size();
		}
		return totalGames;
	}
}
