package com.anbang.qipai.daboluo.web.vo;

import java.util.List;

import com.dml.shisanshui.pai.PaiListValueObject;
import com.dml.shisanshui.position.Position;

public class PanValueObjectVO {
	private int no;
	private List<DaboluoPlayerValueObjectVO> doudizhuPlayerList;
	private PaiListValueObject paiListValueObject;
	private String dizhuPlayerId;// 地主id
	private Position actionPosition;
	private String latestDapaiPlayerId;
	private int rangPai;

	public PanValueObjectVO() {

	}

	public String getDizhuPlayerId() {
		return dizhuPlayerId;
	}

	public void setDizhuPlayerId(String dizhuPlayerId) {
		this.dizhuPlayerId = dizhuPlayerId;
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

	public Position getActionPosition() {
		return actionPosition;
	}

	public void setActionPosition(Position actionPosition) {
		this.actionPosition = actionPosition;
	}

	public String getLatestDapaiPlayerId() {
		return latestDapaiPlayerId;
	}

	public void setLatestDapaiPlayerId(String latestDapaiPlayerId) {
		this.latestDapaiPlayerId = latestDapaiPlayerId;
	}

	public int getRangPai() {
		return rangPai;
	}

	public void setRangPai(int rangPai) {
		this.rangPai = rangPai;
	}

}
