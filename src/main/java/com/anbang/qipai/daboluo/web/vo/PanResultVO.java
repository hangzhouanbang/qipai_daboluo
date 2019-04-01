package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.daboluo.cqrs.q.dbo.DaboluoPanPlayerResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;

public class PanResultVO {

	private List<DaboluoPanPlayerResultVO> playerResultList;

	private boolean dizhuying;

	private String yingjiaPlayerId;// 赢家id

	private int panNo;

	private long finishTime;

	private PanActionFrameVO lastPanActionFrame;

	public PanResultVO() {

	}

	public PanResultVO(PanResultDbo panResultDbo, PukeGameDbo pukeGameDbo) {
		List<DaboluoPanPlayerResultDbo> list = panResultDbo.getPlayerResultList();
		playerResultList = new ArrayList<>();
		panNo = panResultDbo.getPanNo();
		finishTime = panResultDbo.getFinishTime();
	}

	public List<DaboluoPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<DaboluoPanPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public boolean isDizhuying() {
		return dizhuying;
	}

	public void setDizhuying(boolean dizhuying) {
		this.dizhuying = dizhuying;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public PanActionFrameVO getLastPanActionFrame() {
		return lastPanActionFrame;
	}

	public void setLastPanActionFrame(PanActionFrameVO lastPanActionFrame) {
		this.lastPanActionFrame = lastPanActionFrame;
	}

	public String getYingjiaPlayerId() {
		return yingjiaPlayerId;
	}

	public void setYingjiaPlayerId(String yingjiaPlayerId) {
		this.yingjiaPlayerId = yingjiaPlayerId;
	}

}
