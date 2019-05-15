package com.anbang.qipai.daboluo.web.vo;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuPlayerResult;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;

public class DaboluoJuPlayerResultVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private int tspx;// 特殊牌型
	private int qld;// 全垒打
	private int totalScore;

	public DaboluoJuPlayerResultVO(PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		tspx = 0;
		qld = 0;
		totalScore = 0;
	}

	public DaboluoJuPlayerResultVO(DaboluoJuPlayerResult juPlayerResult, PukeGamePlayerDbo playerDbo) {
		playerId = playerDbo.getPlayerId();
		nickname = playerDbo.getNickname();
		headimgurl = playerDbo.getHeadimgurl();
		tspx = juPlayerResult.getTspx();
		qld = juPlayerResult.getQld();
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
