package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJiesuanScore;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanPlayerResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanResult;
import com.dml.shisanshui.ju.Ju;
import com.dml.shisanshui.pai.paixing.comparator.DaoComparator;
import com.dml.shisanshui.pan.CurrentPanResultBuilder;
import com.dml.shisanshui.pan.Pan;
import com.dml.shisanshui.pan.PanResult;
import com.dml.shisanshui.pan.PanValueObject;
import com.dml.shisanshui.player.ShisanshuiPlayer;

public class DaboluoCurrentPanResultBuilder implements CurrentPanResultBuilder {

	private DaoComparator daoComparator;
	private int renshu;
	private boolean dqef;// 打枪二番
	private boolean dqsf;// 打枪三番
	private BianXingWanFa bx;
	private boolean yitiaolong;// 一条龙

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		DaboluoPanResult latestFinishedPanResult = (DaboluoPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (DaboluoPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}
		Pan currentPan = ju.getCurrentPan();
		List<DaboluoPanPlayerResult> panPlayerResultList = new ArrayList<>();

		List<String> playerIdList = currentPan.sortedPlayerIds();
		for (String playerId : playerIdList) {
			DaboluoPanPlayerResult playerResult = new DaboluoPanPlayerResult();
			playerResult.setPlayerId(playerId);
			ShisanshuiPlayer player = currentPan.findShisanshuiPlayerById(playerId);
			DaboluoJiesuanScore jiesuanScore = new DaboluoJiesuanScore();
			jiesuanScore.setChupaiSolution(player.getChupaiSolution());
			playerResult.setJiesuanScore(jiesuanScore);
			panPlayerResultList.add(playerResult);
		}
		// 两两结算基础分
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			DaboluoPanPlayerResult playerResult1 = panPlayerResultList.get(i);
			String playerId1 = playerResult1.getPlayerId();
			DaboluoJiesuanScore score1 = playerResult1.getJiesuanScore();
			for (int j = i + 1; j < panPlayerResultList.size(); j++) {
				DaboluoPanPlayerResult playerResult2 = panPlayerResultList.get(j);
				String playerId2 = playerResult2.getPlayerId();
				DaboluoJiesuanScore score2 = playerResult2.getJiesuanScore();
				score1.calculatePlayerJiesuanScore(playerId2, score2.getChupaiSolution(), daoComparator);
				score2.calculatePlayerJiesuanScore(playerId1, score1.getChupaiSolution(), daoComparator);
			}
		}
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			DaboluoPanPlayerResult playerResult = panPlayerResultList.get(i);
			playerResult.getJiesuanScore().calculateDaoScore();
			playerResult.getJiesuanScore().calculatePaixing(renshu, dqef, dqsf, bx, yitiaolong);
		}
		// 特殊牌型结算
		for (int i = 0; i < panPlayerResultList.size(); i++) {
			DaboluoPanPlayerResult playerResult1 = panPlayerResultList.get(i);
			String playerId1 = playerResult1.getPlayerId();
			DaboluoJiesuanScore score1 = playerResult1.getJiesuanScore();
			for (int j = i + 1; j < panPlayerResultList.size(); j++) {
				DaboluoPanPlayerResult playerResult2 = panPlayerResultList.get(j);
				String playerId2 = playerResult2.getPlayerId();
				DaboluoJiesuanScore score2 = playerResult2.getJiesuanScore();
				score1.jiesuan(-score2.getPlayerJiesuanScoreById(playerId1));
				score2.jiesuan(-score1.getPlayerJiesuanScoreById(playerId2));
			}
		}
		DaboluoPanResult panResult = new DaboluoPanResult();
		panResult.setPan(new PanValueObject(currentPan));
		panResult.setPanFinishTime(panFinishTime);
		panResult.setPanPlayerResultList(panPlayerResultList);
		return panResult;
	}

	public DaoComparator getDaoComparator() {
		return daoComparator;
	}

	public void setDaoComparator(DaoComparator daoComparator) {
		this.daoComparator = daoComparator;
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

	public boolean isYitiaolong() {
		return yitiaolong;
	}

	public void setYitiaolong(boolean yitiaolong) {
		this.yitiaolong = yitiaolong;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

}
