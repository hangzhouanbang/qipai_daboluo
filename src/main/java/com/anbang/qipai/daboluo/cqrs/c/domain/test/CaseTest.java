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
import com.dml.shisanshui.pai.paixing.Paixing;

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
			System.out.println(pukePai.getPaiMian());
			allShoupai.put(pukePai.getId(), pukePai);
		}
		DaboluoChupaiDaoCalculator daboluoChupaiDaoCalculator = new DaboluoChupaiDaoCalculator();
		daboluoChupaiDaoCalculator.setBihuase(true);
		daboluoChupaiDaoCalculator.setBx(BianXingWanFa.bubian);
		long s1 = System.currentTimeMillis();
		List<Dao> daoList = daboluoChupaiDaoCalculator.generateAllPaixingSolution(allShoupai);
		long s2 = System.currentTimeMillis();
		System.out.println("总耗时：" + (s2 - s1) + "ms");
		List<Dao> wulongCandidates = new ArrayList<>();
		List<Dao> duiziCandidates = new ArrayList<>();
		List<Dao> liangduiCandidates = new ArrayList<>();
		List<Dao> santiaoCandidates = new ArrayList<>();
		List<Dao> shunziCandidates = new ArrayList<>();
		List<Dao> tonghuaCandidates = new ArrayList<>();
		List<Dao> huluCandidates = new ArrayList<>();
		List<Dao> tiezhiCandidates = new ArrayList<>();
		List<Dao> tonghuashunCandidates = new ArrayList<>();
		List<Dao> wumeiCandidates = new ArrayList<>();
		for (Dao dao : daoList) {
			List<PukePai> pukePaiList = dao.getPukePaiList();
			if (pukePaiList.size() != 5) {
				continue;
			}
			Paixing paixing = dao.getPaixing();
			if (Paixing.wulong.equals(paixing)) {
				if (wulongCandidates.size() > 10) {
					continue;
				}
				wulongCandidates.add(dao);
			} else if (Paixing.duizi.equals(paixing)) {
				if (duiziCandidates.size() > 10) {
					continue;
				}
				duiziCandidates.add(dao);
			} else if (Paixing.liangdui.equals(paixing)) {
				if (liangduiCandidates.size() > 10) {
					continue;
				}
				liangduiCandidates.add(dao);
			} else if (Paixing.santiao.equals(paixing)) {
				if (santiaoCandidates.size() > 10) {
					continue;
				}
				santiaoCandidates.add(dao);
			} else if (Paixing.shunzi.equals(paixing)) {
				if (shunziCandidates.size() > 10) {
					continue;
				}
				shunziCandidates.add(dao);
			} else if (Paixing.tonghua.equals(paixing)) {
				if (tonghuaCandidates.size() > 10) {
					continue;
				}
				tonghuaCandidates.add(dao);
			} else if (Paixing.hulu.equals(paixing)) {
				if (huluCandidates.size() > 10) {
					continue;
				}
				huluCandidates.add(dao);
			} else if (Paixing.tiezhi.equals(paixing)) {
				if (tiezhiCandidates.size() > 10) {
					continue;
				}
				tiezhiCandidates.add(dao);
			} else if (Paixing.tonghuashun.equals(paixing)) {
				if (tonghuashunCandidates.size() > 10) {
					continue;
				}
				tonghuashunCandidates.add(dao);
			} else if (Paixing.wumei.equals(paixing)) {
				if (wumeiCandidates.size() > 10) {
					continue;
				}
				wumeiCandidates.add(dao);
			}
		}
		System.out.println("总数：" + daoList.size());
	}
	// }
}
