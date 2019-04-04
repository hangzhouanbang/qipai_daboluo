package com.anbang.qipai.daboluo.cqrs.c.domain.result;

public class DaboluoPanPlayerResult {
	private String playerId;
	private DaboluoJiesuanScore jiesuanScore;
	private int totalScore;// 总分

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public DaboluoJiesuanScore getJiesuanScore() {
		return jiesuanScore;
	}

	public void setJiesuanScore(DaboluoJiesuanScore jiesuanScore) {
		this.jiesuanScore = jiesuanScore;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
