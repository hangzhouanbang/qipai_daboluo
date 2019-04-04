package com.anbang.qipai.daboluo.cqrs.c.domain.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.BianXingWanFa;
import com.anbang.qipai.daboluo.cqrs.c.domain.DaboluoChupaiDaoCalculator;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.PukePaiMian;
import com.dml.shisanshui.pai.paixing.Dao;

public class CaseTest {
	public static void main(String[] args) {
		// for (int j = 0; j < 100; j++) {
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
		DaboluoChupaiDaoCalculator daboluoChupaiDaoCalculator = new DaboluoChupaiDaoCalculator();
		daboluoChupaiDaoCalculator.setBihuase(true);
		daboluoChupaiDaoCalculator.setBx(BianXingWanFa.baibian);
		long s1 = System.currentTimeMillis();
		List<Dao> daoList = daboluoChupaiDaoCalculator.generateAllPaixingSolution(allShoupai);
		long s2 = System.currentTimeMillis();
		System.out.println("总耗时：" + (s2 - s1) + "ms");
		System.out.println("总数：" + daoList.size());
	}
	// }
}
