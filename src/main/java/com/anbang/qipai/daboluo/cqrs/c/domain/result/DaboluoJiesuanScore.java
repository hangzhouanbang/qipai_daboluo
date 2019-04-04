package com.anbang.qipai.daboluo.cqrs.c.domain.result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.daboluo.cqrs.c.domain.BianXingWanFa;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.pai.paixing.comparator.DaoComparator;

/**
 * 大菠萝个人结算分
 * 
 * @author lsc
 *
 */
public class DaboluoJiesuanScore {
	private PaixingSolution chupaiSolution;
	private DaboluoDaoScore toudao;// 第一道情况
	private DaboluoDaoScore zhongdao;// 第二道情况
	private DaboluoDaoScore weidao;// 第三道情况
	private Map<String, PlayerJiesuanScore> playerDaoMap = new HashMap<>();
	private Set<String> daqiangPlayerSet = new HashSet<>();
	private boolean daqiang;// 是否打枪
	private int dqbs = 1;// 打枪倍数
	private boolean quanleida;// 是否全垒打
	private int qldbs = 2;// 全垒打倍数
	private boolean tiezhi;// 是否有铁支
	private int tzbs = 2;// 铁支倍数
	private boolean tonghuashun;// 是否有同花顺
	private int thsbs = 2;// 同花顺倍数
	private boolean wumei;// 是否有五枚
	private int wmbs = 4;// 五枚倍数
	private boolean yitiaolong;// 是否一条龙
	private int score;// 基础结算分
	private int value;// 总分

	/**
	 * 修正总分
	 */
	public void jiesuan(int detal) {
		value += detal;
	}

	public int getPlayerJiesuanScoreById(String playerId) {
		return playerDaoMap.get(playerId).getValue();
	}

	/**
	 * 计算总分
	 */
	public void calculatePaixing(int renshu, boolean dqef, boolean dqsf, BianXingWanFa bx, boolean yitiaolong) {
		this.yitiaolong = isYitiaolong(bx, yitiaolong);
		wumei = hasWumei();
		tonghuashun = hasTonghuashun();
		tiezhi = hasTiezhi();
		quanleida = true;
		for (PlayerJiesuanScore playerJiesuanScore : playerDaoMap.values()) {
			if (playerJiesuanScore.isDaqiang()) {
				daqiangPlayerSet.add(playerJiesuanScore.getPlayerId());
				daqiang = true;
			} else {
				quanleida = false;
			}
		}
		if (dqef) {
			dqbs = 2;
		} else if (dqsf) {
			dqbs = 2 << daqiangPlayerSet.size();
		}
		if (renshu != 4 || dqsf) {
			quanleida = false;
		}
		calculateValue();
	}

	/**
	 * 计算特殊牌型分数
	 */
	private void calculateValue() {
		for (PlayerJiesuanScore playerJiesuanScore : playerDaoMap.values()) {
			int jiesuan = playerJiesuanScore.getScore();
			if (playerJiesuanScore.getWeidao() > 0) {
				if (daqiangPlayerSet.contains(playerJiesuanScore.getPlayerId())) {
					jiesuan *= dqbs;
				}
				if (quanleida) {
					jiesuan *= qldbs;
				}
				if (tiezhi) {
					jiesuan *= tzbs;
				}
				if (tonghuashun) {
					jiesuan *= thsbs;
				}
				if (wumei) {
					jiesuan *= wmbs;
				}
			}
			if (!playerJiesuanScore.hasWumei() && yitiaolong) {
				jiesuan = 96;
			}
			playerJiesuanScore.setValue(jiesuan);
			value += playerJiesuanScore.getValue();
		}
	}

	private boolean hasTiezhi() {
		return toudao.isTiezhi() || zhongdao.isTiezhi() || weidao.isTiezhi();
	}

	private boolean hasWumei() {
		return toudao.isWumei() || zhongdao.isWumei() || weidao.isWumei();
	}

	private boolean hasTonghuashun() {
		return toudao.isTonghuashun() || zhongdao.isTonghuashun() || weidao.isTonghuashun();
	}

