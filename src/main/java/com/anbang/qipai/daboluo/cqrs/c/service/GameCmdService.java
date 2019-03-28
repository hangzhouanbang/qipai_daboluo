package com.anbang.qipai.daboluo.cqrs.c.service;

import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.dml.shisanshui.pai.wanfa.BianXingWanFa;

public interface GameCmdService {

	PukeGameValueObject newPukeGame(String gameId, String playerId, Integer panshu, Integer renshu, Boolean dqef,
			Boolean dqsf, BianXingWanFa bx, Boolean bihuase, Boolean zidongzupai, Boolean yitiaolong);

	PukeGameValueObject newPukeGameLeaveAndQuit(String gameId, String playerId, Integer panshu, Integer renshu,
			Boolean dqef, Boolean dqsf, BianXingWanFa bx, Boolean bihuase, Boolean zidongzupai, Boolean yitiaolong);

	PukeGameValueObject joinGame(String playerId, String gameId) throws Exception;

	PukeGameValueObject leaveGameByHangup(String playerId) throws Exception;
}
