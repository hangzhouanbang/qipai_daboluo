package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.Dun;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.pai.paixing.comparator.DunComparator;
import com.dml.shisanshui.pai.wanfa.BianXingWanFa;
import com.dml.shisanshui.player.action.ChupaiPaixingSolutionCalculator;

public class DaboluoChupaiPaixingSolutionCalculator implements ChupaiPaixingSolutionCalculator {

	private DunComparator dunComparator;

	private BianXingWanFa bx;

	@Override
	public List<PaixingSolution> generateAllPaixingSolution(Map<Integer, PukePai> allShoupai) {
		List<PaixingSolution> solutions = new ArrayList<>();
		List<Integer> paiIdList = new ArrayList<>(allShoupai.keySet());
		Dun toudun = new Dun();
		Set<Integer> removed = new HashSet<>();
		calculateFirstDun(solutions, paiIdList, removed, 0, 0, toudun, allShoupai);
		return solutions;
	}

	/**
	 * 计算第一墩
	 */
	public void calculateFirstDun(List<PaixingSolution> solutions, List<Integer> paiIdList, Set<Integer> removed,
			int length, int index, Dun toudun, Map<Integer, PukePai> allShoupai) {
		if (length < 3) {
			for (int i = 0 + index; i < paiIdList.size() + length - 2; i++) {
				Dun newDun = new Dun();
				newDun.getPukePaiList().addAll(toudun.getPukePaiList());
				Set<Integer> newRemoved = new HashSet<>(removed);
				newRemoved.add(paiIdList.get(i));
				newDun.getPukePaiList().add(allShoupai.get(paiIdList.get(i)));
				calculateFirstDun(solutions, paiIdList, newRemoved, length + 1, i + 1, newDun, allShoupai);
			}
		} else {
			toudun.calculatePaixing(bx);
			Dun zhongdun = new Dun();
			List<Integer> remain = new ArrayList<>();
			for (Integer i : paiIdList) {
				if (!removed.contains(i)) {
					remain.add(i);
				}
			}
			removed.clear();
			calculateSecondDun(solutions, remain, removed, 0, 0, toudun, zhongdun, allShoupai);
		}
	}

	/**
	 * 计算第二墩
	 */
	public void calculateSecondDun(List<PaixingSolution> solutions, List<Integer> paiIdList, Set<Integer> removed,
			int length, int index, Dun toudun, Dun zhongdun, Map<Integer, PukePai> allShoupai) {
		if (length < 5) {
			for (int i = 0 + index; i < paiIdList.size() + length - 4; i++) {
				Dun newDun = new Dun();
				newDun.getPukePaiList().addAll(zhongdun.getPukePaiList());
				Set<Integer> newRemoved = new HashSet<>(removed);
				newRemoved.add(paiIdList.get(i));
				newDun.getPukePaiList().add(allShoupai.get(paiIdList.get(i)));
				calculateSecondDun(solutions, paiIdList, newRemoved, length + 1, i + 1, toudun, newDun, allShoupai);
			}
		} else {
			zhongdun.calculatePaixing(bx);
			if (verify(zhongdun, toudun)) {
				List<Integer> remain = new ArrayList<>();
				for (Integer i : paiIdList) {
					if (!removed.contains(i)) {
						remain.add(i);
					}
				}
				calculateThirdDun(solutions, remain, toudun, zhongdun, allShoupai);
			}
		}
	}

	/**
	 * 计算第三墩
	 */
	public void calculateThirdDun(List<PaixingSolution> solutions, List<Integer> paiIdList, Dun toudun, Dun zhongdun,
			Map<Integer, PukePai> allShoupai) {
		Dun weidun = new Dun();
		for (int id : paiIdList) {
			weidun.getPukePaiList().add(allShoupai.get(id));
		}
		weidun.calculatePaixing(bx);
		if (verify(weidun, zhongdun)) {
			PaixingSolution solution = new PaixingSolution();
			solution.setToudao(toudun);
			solution.setZhongdao(zhongdun);
			solution.setWeidao(weidun);
			solution.calculateIndex();
			solutions.add(solution);
		}
	}

	/**
	 * 头墩必须小于等于中墩,中墩必须小于等于尾墩,否则为 "相公".
	 */
	public boolean verify(Dun dun1, Dun dun2) {
		return dunComparator.compare(dun2, dun1) <= 0;
	}

	public DunComparator getDunComparator() {
		return dunComparator;
	}

	public void setDunComparator(DunComparator dunComparator) {
		this.dunComparator = dunComparator;
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

}
