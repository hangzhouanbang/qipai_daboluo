package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.daboluo.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shisanshui.pan.PanActionFrame;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;

public class PlayerActionFrameFilter {

	public PanActionFrame filter(GameLatestPanActionFrameDbo frame, String playerId) {
		PanActionFrame panActionFrame = frame.getPanActionFrame();
		panActionFrame.getAction().setSolution(null);
		List<ShisanshuiPlayerValueObject> playerList = panActionFrame.getPanAfterAction().getPlayerList();
		for (ShisanshuiPlayerValueObject player : playerList) {
			if (!playerId.equals(player.getId())) {
				player.setAllShoupai(null);
				player.setChupaiSolution(null);
				player.setChupaiSolutionCandidates(null);
				player.setChupaiSolutionForTips(null);
			}
		}
		return panActionFrame;
	}
}
