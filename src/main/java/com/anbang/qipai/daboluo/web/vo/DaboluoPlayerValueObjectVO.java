package com.anbang.qipai.daboluo.web.vo;

import java.util.List;

import com.dml.shisanshui.position.Position;

public class DaboluoPlayerValueObjectVO {
	private String id;
	private Position position;
	private DaboluoPlayerShoupaiVO allShoupai;
	private int[] shoupaiDianShuAmountArray;
	private List<List<Integer>> shoupaiIdListForSortList;
	private boolean guo;
	private boolean watingForMe = false;
	private boolean noPaiWarning;// 结束警报
	private int rangPai;

	public DaboluoPlayerValueObjectVO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public DaboluoPlayerShoupaiVO getAllShoupai() {
		return allShoupai;
	}

	public void setAllShoupai(DaboluoPlayerShoupaiVO allShoupai) {
		this.allShoupai = allShoupai;
	}

	public int[] getShoupaiDianShuAmountArray() {
		return shoupaiDianShuAmountArray;
	}

	public void setShoupaiDianShuAmountArray(int[] shoupaiDianShuAmountArray) {
		this.shoupaiDianShuAmountArray = shoupaiDianShuAmountArray;
	}

	public List<List<Integer>> getShoupaiIdListForSortList() {
		return shoupaiIdListForSortList;
	}

	public void setShoupaiIdListForSortList(List<List<Integer>> shoupaiIdListForSortList) {
		this.shoupaiIdListForSortList = shoupaiIdListForSortList;
	}

	public boolean isGuo() {
		return guo;
	}

	public void setGuo(boolean guo) {
		this.guo = guo;
	}

	public boolean isWatingForMe() {
		return watingForMe;
	}

	public void setWatingForMe(boolean watingForMe) {
		this.watingForMe = watingForMe;
	}

	public boolean isNoPaiWarning() {
		return noPaiWarning;
	}

	public void setNoPaiWarning(boolean noPaiWarning) {
		this.noPaiWarning = noPaiWarning;
	}

	public int getRangPai() {
		return rangPai;
	}

	public void setRangPai(int rangPai) {
		this.rangPai = rangPai;
	}

}
