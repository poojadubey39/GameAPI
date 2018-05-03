package com.nhance.assignment.game;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.nhance.assignment.constants.Constants.GameLevel;
import com.nhance.assignment.constants.Constants.UserSkillLevel;
import com.nhance.assignment.datastore.DataStore;
import com.nhance.assignment.datastore.GameDetails;
import com.nhance.assignment.datastore.Threshold;

public class Game {

	private static final Game result = new Game();
	private User user;
	private FileHandler fileTxt;
	private SimpleFormatter formatterTxt;
	private static Logger LOGGER;
	private Game() {
		
		DataStore.init();
		try {
			initializeLogger();
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeLogger() throws SecurityException, IOException {
		LOGGER = Logger.getLogger(Game.class.getName());
		LOGGER.setLevel(Level.INFO);
		fileTxt = new FileHandler("Logging.txt");
		formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        LOGGER.addHandler(fileTxt);
        
        LOGGER.setUseParentHandlers(false);
		
	}

	public static Game getInstance() {
		return result;
	}
	
	public User startGame(int userId) {
		
		user = initializeUser(userId);
		return user;
		
	}

	
	public User submitScore(long score) throws Exception {
		LOGGER.info("Score submited by user: "+score);
		
		user.getCurrentGame().setPlayed(true);
		if (score == 0 ) {
			return user;
		} else if(score > 5000){
			throw new Exception("Score cannot exceed 5000");
		}else{
			user.incrementGamesPlayed();
			user = calculateUserResult(score);
		}
		return user;
	}

	private User initializeUser(int userId) {
		user = new User();
		user.setUserId(userId);
		user.setCurrentSkillLevel(UserSkillLevel.BEGINNER);
		user.setCurrentGame(DataStore.getGameList(GameLevel.EASY).get(0));
		user.setScore(0);
		return user;
	}

	private User calculateUserResult(long score) {
		
		UserSkillLevel currentSkillLevel = user.getCurrentSkillLevel();
		GameLevel currentGameLevel = user.getCurrentGame().getGameLevel();
		
		//totalUserScore
		long userScore = calculateTotalScore(currentGameLevel, score);
		LOGGER.info("Total game score of user: "+userScore);
		UserSkillLevel nextSkillLevel = getNextSkillLevel(currentSkillLevel);
		
		if (nextSkillLevel != null) {
			long skillScore = DataStore.getSkillScore(nextSkillLevel);
			LOGGER.info("User current skill: " + currentSkillLevel
					+ ". Next SkillLevel: " + nextSkillLevel
					+ ". Score needed to reach next skill: " + skillScore
					+ ". Current user score: " + userScore);
			if (userScore >= skillScore) {
				LOGGER.info("User cuurent skill changed to: "+nextSkillLevel);
				if(nextSkillLevel != UserSkillLevel.COMPLETED){
					user.setScore(0);
					user.setCurrentSkillLevel(nextSkillLevel);
				}else{
					LOGGER.info("User has completed all the levels.");
					user.setCurrentGame(null);
					user.setScore(userScore);
					return user;
				}
			} else {
				user.setScore(userScore);
			}
		}
		double progressPercent = calculateUserProgress();
		LOGGER.info("User progress in percentage: "+progressPercent);
		user.setProgressPercentage(progressPercent);
		
		GameDetails nextGame = getNextGameForUser(currentGameLevel);
		user.setCurrentGame(nextGame);
		
		
		return user;

	}

	private GameDetails getNextGameForUser(GameLevel currentGameLevel) {
		GameDetails nextGame = null;
		if (user.getScore() == 0) {
			GameLevel nextGameLevel = DataStore.getGameAgainstSkillLevel(user
					.getCurrentSkillLevel());
			LOGGER.info("User score is reset to 0, it will be assigned game from the next gameLevel: "
					+ nextGameLevel);
			List<GameDetails> gameList = DataStore.getGameList(nextGameLevel);
			LOGGER.info("Next Game: "+nextGame);
			nextGame = gameList.get(0);
		} else {
			LOGGER.info("User current game level: " + currentGameLevel);
			LOGGER.info("User score is not reset to 0. Hence assigning next game from the same or lower level.");
			List<GameDetails> gameList = DataStore
					.getGameList(currentGameLevel);
			for (GameDetails game : gameList) {
				if (!game.isPlayed()) {
					LOGGER.info("Assigning next game from same level.\nNext Game " + game);
					return game;
				}
			}
			GameLevel lowerLevel;
			switch (currentGameLevel) {
			case MEDIUM:
				lowerLevel = GameLevel.EASY;
				break;

			case HARD:
				lowerLevel = GameLevel.MEDIUM;
				break;

			default:
				lowerLevel = GameLevel.EASY;
			}
			LOGGER.info("Lower level : " + lowerLevel);
			List<GameDetails> lowerGameList = DataStore.getGameList(lowerLevel);
			for (GameDetails lowLevelGame : lowerGameList) {
				if (!lowLevelGame.isPlayed()) {
					nextGame = lowLevelGame;
					LOGGER.info("Assigning a game from lower level which has not been played yet. NewGame: "
							+ nextGame);
					break;
				}
			}
			if (nextGame == null) {
				int nextInt = ThreadLocalRandom.current().nextInt(0,
						lowerGameList.size());
				LOGGER.info("Since all the games at lower level have been played, assigning a random game from lower level");
				nextGame = lowerGameList.get(nextInt);
				LOGGER.info("Next Game: " + nextGame);
			}
		}
		return nextGame;
	}

	private double calculateUserProgress() {
		int totalGames = DataStore.getTotalGames();
		int gamesPlayed = user.getGamesPlayed();
		return (gamesPlayed / (double) totalGames) * 100;
	}

	private UserSkillLevel getNextSkillLevel(UserSkillLevel currentSkillLevel) {
		UserSkillLevel nextSkillLevel = null;
		switch (currentSkillLevel) {

		case BEGINNER:
			nextSkillLevel = UserSkillLevel.INTERMEDIATE;
			break;

		case INTERMEDIATE:
			nextSkillLevel = UserSkillLevel.ADVANCED;
			break;

		case ADVANCED:
			nextSkillLevel = UserSkillLevel.COMPLETED;
			break;
		default:
			break;
		}
		return nextSkillLevel;
	}

	private long calculateTotalScore(GameLevel currentGameLevel, long score) {
		long totalScore;
		Threshold threshold = DataStore.getThreshold(currentGameLevel);
		int goodScore = threshold.getGoodScore();
		LOGGER.info("Threshold for good score: "+goodScore);
		int bonus = 0;
		if (score >= goodScore) {
			bonus = threshold.getGoodBonus();
		} else {
			bonus = threshold.getNormalBonus();
		}
		LOGGER.info("Bonus percent based on user score : "+bonus);
		int bonusScore = (int) (score * (bonus / 100.0f));
		totalScore = score + bonusScore + user.getScore();
		return totalScore;
	}

}
