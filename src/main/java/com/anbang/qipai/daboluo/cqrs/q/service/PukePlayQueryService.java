package com.anbang.qipai.daboluo.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anbang.qipai.daboluo.cqrs.c.domain.PlayerActionFrameFilter;
import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.PukeActionResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.ReadyForGameResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.ReadyToNextPanResult;
import com.anbang.qipai.daboluo.cqrs.q.dao.GameLatestPanActionFrameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.PanActionFrameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.PukeGameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedJuResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedPanActionFrameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedPanResultDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dao.memcached.MemcachedPukeGameDboDao;
import com.anbang.qipai.daboluo.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanActionFrameDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.daboluo.plan.bean.PlayerInfo;
import com.anbang.qipai.daboluo.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;
import com.dml.shisanshui.pan.PanActionFrame;

@Service
public class PukePlayQueryService {

	@Autowired
	private PukeGameDboDao pukeGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private PanActionFrameDboDao panActionFrameDboDao;

	@Autowired
	private MemcachedPukeGameDboDao memcachedPukeGameDboDao;

	@Autowired
	private MemcachedPanResultDboDao memcachedPanResultDboDao;

	@Autowired
	private MemcachedJuResultDboDao memcachedJuResultDboDao;

	@Autowired
	private MemcachedPanActionFrameDboDao memcachedPanActionFrameDboDao;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	@Autowired
	private GameLatestPanActionFrameDboDao gameLatestPanActionFrameDboDao;

	private PlayerActionFrameFilter pf = new PlayerActionFrameFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		PukeGameDbo pukeGameDbo = memcachedPukeGameDboDao.findById(gameId);
		if (!(pukeGameDbo.getState().name().equals(Playing.name)
				|| pukeGameDbo.getState().name().equals(VotingWhenPlaying.name)
				|| pukeGameDbo.getState().name().equals(VoteNotPassWhenPlaying.name))) {
			throw new Exception("game not playing");
		}
		GameLatestPanActionFrameDbo frame = gameLatestPanActionFrameDboDao.findById(gameId);
		PanActionFrame panActionFrame = pf.filter(frame, playerId);
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Exception {
		PukeGameValueObject pukeGame = readyForGameResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		if (readyForGameResult.getFirstActionFrame() != null) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			gameLatestPanActionFrameDboDao.save(pukeGame.getId(), panActionFrame);
			// 记录一条Frame，回放的时候要做
			String gameId = pukeGame.getId();
			int panNo = panActionFrame.getPanAfterAction().getNo();
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			try {
				memcachedPanActionFrameDboDao.save(panActionFrameDbo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void action(PukeActionResult pukeActionResult) throws Exception {
		PukeGameValueObject pukeGame = pukeActionResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		String gameId = pukeGameDbo.getId();
		PanActionFrame panActionFrame = pukeActionResult.getPanActionFrame();
		gameLatestPanActionFrameDboDao.save(gameId, panActionFrame);
		// 记录一条Frame，回放的时候要做
		int panNo = panActionFrame.getPanAfterAction().getNo();
		int actionNo = panActionFrame.getNo();
		PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
		panActionFrameDbo.setPanActionFrame(panActionFrame);
		try {
			memcachedPanActionFrameDboDao.save(panActionFrameDbo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 盘出结果的话要记录结果
		DaboluoPanResult daboluoPanResult = pukeActionResult.getPanResult();
		if (daboluoPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, daboluoPanResult);
			panResultDbo.setPanActionFrame(panActionFrame);
			memcachedPanResultDboDao.save(panResultDbo);
			executorService.submit(() -> {
				panResultDboDao.save(panResultDbo);
				try {
					List<PanActionFrameDbo> frameList = memcachedPanActionFrameDboDao.findByGameIdAndActionNo(gameId,
							panNo, actionNo);
					panActionFrameDboDao.save(frameList);
					// memcachedPanActionFrameDboDao.removePanActionFrameDbo(gameId, panNo,
					// actionNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if (pukeActionResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, pukeActionResult.getJuResult());
				memcachedJuResultDboDao.save(juResultDbo);
				executorService.submit(() -> {
					pukeGameDboDao.save(pukeGameDbo);
					juResultDboDao.save(juResultDbo);
				});
			}
		}
	}

	public void readyToNextPan(ReadyToNextPanResult readyToNextPanResult) throws Exception {
		PukeGameValueObject pukeGame = readyToNextPanResult.getPukeGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		pukeGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		PukeGameDbo pukeGameDbo = new PukeGameDbo(pukeGame, playerInfoMap);
		memcachedPukeGameDboDao.save(pukeGameDbo);

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			PanActionFrame panActionFrame = readyToNextPanResult.getFirstActionFrame();
			gameLatestPanActionFrameDboDao.save(pukeGame.getId(), panActionFrame);
			// 记录一条Frame，回放的时候要做
			String gameId = pukeGame.getId();
			int panNo = panActionFrame.getPanAfterAction().getNo();
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			try {
				memcachedPanActionFrameDboDao.save(panActionFrameDbo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) throws Exception {
		return memcachedPanResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public JuResultDbo findJuResultDbo(String gameId) throws Exception {
		return memcachedJuResultDboDao.findByGameId(gameId);
	}

	public List<PanActionFrameDbo> findPanActionFrameDboForBackPlay(String gameId, int panNo) {
		return panActionFrameDboDao.findByGameIdAndPanNo(gameId, panNo);
	}
}
