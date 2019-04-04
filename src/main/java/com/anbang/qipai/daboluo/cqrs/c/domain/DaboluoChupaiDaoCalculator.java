package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.player.action.ChupaiDaoCalculator;

/**
 * 大菠萝牌型计算器
 * 
 * @author lsc
 *
 */
public class DaboluoChupaiDaoCalculator implements ChupaiDaoCalculator {

	private boolean bihuase;

	private BianXingWanFa bx;

	@Override
	public List<Dao> generateAllPaixingSolution(Map<Integer, PukePai> allShoupai) {
		List<Dao> daoList = new ArrayList<>();
		List<Integer> paiIdList = new ArrayList<>(allShoupai.keySet());
		Dao dao = new Dao();
		Set<Integer> removed = new HashSet<>();
		// 计算所有三张牌的道
		calculateSanDun(daoList, paiIdList, removed, 0, 0, dao, allShoupai);
		// 计算所有五张牌的道
		calculateWuDun(daoList, paiIdList, removed, 0, 0, dao, allShoupai);
		return daoList;
	}

	private void calculateSanDun(List<Dao> daoList, List<Integer> paiIdList, Set<Integer> removed, int length,
			int index, Dao dao, Map<Integer, PukePai> allShoupai) {
		if (length < 3) {
			for (int i = 0 + index; i < paiIdList.size() + length - 2; i++) {
				Dao newDao = new Dao();
				newDao.getPukePaiList().addAll(dao.getPukePaiList());
				Set<Integer> newRemoved = new HashSet<>(removed);
				newRemoved.add(paiIdList.get(i));
				newDao.getPukePaiList().add(allShoupai.get(paiIdList.get(i)));
				calculateSanDun(daoList, paiIdList, newRemoved, length + 1, i + 1, newDao, allShoupai);
			}
		} else {
			dao.calculateIndex();
			DaoTypeCodeCalculator.calculateDaoTypeCode(dao, bihuase, bx);
			daoList.add(dao);
		}
	}

	private void calculateWuDun(List<Dao> daoList, List<Integer> paiIdList, Set<Integer> removed, int length, int index,
			Dao dao, Map<Integer, PukePai> allShoupai) {
		if (length < 5) {
			for (int i = 0 + index; i < paiIdList.size() + length - 4; i++) {
				Dao newDao = new Dao();
				newDao.getPukePaiList().addAll(dao.getPukePaiList());
				Set<Integer> newRemoved = new HashSet<>(removed);
				newRemoved.add(paiIdList.get(i));
				newDao.getPukePaiList().add(allShoupai.get(paiIdList.get(i)));
				calculateWuDun(daoList, paiIdList, newRemoved, length + 1, i + 1, newDao, allShoupai);
			}
		} else {
			dao.calculateIndex();
			DaoTypeCodeCalculator.calculateDaoTypeCode(dao, bihuase, bx);
			daoList.add(dao);
		}
	}

	public boolean isBihuase() {
		return bihuase;
	}

	public void setBihuase(boolean bihuase) {
		this.bihuase = bihuase;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

}
