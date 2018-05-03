package com.nhance.assignment.datastore;

import com.nhance.assignment.constants.Constants.GameLevel;

public class Threshold {

	private GameLevel gameLevel;
	private int goodScore;
	private int goodBonus;
	private int normalBonus;
	public GameLevel getGameLevel() {
		return gameLevel;
	}
	public void setGameLevel(GameLevel gameLevel) {
		this.gameLevel = gameLevel;
	}
	public int getGoodScore() {
		return goodScore;
	}
	public void setGoodScore(int goodScore) {
		this.goodScore = goodScore;
	}
	public int getGoodBonus() {
		return goodBonus;
	}
	public void setGoodBonus(int goodBonus) {
		this.goodBonus = goodBonus;
	}
	public int getNormalBonus() {
		return normalBonus;
	}
	public void setNormalBonus(int normalBonus) {
		this.normalBonus = normalBonus;
	}
	@Override
	public String toString() {
		return "Threshold [gameLevel=" + gameLevel + ", goodScore=" + goodScore
				+ ", goodBonus=" + goodBonus + ", normalBonus=" + normalBonus
				+ "]";
	}
}
