package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJiesuanScore;
import com.dml.shisanshui.pai.paixing.PaixingSolution;

public class DaboluoJiesuanScoreVO {
	private DaboluoDaoScoreVO toudao;// 第一道情况
	private DaboluoDaoScoreVO zhongdao;// 第二道情况
	private DaboluoDaoScoreVO weidao;// 第三道情况
	private List<PlayerJiesuanScoreVO> playerDaoList = new ArrayList<>();
	private Set<String> daqiangPlayerSet;
	private boolean daqiang;// 是否打枪
	private int dqbs = 1;// 打枪倍数
	private boolean quanleida;// 是否全垒打
	private int qldbs = 2;// 全垒打倍数
	private boolean tiezhi;// 是否有铁支
	private int tzbs = 2;// 铁支倍数
	private boolean tonghuashun;// 是否有同花顺
	private int thsbs = 2;// 同花顺倍数
	private boolean wumei;// 是否有五枚
	private int wmbs = 4;// 五枚倍数
	private boolean yitiaolong;// 是否一条龙
	private int score;// 基础结算分
	private int value;// 总分

	public DaboluoJiesuanScoreVO() {

	}

	public DaboluoJiesuanScoreVO(DaboluoJiesuanScore daboluoJiesuanScore) {
		PaixingSolution chupaiSolution = daboluoJiesuanScore.getChupaiSolution();
		toudao = new DaboluoDaoScoreVO(daboluoJiesuanScore.getToudao(), chupaiSolution.getToudao());
		zhongdao = new DaboluoDaoScoreVO(daboluoJiesuanScore.getZhongdao(), chupaiSolution.getZhongdao());
		weidao = new DaboluoDaoScoreVO(daboluoJiesuanScore.getWeidao(), chupaiSolution.getWeidao());
		daboluoJiesuanScore.getPlayerDaoMap().values().forEach((playerDao) -> {
			playerDaoList.add(new PlayerJiesuanScoreVO(playerDao));
		});
		daqiangPlayerSet = new HashSet<>(daboluoJiesuanScore.getDaqiangPlayerSet());
		daqiang = daboluoJiesuanScore.isDaqiang();
		dqbs = daboluoJiesuanScore.getDqbs();
		quanleida = daboluoJiesuanScore.isQuanleida();
		qldbs = daboluoJiesuanScore.getQldbs();
		tiezhi = daboluoJiesuanScore.isTiezhi();
		tzbs = daboluoJiesuanScore.getTzbs();
		tonghuashun = daboluoJiesuanScore.isTonghuashun();
		thsbs = daboluoJiesuanScore.getThsbs();
		wumei = daboluoJiesuanScore.isWumei();
		wmbs = daboluoJiesuanScore.getWmbs();
		yitiaolong = daboluoJiesuanScore.isYitiaolong();
		score = daboluoJiesuanScore.getScore();
		value = daboluoJiesuanScore.getValue();
	}

	public List<PlayerJiesuanScoreVO> getPlayerDaoList() {
		return playerDaoList;
	}

	public void setPlayerDaoList(List<PlayerJiesuanScoreVO> playerDaoList) {
		this.playerDaoList = playerDaoList;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public DaboluoDaoScoreVO getToudao() {
		return toudao;
	}

	public void setToudao(DaboluoDaoScoreVO toudao) {
		this.toudao = toudao;
	}

	public DaboluoDaoScoreVO getZhongdao() {
		return zhongdao;
	}

	public void setZhongdao(DaboluoDaoScoreVO zhongdao) {
		this.zhongdao = zhongdao;
	}

	public DaboluoDaoScoreVO getWeidao() {
		return weidao;
	}

	public void setWeidao(DaboluoDaoScoreVO weidao) {
		this.weidao = weidao;
	}

	public Set<String> getDaqiangPlayerSet() {
		return daqiangPlayerSet;
	}

	public void setDaqiangPlayerSet(Set<String> daqiangPlayerSet) {
		this.daqiangPlayerSet = daqiangPlayerSet;
	}

	public boolean isDaqiang() {
		return daqiang;
	}

	public void setDaqiang(boolean daqiang) {
		this.daqiang = daqiang;
	}

	public int getDqbs() {
		return dqbs;
	}

	public void setDqbs(int dqbs) {
		this.dqbs = dqbs;
	}

	public boolean isQuanleida() {
		return quanleida;
	}

	public void setQuanleida(boolean quanleida) {
		this.quanleida = quanleida;
	}

	public int getQldbs() {
		return qldbs;
	}

	public void setQldbs(int qldbs) {
		this.qldbs = qldbs;
	}

	public boolean isTiezhi() {
		return tiezhi;
	}

	public void setTiezhi(boolean tiezhi) {
		this.tiezhi = tiezhi;
	}

	public int getTzbs() {
		return tzbs;
	}

	public void setTzbs(int tzbs) {
		this.tzbs = tzbs;
	}

	public boolean isTonghuashun() {
		return tonghuashun;
	}

	public void setTonghuashun(boolean tonghuashun) {
		this.tonghuashun = tonghuashun;
	}

	public int getThsbs() {
		return thsbs;
	}

	public void setThsbs(int thsbs) {
		this.thsbs = thsbs;
	}

	public boolean isWumei() {
		return wumei;
	}

	public void setWumei(boolean wumei) {
		this.wumei = wumei;
	}

	public int getWmbs() {
		return wmbs;
	}

	public void setWmbs(int wmbs) {
		this.wmbs = wmbs;
	}

	public boolean isYitiaolong() {
		return yitiaolong;
	}

	public void setYitiaolong(boolean yitiaolong) {
		this.yitiaolong = yitiaolong;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
