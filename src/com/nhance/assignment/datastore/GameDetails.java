package com.nhance.assignment.datastore;

import com.nhance.assignment.constants.Constants.GameLevel;

public class GameDetails {

	private int gameId;
	private GameLevel gameLevel;
	private String name;
	private boolean isPlayed;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public GameLevel getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPlayed() {
		return isPlayed;
	}

	public void setPlayed(boolean isPlayed) {
		this.isPlayed = isPlayed;
	}

	@Override
	public String toString() {
		return "Game [gameId=" + gameId + ", gameLevel=" + gameLevel
				+ ", name=" + name + ", isPlayed=" + isPlayed + "]";
	}

}
