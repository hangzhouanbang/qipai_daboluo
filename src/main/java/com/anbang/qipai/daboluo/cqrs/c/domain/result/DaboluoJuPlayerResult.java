package com.anbang.qipai.daboluo.cqrs.c.domain.result;

public class DaboluoJuPlayerResult {
	private String playerId;
	private int yingCount;
	private int totalScore;

	public void increaseYingCount() {
		yingCount++;
	}

	public void increaseTotalScore(int score) {
		totalScore += score;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getYingCount() {
		return yingCount;
	}

	public void setYingCount(int yingCount) {
		this.yingCount = yingCount;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
