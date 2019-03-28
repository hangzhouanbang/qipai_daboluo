package com.anbang.qipai.daboluo.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.DaboluoChupaiPaixingSolutionCalculator;
import com.anbang.qipai.daboluo.cqrs.c.domain.DaboluoDunComparator;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.PukePaiMian;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.pai.wanfa.BianXingWanFa;

public class CaseTest {
	public static void main(String[] args) {
		for (int j = 0; j < 100; j++) {
			List<PukePai> allPaiList = new ArrayList<>();
			// 生成一副牌
			int id = 0;
			for (PukePaiMian paiType : PukePaiMian.values()) {
				PukePai pai = new PukePai();
				pai.setId(id);
				pai.setPaiMian(paiType);
				allPaiList.add(pai);
				id++;
			}
			Collections.shuffle(allPaiList);
			Map<Integer, PukePai> allShoupai = new HashMap<>();
			for (int i = 0; i < 13; i++) {
				PukePai pukePai = allPaiList.remove(0);
				allShoupai.put(pukePai.getId(), pukePai);
			}
			DaboluoChupaiPaixingSolutionCalculator calculator = new DaboluoChupaiPaixingSolutionCalculator();
			calculator.setBx(BianXingWanFa.baibian);
			calculator.setDunComparator(new DaboluoDunComparator());
			long s1 = System.currentTimeMillis();
			// for (int i = 0; i < 72072; i++) {
			//
			// }
			List<PaixingSolution> solutions = calculator.generateAllPaixingSolution(allShoupai);
			long s2 = System.currentTimeMillis();
			System.out.println(s2 - s1 + "ms");
			System.out.println("总数：" + solutions.size());
			// Set<String> indexs = new HashSet<>();
			// solutions.forEach((solution) -> indexs.add(solution.getPaixingIndex()));
			// System.out.println("总数：" + indexs.size());
		}
	}
}
