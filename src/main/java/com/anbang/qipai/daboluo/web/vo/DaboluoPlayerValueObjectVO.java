package com.anbang.qipai.daboluo.web.vo;

import java.util.List;

import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;
import com.dml.shisanshui.position.Position;

public class DaboluoPlayerValueObjectVO {
	private String id;
	private Position position;
	private DaboluoPlayerShoupaiVO allShoupai;
	private List<List<Integer>> shoupaiIdListForSortList;
	/**
	 * 玩家乌龙出牌方案
	 */
	private List<Dao> wulongCandidates;
	/**
	 * 玩家对子出牌方案
	 */
	private List<Dao> duiziCandidates;
	/**
	 * 玩家两对出牌方案
	 */
	private List<Dao> liangduiCandidates;
	/**
	 * 玩家三条出牌方案
	 */
	private List<Dao> santiaoCandidates;
	/**
	 * 玩家顺子出牌方案
	 */
	private List<Dao> shunziCandidates;
	/**
	 * 玩家同花出牌方案
	 */
	private List<Dao> tonghuaCandidates;
	/**
	 * 玩家葫芦出牌方案
	 */
	private List<Dao> huluCandidates;
	/**
	 * 玩家铁支出牌方案
	 */
	private List<Dao> tiezhiCandidates;
	/**
	 * 玩家同花顺出牌方案
	 */
	private List<Dao> tonghuashunCandidates;
	/**
	 * 玩家五枚出牌方案
	 */
	private List<Dao> wumeiCandidates;
	/**
	 * 玩家出牌提示
	 */
	private List<PaixingSolution> chupaiSolutionForTips;

	/**
	 * 是否出牌
	 */
	private boolean chupai;

	public DaboluoPlayerValueObjectVO() {

	}

	public DaboluoPlayerValueObjectVO(ShisanshuiPlayerValueObject shisanshuiPlayerValueObject) {
		id = shisanshuiPlayerValueObject.getId();
		position = shisanshuiPlayerValueObject.getPosition();
		shoupaiIdListForSortList = shisanshuiPlayerValueObject.getShoupaiIdListForSortList();
		if (shoupaiIdListForSortList == null || shoupaiIdListForSortList.isEmpty()) {
			allShoupai = new DaboluoPlayerShoupaiVO(shisanshuiPlayerValueObject.getAllShoupai(),
					shisanshuiPlayerValueObject.getTotalShoupai(), null);
		} else {
			allShoupai = new DaboluoPlayerShoupaiVO(shisanshuiPlayerValueObject.getAllShoupai(),
					shisanshuiPlayerValueObject.getTotalShoupai(), shoupaiIdListForSortList.get(0));
		}
		wulongCandidates = shisanshuiPlayerValueObject.getWulongCandidates();
		duiziCandidates = shisanshuiPlayerValueObject.getDuiziCandidates();
		liangduiCandidates = shisanshuiPlayerValueObject.getLiangduiCandidates();
		santiaoCandidates = shisanshuiPlayerValueObject.getSantiaoCandidates();
		shunziCandidates = shisanshuiPlayerValueObject.getShunziCandidates();
		tonghuaCandidates = shisanshuiPlayerValueObject.getTonghuaCandidates();
		huluCandidates = shisanshuiPlayerValueObject.getHuluCandidates();
		tiezhiCandidates = shisanshuiPlayerValueObject.getTiezhiCandidates();
		tonghuashunCandidates = shisanshuiPlayerValueObject.getTonghuashunCandidates();
		wumeiCandidates = shisanshuiPlayerValueObject.getWumeiCandidates();
		chupaiSolutionForTips = shisanshuiPlayerValueObject.getChupaiSolutionForTips();
		chupai = shisanshuiPlayerValueObject.getChupaiSolution() != null;
	}

	public List<List<Integer>> getShoupaiIdListForSortList() {
		return shoupaiIdListForSortList;
	}

	public void setShoupaiIdListForSortList(List<List<Integer>> shoupaiIdListForSortList) {
		this.shoupaiIdListForSortList = shoupaiIdListForSortList;
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

	public List<Dao> getWulongCandidates() {
		return wulongCandidates;
	}

	public void setWulongCandidates(List<Dao> wulongCandidates) {
		this.wulongCandidates = wulongCandidates;
	}

	public List<Dao> getDuiziCandidates() {
		return duiziCandidates;
	}

	public void setDuiziCandidates(List<Dao> duiziCandidates) {
		this.duiziCandidates = duiziCandidates;
	}

	public List<Dao> getLiangduiCandidates() {
		return liangduiCandidates;
	}

	public void setLiangduiCandidates(List<Dao> liangduiCandidates) {
		this.liangduiCandidates = liangduiCandidates;
	}

	public List<Dao> getSantiaoCandidates() {
		return santiaoCandidates;
	}

	public void setSantiaoCandidates(List<Dao> santiaoCandidates) {
		this.santiaoCandidates = santiaoCandidates;
	}

	public List<Dao> getShunziCandidates() {
		return shunziCandidates;
	}

	public void setShunziCandidates(List<Dao> shunziCandidates) {
		this.shunziCandidates = shunziCandidates;
	}

	public List<Dao> getTonghuaCandidates() {
		return tonghuaCandidates;
	}

	public void setTonghuaCandidates(List<Dao> tonghuaCandidates) {
		this.tonghuaCandidates = tonghuaCandidates;
	}

	public List<Dao> getHuluCandidates() {
		return huluCandidates;
	}

	public void setHuluCandidates(List<Dao> huluCandidates) {
		this.huluCandidates = huluCandidates;
	}

	public List<Dao> getTiezhiCandidates() {
		return tiezhiCandidates;
	}

	public void setTiezhiCandidates(List<Dao> tiezhiCandidates) {
		this.tiezhiCandidates = tiezhiCandidates;
	}

	public List<Dao> getTonghuashunCandidates() {
		return tonghuashunCandidates;
	}

	public void setTonghuashunCandidates(List<Dao> tonghuashunCandidates) {
		this.tonghuashunCandidates = tonghuashunCandidates;
	}

	public List<Dao> getWumeiCandidates() {
		return wumeiCandidates;
	}

	public void setWumeiCandidates(List<Dao> wumeiCandidates) {
		this.wumeiCandidates = wumeiCandidates;
	}

	public List<PaixingSolution> getChupaiSolutionForTips() {
		return chupaiSolutionForTips;
	}

	public void setChupaiSolutionForTips(List<PaixingSolution> chupaiSolutionForTips) {
		this.chupaiSolutionForTips = chupaiSolutionForTips;
	}

	public boolean isChupai() {
		return chupai;
	}

	public void setChupai(boolean chupai) {
		this.chupai = chupai;
	}

}
