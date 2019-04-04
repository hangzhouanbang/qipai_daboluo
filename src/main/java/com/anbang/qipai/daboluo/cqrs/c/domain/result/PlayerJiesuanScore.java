package com.anbang.qipai.daboluo.cqrs.c.domain.result;

import com.dml.shisanshui.pai.paixing.Paixing;

/**
 * 对某个玩家的结算结果
 * 
 * @author lsc
 *
 */
public class PlayerJiesuanScore {
	private String playerId;
	private int toudao;// 头道基础分
	private Paixing toudaoPaixing;
	private int zhongdao;// 中道基础分
	private Paixing zhongdaoPaixing;
	private int weidao;// 尾道基础分
	private Paixing weidaoPaixing;
	private int score;// 基础结算分
	private int value;// 总结算分

	public void calculateScore() {
		score = toudao + zhongdao + weidao;
	}

	public boolean hasWumei() {
		return Paixing.wumei.equals(toudaoPaixing) || Paixing.wumei.equals(zhongdaoPaixing)
				|| Paixing.wumei.equals(weidaoPaixing);
	}

	/**
	 * 是否打枪
	 */
	public boolean isDaqiang() {
		return score == 6;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getToudao() {
		return toudao;
	}

	public void setToudao(int toudao) {
		this.toudao = toudao;
	}

	public int getZhongdao() {
		return zhongdao;
	}

	public void setZhongdao(int zhongdao) {
		this.zhongdao = zhongdao;
	}

	public int getWeidao() {
		return weidao;
	}

	public void setWeidao(int weidao) {
		this.weidao = weidao;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Paixing getToudaoPaixing() {
		return toudaoPaixing;
	}

	public void setToudaoPaixing(Paixing toudaoPaixing) {
		this.toudaoPaixing = toudaoPaixing;
	}

	public Paixing getZhongdaoPaixing() {
		return zhongdaoPaixing;
	}

	public void setZhongdaoPaixing(Paixing zhongdaoPaixing) {
		this.zhongdaoPaixing = zhongdaoPaixing;
	}

	public Paixing getWeidaoPaixing() {
		return weidaoPaixing;
	}

	public void setWeidaoPaixing(Paixing weidaoPaixing) {
		this.weidaoPaixing = weidaoPaixing;
	}

}
