package com.anbang.qipai.daboluo.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerValueObject;
import com.dml.mpgame.game.GameState;
import com.dml.shisanshui.position.Position;

public class PukeGameDbo {
	private String id;
	private int panshu;
	private int renshu;
	private boolean dqef;// 打枪二番
	private boolean dqsf;// 打枪三番
	private BianXingWanFa bx;// 大小王百变
	private boolean bihuase;// 比花色
	private boolean zidongzupai;// 自动组牌
	private boolean yitiaolong;// 一条龙
	private GameState state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private int panNo;
	private List<PukeGamePlayerDbo> players;

	public PukeGameDbo() {

	}

	public PukeGameDbo(PukeGameValueObject pukeGame, Map<String, PlayerInfo> playerInfoMap) {
		id = pukeGame.getId();
		panshu = pukeGame.getPanshu();
		renshu = pukeGame.getRenshu();
		dqef = pukeGame.isDqef();
		dqsf = pukeGame.isDqsf();
		bx = pukeGame.getBx();
		bihuase = pukeGame.isBihuase();
		zidongzupai = pukeGame.isZidongzupai();
		yitiaolong = pukeGame.isYitiaolong();
		state = pukeGame.getState();
		panNo = pukeGame.getPanNo();
		players = new ArrayList<>();
		Map<String, Integer> playerTotalScoreMap = pukeGame.getPlayerTotalScoreMap();
		Map<String, Position> playerIdPositionMap = pukeGame.getPlayerIdPositionMap();
		for (GamePlayerValueObject playerValueObject : pukeGame.getPlayers()) {
			String playerId = playerValueObject.getId();
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			PukeGamePlayerDbo playerDbo = new PukeGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setGender(playerInfo.getGender());
			playerDbo.setOnlineState(playerValueObject.getOnlineState());
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerValueObject.getState());
			if (playerTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playerTotalScoreMap.get(playerId));
			}
			if (playerIdPositionMap.get(playerId) != null) {
				playerDbo.setPosition(playerIdPositionMap.get(playerId));
			}
			players.add(playerDbo);
		}
	}

	public PukeGamePlayerDbo findPlayer(String playerId) {
		for (PukeGamePlayerDbo player : players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isDqef() {
		return dqef;
	}

	public void setDqef(boolean dqef) {
		this.dqef = dqef;
	}

	public boolean isDqsf() {
		return dqsf;
	}

	public void setDqsf(boolean dqsf) {
		this.dqsf = dqsf;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

	public boolean isBihuase() {
		return bihuase;
	}

	public void setBihuase(boolean bihuase) {
		this.bihuase = bihuase;
	}

	public boolean isZidongzupai() {
		return zidongzupai;
	}

	public void setZidongzupai(boolean zidongzupai) {
		this.zidongzupai = zidongzupai;
	}

	public boolean isYitiaolong() {
		return yitiaolong;
	}

	public void setYitiaolong(boolean yitiaolong) {
		this.yitiaolong = yitiaolong;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public List<PukeGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<PukeGamePlayerDbo> players) {
		this.players = players;
	}

}
