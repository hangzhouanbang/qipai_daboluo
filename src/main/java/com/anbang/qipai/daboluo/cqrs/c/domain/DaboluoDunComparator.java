package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.List;

import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.Dun;
import com.dml.shisanshui.pai.paixing.Paixing;
import com.dml.shisanshui.pai.paixing.comparator.DunComparator;

/**
 * 五枚（百变才有）＞一条龙＞同花顺>铁支>葫芦>同花>顺子>三条>两对>对子>乌龙. 同一道牌型相同,则比点数,不比花色.
 * 若创建房间选了比花色，则先比牌型，牌型相同比点数，点数相同比花色.
 * 
 * @author lsc
 *
 */
public class DaboluoDunComparator implements DunComparator {

	private boolean bihuase;

	@Override
	public int compare(Dun dun1, Dun dun2) {
		Paixing paixing1 = dun1.getPaixing();
		Paixing paixing2 = dun2.getPaixing();
		int compare = paixing1.compareTo(paixing2);
		if (compare == 0) {
			List<PukePai> sortedList1 = dun1.getPukePaiList();
			List<PukePai> sortedList2 = dun2.getPukePaiList();
			int length = sortedList1.size() > sortedList2.size() ? sortedList2.size() : sortedList1.size();
			int i = 0;
			while (i < length) {
				int c = sortedList1.get(i).getPaiMian().dianShu().compareTo(sortedList2.get(i).getPaiMian().dianShu());
				if (c == 0 && bihuase) {
					return sortedList1.get(i).getPaiMian().huaSe().compareTo(sortedList2.get(i).getPaiMian().huaSe());
				}
				return c;
			}
		}
		return compare;
	}

	public boolean isBihuase() {
		return bihuase;
	}

	public void setBihuase(boolean bihuase) {
		this.bihuase = bihuase;
	}

}
