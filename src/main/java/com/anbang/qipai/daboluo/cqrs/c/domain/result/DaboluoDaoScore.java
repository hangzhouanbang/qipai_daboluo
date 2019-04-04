package com.anbang.qipai.daboluo.cqrs.c.domain.result;

import com.dml.shisanshui.pai.paixing.Paixing;

/**
 * 每道结算分
 * 
 * @author lsc
 *
 */
public class DaboluoDaoScore {
	private Paixing paixing;
	private int score;

	public void jiesaunScore(int detal) {
		score += detal;
	}

	public boolean isTiezhi() {
		return Paixing.tiezhi.equals(paixing);
	}

	public boolean isWumei() {
		return Paixing.wumei.equals(paixing);
	}

	public boolean isTonghuashun() {
		return Paixing.tonghuashun.equals(paixing);
	}

	public Paixing getPaixing() {
		return paixing;
	}

	public void setPaixing(Paixing paixing) {
		this.paixing = paixing;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
