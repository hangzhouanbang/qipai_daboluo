package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.Paixing;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.pai.paixing.comparator.DaoComparator;
import com.dml.shisanshui.player.action.ChupaiPaixingSolutionFilter;

public class DaboluoChupaiPaixingSolutionFilter implements ChupaiPaixingSolutionFilter {

	private DaoComparator daoComparator;
	private boolean zidongzupai;// 自动组牌

	@Override
	public List<PaixingSolution> filter(Map<Integer, PukePai> allShoupai, List<Dao> daoList) {
		if (!zidongzupai) {
			return new ArrayList<>();
		}
		Map<String, PaixingSolution> solutions = new HashMap<>();
		List<Dao> wumeiList = new LinkedList<>();
		List<Dao> tonghuashunList = new LinkedList<>();
		List<Dao> tiezhiList = new LinkedList<>();
		List<Dao> huluList = new LinkedList<>();
		List<Dao> tonghuaList = new LinkedList<>();
		List<Dao> shunziList = new LinkedList<>();
		List<Dao> santiaoList = new LinkedList<>();
		List<Dao> liangduiList = new LinkedList<>();
		List<Dao> duiziList = new LinkedList<>();
		List<Dao> wulongList = new LinkedList<>();
		for (Dao dao : daoList) {
			Paixing paixing = dao.getPaixing();
			int ordinal = paixing.ordinal();
			switch (ordinal) {
			case 9:
				wumeiList.add(dao);
				break;
			case 8:
				tonghuashunList.add(dao);
				break;
			case 7:
				tiezhiList.add(dao);
				break;
			case 6:
				huluList.add(dao);
				break;
			case 5:
				tonghuaList.add(dao);
				break;
			case 4:
				shunziList.add(dao);
				break;
			case 3:
				santiaoList.add(dao);
				break;
			case 2:
				liangduiList.add(dao);
				break;
			case 1:
				duiziList.add(dao);
				break;
			default:
				wulongList.add(dao);
				break;
			}
		}
		List<Dao> sortedDaoList = new LinkedList<>();
		sortedDaoList.addAll(wumeiList);
		sortedDaoList.addAll(tonghuashunList);
		sortedDaoList.addAll(tiezhiList);
		sortedDaoList.addAll(huluList);
		sortedDaoList.addAll(tonghuaList);
		sortedDaoList.addAll(shunziList);
		sortedDaoList.addAll(santiaoList);
		sortedDaoList.addAll(liangduiList);
		sortedDaoList.addAll(duiziList);
		sortedDaoList.addAll(wulongList);
		while (solutions.size() < 9) {
			PaixingSolution solution = new PaixingSolution();
			for (int i = 0; i < sortedDaoList.size(); i++) {
				Dao dao = sortedDaoList.get(i);
				verifyAndFill(solution, dao);
				if (solution.getToudao() != null) {
					sortedDaoList.remove(i);
					String key = solution.getToudao().getIndex() + solution.getZhongdao().getIndex()
							+ solution.getWeidao().getIndex();
					solutions.put(key, solution);
					break;
				}
			}
		}
		return new ArrayList<>(solutions.values());
	}

	private void verifyAndFill(PaixingSolution solution, Dao dao) {
		Dao toudao = solution.getToudao();
		Dao zhongdao = solution.getZhongdao();
		Dao weidao = solution.getWeidao();
		Map<Integer, PukePai> allPai = new HashMap<>();
		if (toudao != null) {
			for (PukePai pukePai : toudao.getPukePaiList()) {
				allPai.put(pukePai.getId(), pukePai);
			}
		}
		if (zhongdao != null) {
			for (PukePai pukePai : zhongdao.getPukePaiList()) {
				allPai.put(pukePai.getId(), pukePai);
			}
		}
		if (weidao != null) {
			for (PukePai pukePai : weidao.getPukePaiList()) {
				allPai.put(pukePai.getId(), pukePai);
			}
		}
		for (PukePai pukePai : dao.getPukePaiList()) {
			if (allPai.containsKey(pukePai.getId())) {
				return;
			}
		}
		if (toudao != null) {
			return;
		} else if (zhongdao != null && dao.getPukePaiList().size() == 3 && daoComparator.compare(dao, zhongdao) <= 0) {
			solution.setToudao(dao);
			return;
		} else if (weidao != null && dao.getPukePaiList().size() == 5 && daoComparator.compare(dao, weidao) <= 0) {
			solution.setZhongdao(dao);
			return;
		} else if (weidao == null && dao.getPukePaiList().size() == 5) {
			solution.setWeidao(dao);
		}
	}

	public DaoComparator getDaoComparator() {
		return daoComparator;
	}

	public void setDaoComparator(DaoComparator daoComparator) {
		this.daoComparator = daoComparator;
	}

	public boolean isZidongzupai() {
		return zidongzupai;
	}

	public void setZidongzupai(boolean zidongzupai) {
		this.zidongzupai = zidongzupai;
	}

}