	private boolean isYitiaolong(BianXingWanFa bx, boolean yitiaolong) {
		if (yitiaolong) {
			int[] dianshuArray = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			for (PukePai pai : chupaiSolution.getToudao().getPukePaiList()) {
				int ordinal = pai.getPaiMian().dianShu().ordinal();
				if (dianshuArray[ordinal] > 0) {
					dianshuArray[ordinal]--;
				}
			}
			for (PukePai pai : chupaiSolution.getZhongdao().getPukePaiList()) {
				int ordinal = pai.getPaiMian().dianShu().ordinal();
				if (dianshuArray[ordinal] > 0) {
					dianshuArray[ordinal]--;
				}
			}
			for (PukePai pai : chupaiSolution.getWeidao().getPukePaiList()) {
				int ordinal = pai.getPaiMian().dianShu().ordinal();
				if (dianshuArray[ordinal] > 0) {
					dianshuArray[ordinal]--;
				}
			}
			int dianshuCount = 0;
			for (int i : dianshuArray) {
				dianshuCount += i;
			}
			if (dianshuCount == 2) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 计算总基础分
	 */
	public void calculateDaoScore() {
		toudao = new DaboluoDaoScore();
		toudao.setPaixing(chupaiSolution.getToudao().getPaixing());
		zhongdao = new DaboluoDaoScore();
		zhongdao.setPaixing(chupaiSolution.getZhongdao().getPaixing());
		weidao = new DaboluoDaoScore();
		weidao.setPaixing(chupaiSolution.getWeidao().getPaixing());
		for (PlayerJiesuanScore playerJiesuanScore : playerDaoMap.values()) {
			toudao.jiesaunScore(playerJiesuanScore.getToudao());
			zhongdao.jiesaunScore(playerJiesuanScore.getZhongdao());
			weidao.jiesaunScore(playerJiesuanScore.getWeidao());
		}
		score = toudao.getScore() + zhongdao.getScore() + weidao.getScore();
	}

	/**
	 * 计算每个玩家的每道之间的基础分
	 */
	public void calculatePlayerJiesuanScore(String playerId, PaixingSolution solution, DaoComparator daoComparator) {
		PlayerJiesuanScore playerJiesuanScore = new PlayerJiesuanScore();
		playerJiesuanScore.setPlayerId(playerId);
		playerJiesuanScore.setToudao(calculatePlayerToudao(solution, daoComparator));
		playerJiesuanScore.setToudaoPaixing(solution.getToudao().getPaixing());
		playerJiesuanScore.setZhongdao(calculatePlayerZhongdao(solution, daoComparator));
		playerJiesuanScore.setZhongdaoPaixing(solution.getZhongdao().getPaixing());
		playerJiesuanScore.setWeidao(calculatePlayerWeidao(solution, daoComparator));
		playerJiesuanScore.setWeidaoPaixing(solution.getWeidao().getPaixing());
		playerJiesuanScore.calculateScore();
		playerDaoMap.put(playerId, playerJiesuanScore);
	}

	private int calculatePlayerToudao(PaixingSolution solution, DaoComparator daoComparator) {
		if (daoComparator.compare(chupaiSolution.getToudao(), solution.getToudao()) > 0) {
			return 1;
		} else if (daoComparator.compare(chupaiSolution.getToudao(), solution.getToudao()) < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	private int calculatePlayerZhongdao(PaixingSolution solution, DaoComparator daoComparator) {
		if (daoComparator.compare(chupaiSolution.getZhongdao(), solution.getZhongdao()) > 0) {
			return 1;
		} else if (daoComparator.compare(chupaiSolution.getZhongdao(), solution.getZhongdao()) < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	private int calculatePlayerWeidao(PaixingSolution solution, DaoComparator daoComparator) {
		if (daoComparator.compare(chupaiSolution.getWeidao(), solution.getWeidao()) > 0) {
			return 1;
		} else if (daoComparator.compare(chupaiSolution.getWeidao(), solution.getWeidao()) < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	public PaixingSolution getChupaiSolution() {
		return chupaiSolution;
	}

	public void setChupaiSolution(PaixingSolution chupaiSolution) {
		this.chupaiSolution = chupaiSolution;
	}

	public DaboluoDaoScore getToudao() {
		return toudao;
	}

	public void setToudao(DaboluoDaoScore toudao) {
		this.toudao = toudao;
	}

	public DaboluoDaoScore getZhongdao() {
		return zhongdao;
	}

	public void setZhongdao(DaboluoDaoScore zhongdao) {
		this.zhongdao = zhongdao;
	}

	public DaboluoDaoScore getWeidao() {
		return weidao;
	}

	public void setWeidao(DaboluoDaoScore weidao) {
		this.weidao = weidao;
	}

	public Map<String, PlayerJiesuanScore> getPlayerDaoMap() {
		return playerDaoMap;
	}

	public void setPlayerDaoMap(Map<String, PlayerJiesuanScore> playerDaoMap) {
		this.playerDaoMap = playerDaoMap;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Set<String> getDaqiangPlayerSet() {
		return daqiangPlayerSet;
	}

	public void setDaqiangPlayerSet(Set<String> daqiangPlayerSet) {
		this.daqiangPlayerSet = daqiangPlayerSet;
	}

	public boolean isDaqiang() {
		return daqiang;
	}

	public void setDaqiang(boolean daqiang) {
		this.daqiang = daqiang;
	}

	public int getDqbs() {
		return dqbs;
	}

	public void setDqbs(int dqbs) {
		this.dqbs = dqbs;
	}

	public boolean isQuanleida() {
		return quanleida;
	}

	public void setQuanleida(boolean quanleida) {
		this.quanleida = quanleida;
	}

	public int getQldbs() {
		return qldbs;
	}

	public void setQldbs(int qldbs) {
		this.qldbs = qldbs;
	}

	public boolean isTiezhi() {
		return tiezhi;
	}

	public void setTiezhi(boolean tiezhi) {
		this.tiezhi = tiezhi;
	}

	public int getTzbs() {
		return tzbs;
	}

	public void setTzbs(int tzbs) {
		this.tzbs = tzbs;
	}

	public boolean isTonghuashun() {
		return tonghuashun;
	}

	public void setTonghuashun(boolean tonghuashun) {
		this.tonghuashun = tonghuashun;
	}

	public int getThsbs() {
		return thsbs;
	}

	public void setThsbs(int thsbs) {
		this.thsbs = thsbs;
	}

	public boolean isWumei() {
		return wumei;
	}

	public void setWumei(boolean wumei) {
		this.wumei = wumei;
	}

	public int getWmbs() {
		return wmbs;
	}

	public void setWmbs(int wmbs) {
		this.wmbs = wmbs;
	}

	public boolean isYitiaolong() {
		return yitiaolong;
	}

	public void setYitiaolong(boolean yitiaolong) {
		this.yitiaolong = yitiaolong;
	}

}
