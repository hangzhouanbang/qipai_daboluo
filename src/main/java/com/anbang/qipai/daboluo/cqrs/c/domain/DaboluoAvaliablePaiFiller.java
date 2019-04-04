package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.dml.shisanshui.ju.Ju;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.PukePaiMian;
import com.dml.shisanshui.preparedapai.avaliablepai.AvaliablePaiFiller;

public class DaboluoAvaliablePaiFiller implements AvaliablePaiFiller {

	private BianXingWanFa bx;

	@Override
	public void fillAvaliablePai(Ju ju) throws Exception {
		List<PukePai> allPaiList = new ArrayList<>();
		Set<PukePaiMian> removeSet = new HashSet<>();
		if (!BianXingWanFa.baibian.equals(bx)) {
			removeSet.add(PukePaiMian.xiaowang);
			removeSet.add(PukePaiMian.dawang);
		}
		// 生成一副牌
		int id = 0;
		for (PukePaiMian paiType : PukePaiMian.values()) {
			if (removeSet.contains(paiType)) {
				continue;
			}
			PukePai pai = new PukePai();
			pai.setId(id);
			pai.setPaiMian(paiType);
			allPaiList.add(pai);
			id++;
		}
		ju.getCurrentPan().setAvaliablePaiList(allPaiList);
	}

	public BianXingWanFa getBx() {
		return bx;
	}

	public void setBx(BianXingWanFa bx) {
		this.bx = bx;
	}

}
