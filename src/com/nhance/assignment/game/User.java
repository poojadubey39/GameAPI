package com.nhance.assignment.game;

import com.nhance.assignment.constants.Constants.UserSkillLevel;
import com.nhance.assignment.datastore.GameDetails;

public class User {

	private int userId;
	private GameDetails currentGame;
	private UserSkillLevel currentSkillLevel;
	private long score;
	private double progressPercentage;
	private int gamesPlayed;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public GameDetails getCurrentGame() {
		return currentGame;
	}

	public void setCurrentGame(GameDetails currentGame) {
		this.currentGame = currentGame;
	}

	public UserSkillLevel getCurrentSkillLevel() {
		return currentSkillLevel;
	}

	public void setCurrentSkillLevel(UserSkillLevel currentSkillLevel) {
		this.currentSkillLevel = currentSkillLevel;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
	
	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public void incrementGamesPlayed() {
		this.gamesPlayed++;
	}

	public double getProgressPercentage() {
		return progressPercentage;
	}

	public void setProgressPercentage(double progressPercentage) {
		this.progressPercentage = progressPercentage;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", currentGame=" + currentGame
				+ ", currentSkillLevel=" + currentSkillLevel + ", score="
				+ score + ", progressPercentage=" + progressPercentage
				+ ", gamesPlayed=" + gamesPlayed + "]";
	}

}
