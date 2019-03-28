package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.shisanshui.gameprocess.AllPlayerChupaiPanFinishiDeterminer;
import com.dml.shisanshui.gameprocess.FixedPanNumbersJuFinishiDeterminer;
import com.dml.shisanshui.ju.Ju;
import com.dml.shisanshui.pai.wanfa.BianXingWanFa;
import com.dml.shisanshui.pan.PanActionFrame;
import com.dml.shisanshui.preparedapai.avaliablepai.OneAvaliablePaiFiller;
import com.dml.shisanshui.preparedapai.fapai.EveryPlayerShisanzhangFapaiStrategy;
import com.dml.shisanshui.preparedapai.lluanpai.RandomLuanPaiStrategy;
import com.dml.shisanshui.preparedapai.zuowei.JoinGameZuoweiDeterminer;

public class PukeGame extends FixedPlayersMultipanAndVotetofinishGame {
	private int panshu;
	private int renshu;
	private boolean dqef;// 打枪二番
	private boolean dqsf;// 打枪三番
	private BianXingWanFa bx;// 大小王百变
	private boolean bihuase;// 比花色
	private boolean zidongzupai;// 自动组牌
	private boolean yitiaolong;// 一条龙
	private Ju ju;
	private Map<String, Integer> playerTotalScoreMap = new HashMap<>();

	public PanActionFrame createJuAndStartFirstPan(long startTime) throws Exception {
		ju = new Ju();
		ju.setCurrentPanFinishiDeterminer(new AllPlayerChupaiPanFinishiDeterminer());
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer());

		JoinGameZuoweiDeterminer joinGameZuoweiDeterminer = new JoinGameZuoweiDeterminer();
		ju.setZuoweiDeterminer(joinGameZuoweiDeterminer);
		ju.setAvaliablePaiFiller(new OneAvaliablePaiFiller());
		ju.setLuanpaiStrategyForFirstPan(new RandomLuanPaiStrategy(startTime));
		ju.setLuanpaiStrategyForNextPan(new RandomLuanPaiStrategy(startTime + 1));
		ju.setFapaiStrategyForFirstPan(new EveryPlayerShisanzhangFapaiStrategy());
		ju.setFapaiStrategyForNextPan(new EveryPlayerShisanzhangFapaiStrategy());

		DaboluoChupaiPaixingSolutionCalculator daboluoChupaiPaixingSolutionCalculator = new DaboluoChupaiPaixingSolutionCalculator();
		daboluoChupaiPaixingSolutionCalculator.setBx(bx);
		DaboluoDunComparator daboluoDunComparator = new DaboluoDunComparator();
		daboluoDunComparator.setBihuase(bihuase);
		daboluoChupaiPaixingSolutionCalculator.setDunComparator(daboluoDunComparator);
		ju.setChupaiPaixingSolutionCalculator(daboluoChupaiPaixingSolutionCalculator);
		ju.setChupaiPaixingSolutionFilter(new DaboluoChupaiPaixingSolutionFilter());

		ju.setCurrentPanResultBuilder(new DaboluoCurrentPanResultBuilder());
		ju.setJuResultBuilder(new DaboluoJuResultBuilder());
		return ju.startFirstPan(allPlayerIds(), startTime);
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		return ju.getJuResult() != null;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		return ju.getCurrentPan() == null;
	}

	@Override
	protected void startNextPan() throws Exception {
		ju.startNextPan();
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToExtendedVotingState() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateToVoteNotPassStateFromExtendedVoting() throws Exception {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public PukeGameValueObject toValueObject() {
		return new PukeGameValueObject(this);
	}

	@Override
	public void start(long currentTime) throws Exception {
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	public void finish() throws Exception {
		if (ju != null) {
			ju.finish();
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

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

	public Map<String, Integer> getPlayerTotalScoreMap() {
		return playerTotalScoreMap;
	}

	public void setPlayerTotalScoreMap(Map<String, Integer> playerTotalScoreMap) {
		this.playerTotalScoreMap = playerTotalScoreMap;
	}

}
