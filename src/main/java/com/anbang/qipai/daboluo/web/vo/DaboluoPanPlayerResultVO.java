package com.anbang.qipai.daboluo.web.vo;

public class DaboluoPanPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private DaboluoPlayerShoupaiVO allShoupai;
	private boolean ying;
	private int score;// 一盘结算分
	private int totalScore;// 总分

	public DaboluoPanPlayerResultVO() {

	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public DaboluoPlayerShoupaiVO getAllShoupai() {
		return allShoupai;
	}

	public void setAllShoupai(DaboluoPlayerShoupaiVO allShoupai) {
		this.allShoupai = allShoupai;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public boolean isYing() {
		return ying;
	}

	public void setYing(boolean ying) {
		this.ying = ying;
	}

}
