package com.anbang.qipai.daboluo.web.vo;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuPlayerResult;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;

public class DaboluoJuPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int yingCount;
	private int totalScore;

	public DaboluoJuPlayerResultVO(PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		yingCount = 0;
		totalScore = 0;
	}

	public DaboluoJuPlayerResultVO(DaboluoJuPlayerResult juPlayerResult, PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		yingCount = juPlayerResult.getYingCount();
		totalScore = juPlayerResult.getTotalScore();
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
