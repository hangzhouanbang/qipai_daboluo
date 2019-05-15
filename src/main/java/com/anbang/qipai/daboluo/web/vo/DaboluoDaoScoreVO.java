package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoDaoScore;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.Paixing;

public class DaboluoDaoScoreVO {

	private Paixing paixing;

	private List<PukePai> pukePaiList = new ArrayList<>();

	private int score;

	public DaboluoDaoScoreVO() {

	}

	public DaboluoDaoScoreVO(DaboluoDaoScore score, Dao dao) {
		paixing = dao.getPaixing();
		pukePaiList = dao.getPukePaiList();
		this.score = score.getScore();
	}

	public Paixing getPaixing() {
		return paixing;
	}

	public void setPaixing(Paixing paixing) {
		this.paixing = paixing;
	}

	public List<PukePai> getPukePaiList() {
		return pukePaiList;
	}

	public void setPukePaiList(List<PukePai> pukePaiList) {
		this.pukePaiList = pukePaiList;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
