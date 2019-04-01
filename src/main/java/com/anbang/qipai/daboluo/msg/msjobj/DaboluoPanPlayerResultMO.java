package com.anbang.qipai.daboluo.msg.msjobj;

import com.anbang.qipai.daboluo.cqrs.q.dbo.DaboluoPanPlayerResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;

public class DaboluoPanPlayerResultMO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int score;// 一盘结算分
	private int totalScore;// 总分

	public DaboluoPanPlayerResultMO() {

	}

	public DaboluoPanPlayerResultMO(PukeGamePlayerDbo playerDbo, DaboluoPanPlayerResultDbo panPlayerResult) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
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

}
