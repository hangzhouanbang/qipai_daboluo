package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.dml.shisanshui.pai.HuaSe;
import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.PukePaiMian;
import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.Paixing;

/**
 * 牌型编码计算器
 * 
 * @author lsc
 *
 */
public class DaoTypeCodeCalculator {

	public static void calculateDaoTypeCode(Dao dao, boolean bihuase, BianXingWanFa bx) {
		List<PukePaiMian> paiMianList = new ArrayList<>();
		List<PukePai> wangpaiList = new ArrayList<>();
		for (PukePai pukePai : dao.getPukePaiList()) {
			if (pukePai.getPaiMian().dianShu().ordinal() > 12) {
				wangpaiList.add(pukePai);
			} else {
				paiMianList.add(pukePai.getPaiMian());
			}
		}
		if (wangpaiList.size() > 0 && BianXingWanFa.baibian.equals(bx)) {
			calculateDaoTypeCodeWithWangpai(dao, paiMianList, wangpaiList, bihuase);
		} else {
			calculateDaoTypeCodeWithoutWangpai(dao, paiMianList, bihuase);
		}
	}

	private static void calculateDaoTypeCodeWithoutWangpai(Dao dao, List<PukePaiMian> paiMianList, boolean bihuase) {
		long typeCode = 0;
		Paixing paixing = calculatePaixing(paiMianList);
		typeCode = paixing.ordinal();
		Collections.sort(paiMianList, new Comparator<PukePaiMian>() {
			@Override
			public int compare(PukePaiMian o1, PukePaiMian o2) {
				// TODO Auto-generated method stub
				return o2.compareTo(o1);
			}
		});
		for (PukePaiMian paimian : paiMianList) {
			typeCode = typeCode << 4;
			typeCode += paimian.dianShu().ordinal();
			typeCode = typeCode << 2;
			if (bihuase) {
				typeCode += paimian.huaSe().ordinal();
			}
		}
		// 如果是三张牌末尾补零，因为有“相公”，所以取最大值
		if (paiMianList.size() == 3) {
			typeCode = (typeCode + 1) << 12 - 1;
		}
		dao.setTypeCode(typeCode);
		dao.setPaixing(paixing);
	}

	private static void calculateDaoTypeCodeWithWangpai(Dao dao, List<PukePaiMian> paiMianList,
			List<PukePai> wangpaiList, boolean bihuase) {
		long maxTypeCode = 0;
		Paixing bestPaixing = Paixing.wulong;
		int wangpaiCount = wangpaiList.size();
		int maxZuheCode = (int) Math.pow(52, wangpaiCount);
		int[] modArray = new int[wangpaiCount];
		for (int i = 0; i < wangpaiCount; i++) {
			modArray[i] = (int) Math.pow(52, wangpaiCount - 1 - i);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			WangjiesuanPai[] wangpaiDangPaiArray = new WangjiesuanPai[wangpaiCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int i = 0; i < wangpaiCount; i++) {
				int mod = modArray[i];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {
					int yu = temp % mod;
					wangpaiDangPaiArray[i] = new WangjiesuanPai(wangpaiList.get(i).getPaiMian(),
							PukePaiMian.values()[shang]);
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					wangpaiDangPaiArray = null;
					break;
				}
			}
			if (wangpaiDangPaiArray != null) {
				long typeCode = 0;
				List<PukePaiMian> pukePaiList = new ArrayList<>(paiMianList);
				for (WangjiesuanPai jiesuanPai : wangpaiDangPaiArray) {
					pukePaiList.add(jiesuanPai.getJiesuanpai());
				}
				Paixing paixing = calculatePaixing(pukePaiList);
				typeCode = paixing.ordinal();
				Collections.sort(pukePaiList, new Comparator<PukePaiMian>() {
					@Override
					public int compare(PukePaiMian o1, PukePaiMian o2) {
						// TODO Auto-generated method stub
						return o2.compareTo(o1);
					}
				});
				for (PukePaiMian paimian : pukePaiList) {
					typeCode = typeCode << 4;
					typeCode += paimian.dianShu().ordinal();
					typeCode = typeCode << 2;
					if (bihuase) {
						typeCode += paimian.huaSe().ordinal();
					}
				}
				// 如果是三张牌末尾补零，因为有“相公”，所以取最大值
				if (pukePaiList.size() == 3) {
					typeCode = (typeCode + 1) << 12 - 1;
				}
				if (typeCode > maxTypeCode) {
					maxTypeCode = typeCode;
					bestPaixing = paixing;
				}
			}
		}
		dao.setTypeCode(maxTypeCode);
		dao.setPaixing(bestPaixing);
	}

	private static Paixing calculatePaixing(List<PukePaiMian> pukePaiList) {
		int[] dianshuArray = new int[13];
		for (PukePaiMian paimian : pukePaiList) {
			dianshuArray[paimian.dianShu().ordinal()]++;
		}
		boolean tonghua = isTonghua(pukePaiList);
		boolean shunzi = isShunzi(dianshuArray);
		int tongDianshu = countMaxDianshu(dianshuArray);
		int duizi = countDuizi(dianshuArray);
		Paixing paixing = Paixing.wulong;
		if (tongDianshu == 5) {
			paixing = Paixing.wumei;
		} else if (tonghua && shunzi) {
			paixing = Paixing.tonghuashun;
		} else if (tongDianshu == 4) {
			paixing = Paixing.tiezhi;
		} else if (tongDianshu == 3 && duizi == 2) {
			paixing = Paixing.hulu;
		} else if (tonghua) {
			paixing = Paixing.tonghua;
		} else if (shunzi) {
			paixing = Paixing.shunzi;
		} else if (tongDianshu == 3) {
			paixing = Paixing.santiao;
		} else if (duizi == 2) {
			paixing = Paixing.liangdui;
		} else if (duizi == 1) {
			paixing = Paixing.duizi;
		} else {
			paixing = Paixing.wulong;
		}
		return paixing;
	}

	private static boolean isTonghua(List<PukePaiMian> pukePaiList) {
		HuaSe huase = pukePaiList.get(0).huaSe();
		for (PukePaiMian paimian : pukePaiList) {
			if (!huase.equals(paimian.huaSe())) {
				return false;
			}
		}
		return true;
	}

	private static boolean isShunzi(int[] dianshuArray) {
		int firstIndex = 0;
		while (true) {
			if (dianshuArray[firstIndex] > 0) {
				break;
			}
			firstIndex++;
			if (firstIndex > dianshuArray.length) {
				break;
			}
		}
		int lianxuCount = 0;
		for (int i = 0; i + firstIndex < dianshuArray.length; i++) {
			if (dianshuArray[i + firstIndex] > 0) {
				lianxuCount++;
			} else {
				break;
			}
		}
		if (lianxuCount == 5) {
			return true;
		} else if (firstIndex == 0 && lianxuCount == 4 && dianshuArray[dianshuArray.length - 1] > 0) {
			return true;
		}
		return false;
	}

	private static int countMaxDianshu(int[] dianshuArray) {
		int max = 0;
		for (int i : dianshuArray) {
			if (i > max) {
				max = i;
			}
		}
		return max;
	}

	private static int countDuizi(int[] dianshuArray) {
		int duiziCount = 0;
		for (int i : dianshuArray) {
			if (i > 1) {
				duiziCount++;
			}
		}
		return duiziCount;
	}
}
