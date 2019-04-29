package com.anbang.qipai.daboluo.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.anbang.qipai.daboluo.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.ReadyForGameResult;
import com.anbang.qipai.daboluo.cqrs.c.service.GameCmdService;
import com.anbang.qipai.daboluo.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.daboluo.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanActionFrameDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;
import com.anbang.qipai.daboluo.cqrs.q.service.PukeGameQueryService;
import com.anbang.qipai.daboluo.cqrs.q.service.PukePlayQueryService;
import com.anbang.qipai.daboluo.msg.msjobj.PukeHistoricalJuResult;
import com.anbang.qipai.daboluo.msg.service.DaboluoGameMsgService;
import com.anbang.qipai.daboluo.msg.service.DaboluoResultMsgService;
import com.anbang.qipai.daboluo.msg.service.MemberGoldsMsgService;
import com.anbang.qipai.daboluo.msg.service.WatchRecordMsgService;
import com.anbang.qipai.daboluo.msg.service.WiseCrackMsgServcie;
import com.anbang.qipai.daboluo.plan.bean.MemberGoldBalance;
import com.anbang.qipai.daboluo.plan.bean.PlayerInfo;
import com.anbang.qipai.daboluo.plan.service.MemberGoldBalanceService;
import com.anbang.qipai.daboluo.plan.service.PlayerInfoService;
import com.anbang.qipai.daboluo.utils.CommonVoUtil;
import com.anbang.qipai.daboluo.web.vo.CommonVO;
import com.anbang.qipai.daboluo.web.vo.GameFinishVoteVO;
import com.anbang.qipai.daboluo.web.vo.GameVO;
import com.anbang.qipai.daboluo.web.vo.PanActionFrameVO;
import com.anbang.qipai.daboluo.web.vo.PanResultVO;
import com.anbang.qipai.daboluo.websocket.GamePlayWsNotifier;
import com.anbang.qipai.daboluo.websocket.QueryScope;
import com.anbang.qipai.daboluo.websocket.WatchQueryScope;
import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.CrowdLimitsException;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.GameNotFoundException;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;

