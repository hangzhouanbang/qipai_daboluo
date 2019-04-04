package com.anbang.qipai.daboluo.web.vo;

import com.anbang.qipai.daboluo.cqrs.q.dbo.DaboluoPanPlayerResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;

public class DaboluoPanPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private DaboluoJiesuanScoreVO score;// 一盘结算分
	private int totalScore;// 总分

	public DaboluoPanPlayerResultVO() {

	}

	public DaboluoPanPlayerResultVO(PukeGamePlayerDbo playerDbo, DaboluoPanPlayerResultDbo panPlayerResult) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		score = new DaboluoJiesuanScoreVO(panPlayerResult.getPlayerResult().getJiesuanScore());
		totalScore = panPlayerResult.getPlayerResult().getTotalScore();
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

	public DaboluoJiesuanScoreVO getScore() {
		return score;
	}

	public void setScore(DaboluoJiesuanScoreVO score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
