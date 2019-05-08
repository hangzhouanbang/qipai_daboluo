package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.daboluo.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.dml.shisanshui.pan.PanActionFrame;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;

public class PlayerActionFrameFilter {

	public PanActionFrame filter(GameLatestPanActionFrameDbo frame, String playerId) {
		PanActionFrame panActionFrame = frame.getPanActionFrame();
		panActionFrame.setAction(null);
		List<ShisanshuiPlayerValueObject> playerList = panActionFrame.getPanAfterAction().getPlayerList();
		for (ShisanshuiPlayerValueObject player : playerList) {
			if (!playerId.equals(player.getId())) {
				player.setAllShoupai(null);
				player.setShoupaiIdListForSortList(null);
				player.setChupaiSolution(null);
				player.setWulongCandidates(null);
				player.setDuiziCandidates(null);
				player.setLiangduiCandidates(null);
				player.setSantiaoCandidates(null);
				player.setShunziCandidates(null);
				player.setTonghuaCandidates(null);
				player.setTiezhiCandidates(null);
				player.setHuluCandidates(null);
				player.setTonghuashunCandidates(null);
				player.setWumeiCandidates(null);
				player.setChupaiSolutionForTips(null);
			}
		}
		return panActionFrame;
	}
}
