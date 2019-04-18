package com.anbang.qipai.daboluo.msg.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.anbang.qipai.daboluo.cqrs.c.service.GameCmdService;
import com.anbang.qipai.daboluo.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGameDbo;
import com.anbang.qipai.daboluo.cqrs.q.dbo.PukeGamePlayerDbo;
import com.anbang.qipai.daboluo.cqrs.q.service.PukeGameQueryService;
import com.anbang.qipai.daboluo.cqrs.q.service.PukePlayQueryService;
import com.anbang.qipai.daboluo.msg.channel.GameRoomSink;
import com.anbang.qipai.daboluo.msg.msjobj.CommonMO;
import com.anbang.qipai.daboluo.msg.msjobj.PukeHistoricalJuResult;
import com.anbang.qipai.daboluo.msg.service.DaboluoGameMsgService;
import com.anbang.qipai.daboluo.msg.service.DaboluoResultMsgService;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.google.gson.Gson;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private PukeGameQueryService pukeGameQueryService;

	@Autowired
	private PukePlayQueryService pukePlayQueryService;

	@Autowired
	private DaboluoResultMsgService daboluoResultMsgService;

	@Autowired
	private DaboluoGameMsgService daboluoGameMsgService;

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.DABOLUOGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {
			List<String> gameIds = gson.fromJson(json, ArrayList.class);
			for (String gameId : gameIds) {
				try {
					PukeGameDbo pukeGameDbo = pukeGameQueryService.findPukeGameDboById(gameId);
					boolean playerOnline = false;
					for (PukeGamePlayerDbo player : pukeGameDbo.getPlayers()) {
						if (GamePlayerOnlineState.online.equals(player.getOnlineState())) {
							playerOnline = true;
						}
					}
					if (playerOnline) {
						daboluoGameMsgService.delay(gameId);
					} else {
						PukeGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
						pukeGameQueryService.finishGameImmediately(gameValueObject);
						daboluoGameMsgService.gameFinished(gameId);
						JuResultDbo juResultDbo = pukePlayQueryService.findJuResultDbo(gameId);
						if (juResultDbo != null) {
							PukeHistoricalJuResult juResult = new PukeHistoricalJuResult(juResultDbo, pukeGameDbo);
							daboluoResultMsgService.recordJuResult(juResult);
						}
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

}
