package com.anbang.qipai.daboluo.cqrs.q.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuResult;
import com.anbang.qipai.daboluo.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.PukeGameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.WatchRecordDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedGameFinishVoteDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedJuResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedPukeGameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.daboluo.plan.bean.PlayerInfo;
import com.anbang.qipai.daboluo.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;

@Service
public class PukeGameQueryService {

	@Autowired
	private PukeGameDboDao pukeGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private MemcachedPukeGameDboDao memcachedPukeGameDboDao;

	@Autowired
	private MemcachedGameFinishVoteDboDao memcachedGameFinishVoteDboDao;

	@Autowired
	private MemcachedJuResultDboDao memcachedJuResultDboDao;

	@Autowired
	private WatchRecordDao watchRecordDao;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	public PukeGameDbo findPukeGameDboById(String gameId) throws Exception {
		return memcachedPukeGameDboDao.findById(gameId);
	}

	public void newPukeGame(PukeGameValueObject pukeGame) throws Exception {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);
	}

	public void joinGame(PukeGameValueObject pukeGame) throws Exception {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);
	}

	public void leaveGame(PukeGameValueObject pukeGame) throws Exception {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGame.getVote();
		if (gameFinishVoteValueObject != null) {
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
			gameFinishVoteDbo.setGameId(pukeGame.getId());
			memcachedGameFinishVoteDboDao.save(gameFinishVoteDbo);
		}
		if (pukeGame.getJuResult() != null) {
			DaboluoJuResult daboluoJuResult = (DaboluoJuResult) pukeGame.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(pukeGame.getId(), null, daboluoJuResult);
			executorService.submit(() -> {
				pukeGameDboDao.save(pukeGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao.findByGameId(pukeGame.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void backToGame(String playerId, PukeGameValueObject pukeGameValueObject) throws Exception {
		memcachedPukeGameDboDao.updatePlayerOnlineState(pukeGameValueObject.getId(), playerId,
				pukeGameValueObject.findPlayerOnlineState(playerId));
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			memcachedGameFinishVoteDboDao.update(pukeGameValueObject.getId(), gameFinishVoteValueObject);
		}
	}

	public void finishGameImmediately(PukeGameValueObject pukeGameValueObject) throws Exception {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGameValueObject, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		if (pukeGameValueObject.getJuResult() != null) {
			DaboluoJuResult doudizhuJuResult = (DaboluoJuResult) pukeGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(pukeGameValueObject.getId(), null, doudizhuJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				pukeGameDboDao.save(pukeGameDbo);
				juResultDboDao.save(juResultDbo);
			});
		}
	}

	public void finish(PukeGameValueObject pukeGameValueObject) throws Exception {
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
			gameFinishVoteDbo.setGameId(pukeGameValueObject.getId());
			memcachedGameFinishVoteDboDao.save(gameFinishVoteDbo);
		}

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGameValueObject, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		if (pukeGameValueObject.getJuResult() != null) {
			DaboluoJuResult doudizhuJuResult = (DaboluoJuResult) pukeGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(pukeGameValueObject.getId(), null, doudizhuJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				pukeGameDboDao.save(pukeGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao
							.findByGameId(pukeGameValueObject.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void voteToFinish(PukeGameValueObject pukeGameValueObject) throws Exception {
		GameFinishVoteValueObject gameFinishVoteValueObject = pukeGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			memcachedGameFinishVoteDboDao.update(pukeGameValueObject.getId(), gameFinishVoteValueObject);
		}

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGameValueObject, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		if (pukeGameValueObject.getJuResult() != null) {
			DaboluoJuResult doudizhuJuResult = (DaboluoJuResult) pukeGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(pukeGameValueObject.getId(), null, doudizhuJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				pukeGameDboDao.save(pukeGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao
							.findByGameId(pukeGameValueObject.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) throws Exception {
		return memcachedGameFinishVoteDboDao.findByGameId(gameId);
	}

	public WatchRecord saveWatchRecord(String gameId, Watcher watcher) {
		WatchRecord watchRecord = watchRecordDao.findByGameId(gameId);
		if (watchRecord == null) {
			WatchRecord record = new WatchRecord();
			List<Watcher> watchers = new ArrayList<>();
			watchers.add(watcher);

			record.setGameId(gameId);
			record.setWatchers(watchers);
			watchRecordDao.save(record);
			return record;
		}

		for (Watcher list : watchRecord.getWatchers()) {
			if (list.getId().equals(watcher.getId())) {
				list.setState(watcher.getState());
				watchRecordDao.save(watchRecord);
				return watchRecord;
			}
		}

		watchRecord.getWatchers().add(watcher);
		watchRecordDao.save(watchRecord);
		return watchRecord;
	}

	/**
	 * 查询观战中的玩家
	 */
	public boolean findByPlayerId(String gameId, String playerId) {
		if (watchRecordDao.findByPlayerId(gameId, playerId, "join") != null) {
			return true;
		}
		return false;
	}

}
