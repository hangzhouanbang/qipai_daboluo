package com.anbang.qipai.daboluo.cqrs.c.service;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.PukeActionResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.ReadyToNextPanResult;

public interface PukePlayCmdService {

	PukeActionResult chupai(String playerId, String toudaoIndex, String zhongdaoIndex, String weidaoIndex,
			Long actionTime) throws Exception;

	ReadyToNextPanResult readyToNextPan(String playerId) throws Exception;

}