@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private PlayerAuthService playerAuthService;

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private PukeGameQueryService pukeGameQueryService;

	@Autowired
	private PukePlayQueryService pukePlayQueryService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private DaboluoGameMsgService gameMsgService;

	@Autowired
	private DaboluoResultMsgService daboluoResultMsgService;

	@Autowired
	private WiseCrackMsgServcie wiseCrackMsgServcie;

	@Autowired
	private MemberGoldBalanceService memberGoldBalanceService;

	@Autowired
	private MemberGoldsMsgService memberGoldsMsgService;

	@Autowired
	private WatchRecordMsgService watchRecordMsgService;

	@Autowired
	private PlayerInfoService playerInfoService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 新一局游戏
	 */
	@RequestMapping(value = "/newgame")
	@ResponseBody
	public CommonVO newgame(String playerId, int panshu, int renshu, boolean dqef, boolean dqsf, BianXingWanFa bx,
			boolean bihuase, boolean zidongzupai, boolean yitiaolong) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		PukeGameValueObject pukeGameValueObject = gameCmdService.newPukeGame(newGameId, playerId, panshu, renshu, dqef,
				dqsf, bx, bihuase, zidongzupai, yitiaolong);
		try {
			pukeGameQueryService.newPukeGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 新一局游戏,游戏未开始时离开就是退出
	 */
	@RequestMapping(value = "/newgame_leave_quit")
	@ResponseBody
	public CommonVO newgame_leave_quit(String playerId, int panshu, int renshu, boolean dqef, boolean dqsf,
			BianXingWanFa bx, boolean bihuase, boolean zidongzupai, boolean yitiaolong) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		PukeGameValueObject pukeGameValueObject = gameCmdService.newPukeGameLeaveAndQuit(newGameId, playerId, panshu,
				renshu, dqef, dqsf, bx, bihuase, zidongzupai, yitiaolong);
		try {
			pukeGameQueryService.newPukeGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 新一局游戏,游戏未开始时退出就不在房间内
	 */
	@RequestMapping(value = "/newgame_player_quit")
	@ResponseBody
	public CommonVO newgame_player_quit(String playerId, int panshu, int renshu, boolean dqef, boolean dqsf,
			BianXingWanFa bx, boolean bihuase, boolean zidongzupai, boolean yitiaolong) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		PukeGameValueObject pukeGameValueObject = gameCmdService.newMajiangGamePlayerLeaveAndQuit(newGameId, playerId,
				panshu, renshu, dqef, dqsf, bx, bihuase, zidongzupai, yitiaolong);
		try {
			pukeGameQueryService.newPukeGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 加入游戏
	 */
	@RequestMapping(value = "/joingame")
	@ResponseBody
	public CommonVO joingame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		PukeGameValueObject pukeGameValueObject;
		try {
			pukeGameValueObject = gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		try {
			pukeGameQueryService.joinGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		// 通知其他玩家
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					wsNotifier.notifyToQuery(otherPlayerId, QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId)));
				}
			}
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 挂起（手机按黑的时候调用）
	 */
	@RequestMapping(value = "/hangup")
	@ResponseBody
	public CommonVO hangup(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PukeGameValueObject pukeGameValueObject;
		String flag = "query";
		try {
			pukeGameValueObject = gameCmdService.leaveGameByHangup(playerId);
			if (pukeGameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukeGameQueryService.leaveGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		// 断开玩家的socket
		wsNotifier.closeSessionForPlayer(playerId);
		String gameId = pukeGameValueObject.getId();
		try {
			JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);

			// 记录战绩
			if (juResultDbo != null) {
				PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
				PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
				daboluoResultMsgService.recordJuResult(juResult);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (pukeGameValueObject.getState().name().equals(FinishedByVote.name)
				|| pukeGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			flag = WatchQueryScope.watchEnd.name();
		} else {
			gameMsgService.gamePlayerLeave(pukeGameValueObject, playerId);

		}
		// 通知其他玩家
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					scopes.remove(QueryScope.panResult);
					if (pukeGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
							|| pukeGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
						scopes.remove(QueryScope.gameFinishVote);
					}
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}

		// 挂起通知观战者
		hintWatcher(gameId, flag);
		return vo;
	}

	/**
	 * 离开游戏(非退出,还会回来的)
	 */
	@RequestMapping(value = "/leavegame")
	@ResponseBody
	public CommonVO leavegame(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PukeGameValueObject pukeGameValueObject;
		String endFlag = "query";
		try {
			pukeGameValueObject = gameCmdService.leaveGame(playerId);
			if (pukeGameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukeGameQueryService.leaveGame(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		// 断开玩家的socket
		wsNotifier.closeSessionForPlayer(playerId);
		String gameId = pukeGameValueObject.getId();
		try {
			JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
			// 记录战绩
			if (juResultDbo != null) {
				PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
				PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
				daboluoResultMsgService.recordJuResult(juResult);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (pukeGameValueObject.getState().name().equals(FinishedByVote.name)
				|| pukeGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			endFlag = WatchQueryScope.watchEnd.name();
		} else if (pukeGameValueObject.getState().name().equals(Finished.name)) {
			gameMsgService.gameCanceled(gameId, playerId);
		} else {
			gameMsgService.gamePlayerLeave(pukeGameValueObject, playerId);

		}
		// 通知其他玩家
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					if (!pukeGameValueObject.getState().name().equals(Finished.name)) {
						scopes.remove(QueryScope.panResult);
					}
					if (pukeGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
							|| pukeGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
						scopes.remove(QueryScope.gameFinishVote);
					}
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}

		// 离开游戏通知观战者
		hintWatcher(gameId, endFlag);
		return vo;
	}

	/**
	 * 返回游戏
	 */
	@RequestMapping(value = "/backtogame")
	@ResponseBody
	public CommonVO backtogame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		PukeGameValueObject pukeGameValueObject;
		try {
			pukeGameValueObject = gameCmdService.backToGame(playerId, gameId);
		} catch (Exception e) {
			// 如果找不到game，看下是否是已经结束(正常结束和被投票)的game
			if (e instanceof GameNotFoundException) {
				try {
					PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
					if (pukeGameDbo != null && (pukeGameDbo.getState().name().equals(FinishedByVote.name)
							|| pukeGameDbo.getState().name().equals(Finished.name))) {
						data.put("queryScope", QueryScope.juResult);
						return vo;
					}
				} catch (Exception e1) {
					vo.setSuccess(false);
					vo.setMsg(e1.getClass().getName());
					return vo;
				}
			}
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}

		try {
			pukeGameQueryService.backToGame(playerId, pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		// 通知其他人
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					scopes.remove(QueryScope.panResult);
					if (pukeGameValueObject.getState().name().equals(VoteNotPassWhenPlaying.name)
							|| pukeGameValueObject.getState().name().equals(VoteNotPassWhenWaitingNextPan.name)) {
						scopes.remove(QueryScope.gameFinishVote);
					}
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		data.put("token", token);
		return vo;

	}

	/**
	 * 游戏的所有信息,不包含局
	 * 
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public CommonVO info(String gameId) {
		CommonVO vo = new CommonVO();
		PukeGameDbo pukeGameDbo = null;
		try {
			pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		GameVO gameVO = new GameVO(pukeGameDbo);
		Map data = new HashMap();
		data.put("game", gameVO);
		vo.setData(data);
		return vo;
	}

	/**
	 * 最开始的准备,不适用下一盘的准备
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/ready")
	@ResponseBody
	public CommonVO ready(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		ReadyForGameResult readyForGameResult;
		try {
			readyForGameResult = gameCmdService.readyForGame(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			pukePlayQueryService.readyForGame(readyForGameResult);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		} // TODO 一起点准备的时候可能有同步问题.要靠框架解决
			// 通知其他人
		for (String otherPlayerId : readyForGameResult.getPukeGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = readyForGameResult.getPukeGame()
						.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					wsNotifier.notifyToQuery(otherPlayerId,
							QueryScope.scopesForState(readyForGameResult.getPukeGame().getState(),
									readyForGameResult.getPukeGame().findPlayerState(otherPlayerId)));
				}
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (readyForGameResult.getPukeGame().getState().name().equals(Playing.name)) {
			queryScopes.add(QueryScope.panForMe);
			gameMsgService.start(readyForGameResult.getPukeGame().getId());
		}
		data.put("queryScopes", queryScopes);
		return vo;
	}

	/**
	 * 最开始的取消准备,不适用下一盘的准备
	 *
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/cancelready")
	@ResponseBody
	public CommonVO cancelReady(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		ReadyForGameResult readyForGameResult;
		try {
			readyForGameResult = gameCmdService.cancelReadyForGame(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			pukePlayQueryService.readyForGame(readyForGameResult);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		} // TODO 一起点准备的时候可能有同步问题.要靠框架解决
			// 通知其他人
		for (String otherPlayerId : readyForGameResult.getPukeGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = readyForGameResult.getPukeGame()
						.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					wsNotifier.notifyToQuery(otherPlayerId,
							QueryScope.scopesForState(readyForGameResult.getPukeGame().getState(),
									readyForGameResult.getPukeGame().findPlayerState(otherPlayerId)));
				}
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		data.put("queryScopes", queryScopes);
		return vo;
	}

	@RequestMapping(value = "/finish")
	@ResponseBody
	public CommonVO finish(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		PukeGameValueObject pukeGameValueObject;
		String endFlag = "query";
		try {
			pukeGameValueObject = gameCmdService.finish(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukeGameQueryService.finish(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String gameId = pukeGameValueObject.getId();
		try {
			JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
			// 记录战绩
			if (juResultDbo != null) {
				PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
				PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
				daboluoResultMsgService.recordJuResult(juResult);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (pukeGameValueObject.getState().name().equals(FinishedByVote.name)
				|| pukeGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			data.put("queryScope", QueryScope.gameInfo);
			endFlag = WatchQueryScope.watchEnd.name();
		} else {
			// 游戏没结束有两种可能：一种是发起了投票。还有一种是游戏没开始，解散发起人又不是房主，那就自己走人。
			if (pukeGameValueObject.allPlayerIds().contains(playerId)) {
				data.put("queryScope", QueryScope.gameFinishVote);
			} else {
				data.put("queryScope", null);
				gameMsgService.gamePlayerLeave(pukeGameValueObject, playerId);
			}
		}

		// 通知其他人来查询
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					scopes.remove(QueryScope.panResult);
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}

		// 游戏结束通知观战者
		hintWatcher(gameId, endFlag);
		return vo;
	}

	@RequestMapping(value = "/vote_to_finish")
	@ResponseBody
	public CommonVO votetofinish(String token, boolean yes) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PukeGameValueObject pukeGameValueObject;
		String endFlag = "query";
		try {
			pukeGameValueObject = gameCmdService.voteToFinish(playerId, yes);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukeGameQueryService.voteToFinish(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String gameId = pukeGameValueObject.getId();
		try {
			JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
			// 记录战绩
			if (juResultDbo != null) {
				PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
				PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
				daboluoResultMsgService.recordJuResult(juResult);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (pukeGameValueObject.getState().name().equals(FinishedByVote.name)
				|| pukeGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			endFlag = WatchQueryScope.watchEnd.name();
		}
		data.put("queryScope", QueryScope.gameFinishVote);
		// 通知其他人来查询投票情况
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					scopes.remove(QueryScope.panResult);
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}

		// 投票结束通知观战者
		hintWatcher(gameId, endFlag);
		return vo;
	}

	/**
	 * 投票倒计时结束弃权
	 */
	@RequestMapping(value = "/timeover_to_waiver")
	@ResponseBody
	public CommonVO timeoverToWaiver(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		PukeGameValueObject pukeGameValueObject;
		String endFlag = "query";
		try {
			pukeGameValueObject = gameCmdService.voteToFinishByTimeOver(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		try {
			pukeGameQueryService.voteToFinish(pukeGameValueObject);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String gameId = pukeGameValueObject.getId();
		try {
			JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
			// 记录战绩
			if (juResultDbo != null) {
				PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
				PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
				daboluoResultMsgService.recordJuResult(juResult);
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (pukeGameValueObject.getState().name().equals(FinishedByVote.name)
				|| pukeGameValueObject.getState().name().equals(Canceled.name)) {
			gameMsgService.gameFinished(gameId);
			endFlag = WatchQueryScope.watchEnd.name();
		}
		data.put("queryScope", QueryScope.gameFinishVote);
		// 通知其他人来查询投票情况
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				GamePlayerOnlineState onlineState = pukeGameValueObject.findPlayerOnlineState(otherPlayerId);
				if (onlineState.equals(GamePlayerOnlineState.online)) {
					List<QueryScope> scopes = QueryScope.scopesForState(pukeGameValueObject.getState(),
							pukeGameValueObject.findPlayerState(otherPlayerId));
					scopes.remove(QueryScope.panResult);
					wsNotifier.notifyToQuery(otherPlayerId, scopes);
				}
			}
		}

		// 投票结束通知观战者
		hintWatcher(gameId, endFlag);
		return vo;
	}

	@RequestMapping(value = "/finish_vote_info")
	@ResponseBody
	public CommonVO finishvoteinfo(String gameId) {
		CommonVO vo = new CommonVO();
		GameFinishVoteDbo gameFinishVoteDbo;
		try {
			gameFinishVoteDbo = pukeGameQueryService.findGameFinishVoteDbo(gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		Map data = new HashMap();
		data.put("vote", new GameFinishVoteVO(gameFinishVoteDbo.getVote()));
		vo.setData(data);
		return vo;

	}

	@RequestMapping(value = "/wisecrack")
	@ResponseBody
	public CommonVO wisecrack(String token, String gameId, String ordinal) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PukeGameDbo pukeGameDbo;
		try {
			pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		if (!ordinal.contains("qiaopihuafy")) {
			// 通知其他人
			for (PukeGamePlayerDbo otherPlayer : pukeGameDbo.getPlayers()) {
				if (!otherPlayer.getPlayerId().equals(playerId)) {
					wsNotifier.notifyToListenWisecrack(otherPlayer.getPlayerId(), ordinal, playerId);
				}
			}
			wiseCrackMsgServcie.wisecrack(playerId);
			vo.setSuccess(true);
			return vo;
		}
		MemberGoldBalance account = memberGoldBalanceService.findByMemberId(playerId);
		if (account.getBalanceAfter() > 10) {
			memberGoldsMsgService.withdraw(playerId, 10, "wisecrack");
			// 通知其他人
			for (PukeGamePlayerDbo otherPlayer : pukeGameDbo.getPlayers()) {
				if (!otherPlayer.getPlayerId().equals(playerId)) {
					wsNotifier.notifyToListenWisecrack(otherPlayer.getPlayerId(), ordinal, playerId);
				}
			}
			wiseCrackMsgServcie.wisecrack(playerId);
			vo.setSuccess(true);
			return vo;
		}
		vo.setSuccess(false);
		vo.setMsg("InsufficientBalanceException");
		return vo;
	}

	@RequestMapping(value = "/playback")
	@ResponseBody
	public CommonVO playback(String gameId, int panNo) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		PukeGameDbo pukeGameDbo;
		try {
			pukeGameDbo = pukeGameQueryService.findPukeGameDboByIdForBackPlay(gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		pukeGameDbo.setPanNo(panNo);
		GameVO gameVO = new GameVO(pukeGameDbo);
		data.put("game", gameVO);
		List<PanActionFrameVO> frameVOList = new ArrayList<>();
		List<PanActionFrameDbo> frameList = pukePlayQueryService.findPanActionFrameDboForBackPlay(gameId, panNo);
		data.put("framelist", frameVOList);
		PanResultDbo panResultDbo;
		try {
			panResultDbo = pukePlayQueryService.findPanResultDboForBackPlay(gameId, panNo);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		data.put("panResult", new PanResultVO(panResultDbo, pukeGameDbo));
		return vo;
	}

	@RequestMapping(value = "/speak")
	@ResponseBody
	public CommonVO speak(String token, String gameId, String wordId) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		PukeGameDbo pukeGameDbo;
		try {
			pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		List<PukeGamePlayerDbo> playerList = pukeGameDbo.getPlayers();
		for (PukeGamePlayerDbo player : playerList) {
			if (!player.getPlayerId().equals(playerId)) {
				wsNotifier.notifyToListenSpeak(player.getPlayerId(), wordId, playerId, true);
			}
		}
		vo.setSuccess(true);
		return vo;
	}

	/**
	 * 加入观战
	 */
	@RequestMapping(value = "/joinwatch")
	@ResponseBody
	public CommonVO joinWatch(String playerId, String gameId) {
		PukeGameValueObject pukeGameValueObject;
		String nickName = "";
		String headimgurl = "";

		// 加入观战
		try {
			PlayerInfo playerInfo = playerInfoService.findPlayerInfoById(playerId);
			nickName = playerInfo.getNickname();
			headimgurl = playerInfo.getHeadimgurl();
			pukeGameValueObject = gameCmdService.joinWatch(playerId, nickName, headimgurl, gameId);
		} catch (CrowdLimitsException e) {
			return CommonVoUtil.error("too many watchers");
		} catch (Exception e) {
			logger.error("joinWatch:" + JSON.toJSONString(e));
			return CommonVoUtil.error(e.getClass().toString());
		}

		// 通知游戏玩家
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			wsNotifier.notifyWatchInfo(otherPlayerId, "input", playerId, nickName, headimgurl);
		}
		// 通知其他观战者
		Map<String, Watcher> map = gameCmdService.getwatch(gameId);
		if (!CollectionUtils.isEmpty(map)) {
			for (Watcher list : map.values()) {
				if (!list.getId().equals(playerId)) {
					wsNotifier.notifyWatchInfo(list.getId(), "input", playerId, nickName, headimgurl);
				}
			}
		}

		// 返回查询token
		String token = playerAuthService.newSessionForPlayer(playerId);

		Watcher watcher = new Watcher();
		watcher.setId(playerId);
		watcher.setHeadimgurl(headimgurl);
		watcher.setNickName(nickName);
		watcher.setState("join");
		watcher.setJoinTime(System.currentTimeMillis());
		WatchRecord watchRecord = pukeGameQueryService.saveWatchRecord(gameId, watcher);
		watchRecordMsgService.joinWatch(watchRecord);

		Map data = new HashMap();
		data.put("token", token);
		return CommonVoUtil.success(data, "join watch success");
	}

	/**
	 * 离开观战
	 */
	@RequestMapping(value = "/leavewatch")
	@ResponseBody
	public CommonVO leaveWatch(String token, String gameId) {
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			return CommonVoUtil.error("invalid token");
		}
		PukeGameValueObject pukeGameValueObject;
		String nickName = "";
		String headimgurl = "";

		try {
			nickName = playerInfoService.findPlayerInfoById(playerId).getNickname();
			pukeGameValueObject = gameCmdService.leaveWatch(playerId, gameId);
		} catch (Exception e) {
			logger.error("leavewatch():" + gameId + JSON.toJSONString(e));
			return CommonVoUtil.error(e.getClass().toString());
		}

		// 通知游戏玩家
		for (String otherPlayerId : pukeGameValueObject.allPlayerIds()) {
			wsNotifier.notifyWatchInfo(otherPlayerId, "leave", playerId, nickName, headimgurl);
		}
		// 通知观战者
		Map<String, Watcher> map = gameCmdService.getwatch(gameId);
		if (!CollectionUtils.isEmpty(map)) {
			for (Watcher list : map.values()) {
				if (!list.getId().equals(playerId)) {
					wsNotifier.notifyWatchInfo(list.getId(), "input", playerId, nickName, headimgurl);
				}
			}
		}

		Watcher watcher = new Watcher();
		watcher.setId(playerId);
		watcher.setHeadimgurl(headimgurl);
		watcher.setNickName(nickName);
		watcher.setState("leave");
		WatchRecord watchRecord = pukeGameQueryService.saveWatchRecord(gameId, watcher);
		watchRecordMsgService.leaveWatch(watchRecord);

		return CommonVoUtil.success("leave success");
	}

	/**
	 * 通知观战者
	 */
	private void hintWatcher(String gameId, String flag) {
		Map<String, Object> map = gameCmdService.getwatch(gameId);
		if (!CollectionUtils.isEmpty(map)) {
			List<String> playerIds = map.entrySet().stream().map(e -> e.getKey()).collect(Collectors.toList());
			wsNotifier.notifyToWatchQuery(playerIds, flag);
			if (WatchQueryScope.watchEnd.name().equals(flag)) {
				gameCmdService.recycleWatch(gameId);
			}
		}
	}
}
