package com.anbang.qipai.daboluo.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuResult;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;

public class PukeHistoricalJuResult {
	private String gameId;
	private String dayingjiaId;
	private String datuhaoId;
	private int panshu;
	private int lastPanNo;
	private List<DaboluoJuPlayerResultMO> playerResultList;

	private long finishTime;

	public PukeHistoricalJuResult() {

	}

	public PukeHistoricalJuResult(JuResultDbo juResultDbo, PukeGameDbo pukeGameDbo) {
		gameId = juResultDbo.getGameId();
		DaboluoJuResult juResult = juResultDbo.getJuResult();
		dayingjiaId = juResult.getDayingjiaId();
		datuhaoId = juResult.getDatuhaoId();
		if (juResultDbo.getLastPanResult() != null) {

		}
		finishTime = juResultDbo.getFinishTime();
		this.panshu = pukeGameDbo.getPanshu();
		lastPanNo = juResult.getFinishedPanCount();
		playerResultList = new ArrayList<>();
		if (juResult.getPlayerResultList() != null) {
			juResult.getPlayerResultList()
					.forEach((juPlayerResult) -> playerResultList.add(new DaboluoJuPlayerResultMO(juPlayerResult,
							pukeGameDbo.findPlayer(juPlayerResult.getPlayerId()))));
		} else {
			pukeGameDbo.getPlayers().forEach(
					(pukeGamePlayerDbo) -> playerResultList.add(new DaboluoJuPlayerResultMO(pukeGamePlayerDbo)));
		}
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getLastPanNo() {
		return lastPanNo;
	}

	public void setLastPanNo(int lastPanNo) {
		this.lastPanNo = lastPanNo;
	}

	public List<DaboluoJuPlayerResultMO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<DaboluoJuPlayerResultMO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
