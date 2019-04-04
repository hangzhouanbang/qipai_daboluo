package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;
import com.dml.shisanshui.ju.JuResult;
import com.dml.shisanshui.position.Position;

public class PukeGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {
	private int panshu;
	private int renshu;
	private boolean dqef;// 打枪二番
	private boolean dqsf;// 打枪三番
	private BianXingWanFa bx;// 大小王百变
	private boolean bihuase;// 比花色
	private boolean zidongzupai;// 自动组牌
	private boolean yitiaolong;// 一条龙
	private Map<String, Integer> playerTotalScoreMap = new HashMap<>();
	private Map<String, Position> playerIdPositionMap = new HashMap<>();
	private JuResult juResult;

	public PukeGameValueObject(PukeGame game) {
		super(game);
		panshu = game.getPanshu();
		renshu = game.getRenshu();
		dqef = game.isDqef();
		dqsf = game.isDqsf();
		bx = game.getBx();
		bihuase = game.isBihuase();
		zidongzupai = game.isZidongzupai();
		yitiaolong = game.isYitiaolong();
		playerTotalScoreMap.putAll(game.getPlayerTotalScoreMap());
		playerIdPositionMap.putAll(game.getPlayerIdPositionMap());
		if (game.getJu() != null) {
			juResult = game.getJu().getJuResult();
		}
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

	public Map<String, Integer> getPlayerTotalScoreMap() {
		return playerTotalScoreMap;
	}

	public void setPlayerTotalScoreMap(Map<String, Integer> playerTotalScoreMap) {
		this.playerTotalScoreMap = playerTotalScoreMap;
	}

	public JuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(JuResult juResult) {
		this.juResult = juResult;
	}

	public Map<String, Position> getPlayerIdPositionMap() {
		return playerIdPositionMap;
	}

	public void setPlayerIdPositionMap(Map<String, Position> playerIdPositionMap) {
		this.playerIdPositionMap = playerIdPositionMap;
	}

}
