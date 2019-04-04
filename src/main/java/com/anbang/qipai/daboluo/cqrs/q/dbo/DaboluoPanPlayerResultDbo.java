package com.anbang.qipai.daboluo.cqrs.q.dbo;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanPlayerResult;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;

public class DaboluoPanPlayerResultDbo {

	private String playerId;
	private DaboluoPanPlayerResult playerResult;
	private ShisanshuiPlayerValueObject player;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public DaboluoPanPlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(DaboluoPanPlayerResult playerResult) {
		this.playerResult = playerResult;
	}

	public ShisanshuiPlayerValueObject getPlayer() {
		return player;
	}

	public void setPlayer(ShisanshuiPlayerValueObject player) {
		this.player = player;
	}

}
