package com.anbang.qipai.daboluo.cqrs.c.domain.result;

public class DaboluoJuPlayerResult {
	private String playerId;
	private int tspx;// 特殊牌型
	private int qld;// 全垒打
	private int totalScore;

	public void increaseTspx() {
		tspx++;
	}

	public void increaseQld() {
		qld++;
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

	public int getTspx() {
		return tspx;
	}

	public void setTspx(int tspx) {
		this.tspx = tspx;
	}

	public int getQld() {
		return qld;
	}

	public void setQld(int qld) {
		this.qld = qld;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
