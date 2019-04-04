package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.daboluo.cqrs.q.dbo.DaboluoPanPlayerResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;

public class PanResultVO {

	private List<DaboluoPanPlayerResultVO> playerResultList;

	private int panNo;

	private long finishTime;

	private PanActionFrameVO lastPanActionFrame;

	public PanResultVO() {

	}

	public PanResultVO(PanResultDbo panResultDbo, PukeGameDbo pukeGameDbo) {
		List<DaboluoPanPlayerResultDbo> list = panResultDbo.getPlayerResultList();
		playerResultList = new ArrayList<>();
		if (list != null) {
			list.forEach((panPlayerResult) -> {
				playerResultList.add(new DaboluoPanPlayerResultVO(pukeGameDbo.findPlayer(panPlayerResult.getPlayerId()),
						panPlayerResult));
			});
		}
		panNo = panResultDbo.getPanNo();
		finishTime = panResultDbo.getFinishTime();
		lastPanActionFrame = new PanActionFrameVO(panResultDbo.getPanActionFrame());
	}

	public List<DaboluoPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<DaboluoPanPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
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

}
