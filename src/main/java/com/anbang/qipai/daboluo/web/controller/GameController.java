package com.anbang.qipai.daboluo.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.cqrs.c.service.GameCmdService;
import com.anbang.qipai.daboluo.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.daboluo.cqrs.q.service.PukeGameQueryService;
import com.anbang.qipai.daboluo.plan.service.MemberGoldBalanceService;
import com.anbang.qipai.daboluo.web.vo.CommonVO;
import com.anbang.qipai.daboluo.websocket.GamePlayWsNotifier;
import com.anbang.qipai.daboluo.websocket.QueryScope;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.dml.shisanshui.pai.wanfa.BianXingWanFa;

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
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private MemberGoldBalanceService memberGoldBalanceService;

	/**
	 * 新一局游戏
	 */
	@RequestMapping(value = "/newgame")
	@ResponseBody
	public CommonVO newgame(String gameId, String playerId, int panshu, int renshu, boolean dqef, boolean dqsf,
			BianXingWanFa bx, boolean bihuase, boolean zidongzupai, boolean yitiaolong) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		PukeGameValueObject pukeGameValueObject = gameCmdService.newPukeGame(newGameId, playerId, panshu, renshu, dqef,
				dqsf, bx, bihuase, zidongzupai, yitiaolong);
		pukeGameQueryService.newPukeGame(pukeGameValueObject);
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
	public CommonVO newgame_leave_quit(String gameId, String playerId, int panshu, int renshu, boolean dqef,
			boolean dqsf, BianXingWanFa bx, boolean bihuase, boolean zidongzupai, boolean yitiaolong) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		PukeGameValueObject pukeGameValueObject = gameCmdService.newPukeGameLeaveAndQuit(newGameId, playerId, panshu,
				renshu, dqef, dqsf, bx, bihuase, zidongzupai, yitiaolong);
		pukeGameQueryService.newPukeGame(pukeGameValueObject);
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
		pukeGameQueryService.joinGame(pukeGameValueObject);
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
}
