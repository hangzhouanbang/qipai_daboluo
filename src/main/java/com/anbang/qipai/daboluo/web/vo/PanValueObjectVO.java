package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.dml.shisanshui.pai.PaiListValueObject;
import com.dml.shisanshui.pan.PanValueObject;

public class PanValueObjectVO {
	private int no;
	private List<DaboluoPlayerValueObjectVO> doudizhuPlayerList;
	private PaiListValueObject paiListValueObject;

	public PanValueObjectVO() {

	}

	public PanValueObjectVO(PanValueObject panValueObject) {
		no = panValueObject.getNo();
		doudizhuPlayerList = new ArrayList<>();
		panValueObject.getPlayerList()
				.forEach((doudizhuPlayer) -> doudizhuPlayerList.add(new DaboluoPlayerValueObjectVO(doudizhuPlayer)));
		paiListValueObject = panValueObject.getPaiListValueObject();
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<DaboluoPlayerValueObjectVO> getDoudizhuPlayerList() {
		return doudizhuPlayerList;
	}

	public void setDoudizhuPlayerList(List<DaboluoPlayerValueObjectVO> doudizhuPlayerList) {
		this.doudizhuPlayerList = doudizhuPlayerList;
	}

	public PaiListValueObject getPaiListValueObject() {
		return paiListValueObject;
	}

	public void setPaiListValueObject(PaiListValueObject paiListValueObject) {
		this.paiListValueObject = paiListValueObject;
	}

}
