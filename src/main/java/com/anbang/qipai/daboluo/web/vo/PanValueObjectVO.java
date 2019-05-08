package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.dml.shisanshui.pai.PaiListValueObject;
import com.dml.shisanshui.pan.PanValueObject;

public class PanValueObjectVO {
	private int no;
	private List<DaboluoPlayerValueObjectVO> daboluoPlayerList;
	private PaiListValueObject paiListValueObject;

	public PanValueObjectVO() {

	}

	public PanValueObjectVO(PanValueObject panValueObject) {
		no = panValueObject.getNo();
		daboluoPlayerList = new ArrayList<>();
		panValueObject.getPlayerList()
				.forEach((doudizhuPlayer) -> daboluoPlayerList.add(new DaboluoPlayerValueObjectVO(doudizhuPlayer)));
		paiListValueObject = panValueObject.getPaiListValueObject();
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<DaboluoPlayerValueObjectVO> getDaboluoPlayerList() {
		return daboluoPlayerList;
	}

	public void setDaboluoPlayerList(List<DaboluoPlayerValueObjectVO> daboluoPlayerList) {
		this.daboluoPlayerList = daboluoPlayerList;
	}

	public PaiListValueObject getPaiListValueObject() {
		return paiListValueObject;
	}

	public void setPaiListValueObject(PaiListValueObject paiListValueObject) {
		this.paiListValueObject = paiListValueObject;
	}

}
