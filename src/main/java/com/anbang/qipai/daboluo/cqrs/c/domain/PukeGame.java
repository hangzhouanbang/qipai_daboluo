package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanPlayerResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.PukeActionResult;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;
import com.dml.shisanshui.gameprocess.AllPlayerChupaiPanFinishiDeterminer;
import com.dml.shisanshui.gameprocess.FixedPanNumbersJuFinishiDeterminer;
import com.dml.shisanshui.ju.Ju;
import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.comparator.TypeCodeDaoComparator;
import com.dml.shisanshui.pan.PanActionFrame;
import com.dml.shisanshui.position.Position;
import com.dml.shisanshui.preparedapai.fapai.EveryPlayerShisanzhangFapaiStrategy;
import com.dml.shisanshui.preparedapai.lipai.DianshuOrHuaseShoupaiSortStrategy;
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
	private Map<String, Position> playerIdPositionMap = new HashMap<>();

	public void updatePlayerPosition(String playerId) {
		List<Position> positionList = new ArrayList<>();
		if (renshu == 2) {
			positionList.add(Position.dong);
			positionList.add(Position.xi);
		} else if (renshu == 3) {
			positionList.add(Position.dong);
			positionList.add(Position.nan);
			positionList.add(Position.xi);
		} else {
			positionList.add(Position.dong);
			positionList.add(Position.nan);
			positionList.add(Position.xi);
			positionList.add(Position.bei);
		}
		Map<String, Position> positionMap = new HashMap<>();
		for (String pid : playerIdPositionMap.keySet()) {
			if (idPlayerMap.containsKey(pid)) {
				positionMap.put(pid, playerIdPositionMap.get(pid));
				positionList.remove(playerIdPositionMap.get(pid));
			}
		}
		playerIdPositionMap = positionMap;
		if (!positionList.isEmpty()) {
			playerIdPositionMap.put(playerId, positionList.remove(0));
		}
	}

	public PanActionFrame createJuAndStartFirstPan(long startTime) throws Exception {
		ju = new Ju();
		ju.setCurrentPanFinishiDeterminer(new AllPlayerChupaiPanFinishiDeterminer());
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer());

		JoinGameZuoweiDeterminer joinGameZuoweiDeterminer = new JoinGameZuoweiDeterminer();
		joinGameZuoweiDeterminer.setPlayerIdPositionMap(playerIdPositionMap);
		ju.setZuoweiDeterminer(joinGameZuoweiDeterminer);
		DaboluoAvaliablePaiFiller daboluoAvaliablePaiFiller = new DaboluoAvaliablePaiFiller();
		daboluoAvaliablePaiFiller.setBx(bx);
		ju.setAvaliablePaiFiller(daboluoAvaliablePaiFiller);
		ju.setLuanpaiStrategyForFirstPan(new RandomLuanPaiStrategy(startTime));
		ju.setLuanpaiStrategyForNextPan(new RandomLuanPaiStrategy(startTime + 1));
		ju.setFapaiStrategyForFirstPan(new EveryPlayerShisanzhangFapaiStrategy());
		ju.setFapaiStrategyForNextPan(new EveryPlayerShisanzhangFapaiStrategy());
		ju.setShoupaiSortStrategy(new DianshuOrHuaseShoupaiSortStrategy());
		TypeCodeDaoComparator typeCodeDaoComparator = new TypeCodeDaoComparator();
		ju.setDaoComparator(typeCodeDaoComparator);

		DaboluoChupaiDaoCalculator daboluoChupaiDaoCalculator = new DaboluoChupaiDaoCalculator();
		daboluoChupaiDaoCalculator.setBx(bx);
		daboluoChupaiDaoCalculator.setBihuase(bihuase);
		ju.setChupaiDaoCalculator(daboluoChupaiDaoCalculator);
		DaboluoChupaiPaixingSolutionFilter daboluoChupaiPaixingSolutionFilter = new DaboluoChupaiPaixingSolutionFilter();
		daboluoChupaiPaixingSolutionFilter.setDaoComparator(typeCodeDaoComparator);
		ju.setChupaiPaixingSolutionFilter(daboluoChupaiPaixingSolutionFilter);

		DaboluoCurrentPanResultBuilder daboluoCurrentPanResultBuilder = new DaboluoCurrentPanResultBuilder();
		daboluoCurrentPanResultBuilder.setDaoComparator(typeCodeDaoComparator);
		daboluoCurrentPanResultBuilder.setRenshu(renshu);
		daboluoCurrentPanResultBuilder.setDqef(dqef);
		daboluoCurrentPanResultBuilder.setDqsf(dqsf);
		daboluoCurrentPanResultBuilder.setBx(bx);
		daboluoCurrentPanResultBuilder.setYitiaolong(yitiaolong);
		ju.setCurrentPanResultBuilder(daboluoCurrentPanResultBuilder);
		ju.setJuResultBuilder(new DaboluoJuResultBuilder());
		return ju.startFirstPan(allPlayerIds(), startTime);
	}

	public Dao findDaoByPlayerIdAndIndex(String playerId, String index) throws Exception {
		return ju.findDaoByPlayerIdAndIndex(playerId, index);
	}

	public PukeActionResult chupai(String playerId, String toudaoIndex, String zhongdaoIndex, String weidaoIndex,
			long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.chupai(playerId, toudaoIndex, zhongdaoIndex, weidaoIndex, actionTime);
		PukeActionResult result = new PukeActionResult();
		result.setPanActionFrame(panActionFrame);
		if (state.name().equals(VoteNotPassWhenPlaying.name)) {
			state = new Playing();
		}
		checkAndFinishPan();
		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			DaboluoPanResult panResult = (DaboluoPanResult) ju.findLatestFinishedPanResult();
			for (DaboluoPanPlayerResult doudizhuPanPlayerResult : panResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(doudizhuPanPlayerResult.getPlayerId(), doudizhuPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((DaboluoJuResult) ju.getJuResult());
			}
		}
		result.setPukeGame(new PukeGameValueObject(this));
		return result;
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

	public Map<String, Position> getPlayerIdPositionMap() {
		return playerIdPositionMap;
	}

	public void setPlayerIdPositionMap(Map<String, Position> playerIdPositionMap) {
		this.playerIdPositionMap = playerIdPositionMap;
	}

}
