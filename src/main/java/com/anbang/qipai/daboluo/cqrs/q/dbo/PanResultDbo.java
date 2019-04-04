package com.anbang.qipai.daboluo.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanPlayerResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanResult;
import com.dml.shisanshui.pan.PanActionFrame;

public class PanResultDbo {
	private String id;
	private String gameId;
	private int panNo;
	private List<DaboluoPanPlayerResultDbo> playerResultList;
	private long finishTime;
	private PanActionFrame panActionFrame;

	public PanResultDbo() {

	}

	public PanResultDbo(String gameId, DaboluoPanResult panResult) {
		this.gameId = gameId;
		panNo = panResult.getPan().getNo();
		playerResultList = new ArrayList<>();
		for (DaboluoPanPlayerResult playerResult : panResult.getPanPlayerResultList()) {
			DaboluoPanPlayerResultDbo dbo = new DaboluoPanPlayerResultDbo();
			dbo.setPlayerId(playerResult.getPlayerId());
			dbo.setPlayerResult(playerResult);
			dbo.setPlayer(panResult.findPlayer(playerResult.getPlayerId()));
			playerResultList.add(dbo);
		}
		finishTime = panResult.getPanFinishTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public List<DaboluoPanPlayerResultDbo> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<DaboluoPanPlayerResultDbo> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
