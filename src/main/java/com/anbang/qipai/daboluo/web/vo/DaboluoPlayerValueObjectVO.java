package com.anbang.qipai.daboluo.web.vo;

import java.util.List;

import com.dml.shisanshui.pai.PukePai;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;
import com.dml.shisanshui.position.Position;

public class DaboluoPlayerValueObjectVO {
	private String id;
	private Position position;
	private DaboluoPlayerShoupaiVO allShoupai;
	private List<List<Integer>> shoupaiIdListForSortList;
	/**
	 * 玩家对子出牌方案
	 */
	private List<List<PukePai>> duiziCandidates;
	/**
	 * 玩家两对出牌方案
	 */
	private List<List<PukePai>> liangduiCandidates;
	/**
	 * 玩家三条出牌方案
	 */
	private List<List<PukePai>> santiaoCandidates;
	/**
	 * 玩家顺子出牌方案
	 */
	private List<List<PukePai>> shunziCandidates;
	/**
	 * 玩家同花出牌方案
	 */
	private List<List<PukePai>> tonghuaCandidates;
	/**
	 * 玩家葫芦出牌方案
	 */
	private List<List<PukePai>> huluCandidates;
	/**
	 * 玩家铁支出牌方案
	 */
	private List<List<PukePai>> tiezhiCandidates;
	/**
	 * 玩家同花顺出牌方案
	 */
	private List<List<PukePai>> tonghuashunCandidates;
	/**
	 * 玩家五枚出牌方案
	 */
	private List<List<PukePai>> wumeiCandidates;
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

	public List<List<PukePai>> getDuiziCandidates() {
		return duiziCandidates;
	}

	public void setDuiziCandidates(List<List<PukePai>> duiziCandidates) {
		this.duiziCandidates = duiziCandidates;
	}

	public List<List<PukePai>> getLiangduiCandidates() {
		return liangduiCandidates;
	}

	public void setLiangduiCandidates(List<List<PukePai>> liangduiCandidates) {
		this.liangduiCandidates = liangduiCandidates;
	}

	public List<List<PukePai>> getSantiaoCandidates() {
		return santiaoCandidates;
	}

	public void setSantiaoCandidates(List<List<PukePai>> santiaoCandidates) {
		this.santiaoCandidates = santiaoCandidates;
	}

	public List<List<PukePai>> getShunziCandidates() {
		return shunziCandidates;
	}

	public void setShunziCandidates(List<List<PukePai>> shunziCandidates) {
		this.shunziCandidates = shunziCandidates;
	}

	public List<List<PukePai>> getTonghuaCandidates() {
		return tonghuaCandidates;
	}

	public void setTonghuaCandidates(List<List<PukePai>> tonghuaCandidates) {
		this.tonghuaCandidates = tonghuaCandidates;
	}

	public List<List<PukePai>> getHuluCandidates() {
		return huluCandidates;
	}

	public void setHuluCandidates(List<List<PukePai>> huluCandidates) {
		this.huluCandidates = huluCandidates;
	}

	public List<List<PukePai>> getTiezhiCandidates() {
		return tiezhiCandidates;
	}

	public void setTiezhiCandidates(List<List<PukePai>> tiezhiCandidates) {
		this.tiezhiCandidates = tiezhiCandidates;
	}

	public List<List<PukePai>> getTonghuashunCandidates() {
		return tonghuashunCandidates;
	}

	public void setTonghuashunCandidates(List<List<PukePai>> tonghuashunCandidates) {
		this.tonghuashunCandidates = tonghuashunCandidates;
	}

	public List<List<PukePai>> getWumeiCandidates() {
		return wumeiCandidates;
	}

	public void setWumeiCandidates(List<List<PukePai>> wumeiCandidates) {
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
