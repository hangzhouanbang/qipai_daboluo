package com.anbang.qipai.daboluo.web.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.shisanshui.pai.paixing.Dao;
import com.dml.shisanshui.pai.paixing.PaixingSolution;
import com.dml.shisanshui.player.ShisanshuiPlayerValueObject;
import com.dml.shisanshui.position.Position;

public class DaboluoPlayerValueObjectVO {
	private String id;
	private Position position;
	private DaboluoPlayerShoupaiVO allShoupai;
	/**
	 * 玩家出牌方案
	 */
	private Map<String, Dao> chupaiSolutionCandidates = new HashMap<>();
	/**
	 * 玩家出牌提示
	 */
	private List<PaixingSolution> chupaiSolutionForTips = new ArrayList<>();

	/**
	 * 最终出牌
	 */
	private PaixingSolution chupaiSolution;

	public DaboluoPlayerValueObjectVO() {

	}

	public DaboluoPlayerValueObjectVO(ShisanshuiPlayerValueObject shisanshuiPlayerValueObject) {
		id = shisanshuiPlayerValueObject.getId();
		position = shisanshuiPlayerValueObject.getPosition();
		allShoupai = new DaboluoPlayerShoupaiVO(shisanshuiPlayerValueObject.getAllShoupai(),
				shisanshuiPlayerValueObject.getTotalShoupai());
		chupaiSolutionCandidates.putAll(shisanshuiPlayerValueObject.getChupaiSolutionCandidates());
		chupaiSolutionForTips.addAll(shisanshuiPlayerValueObject.getChupaiSolutionForTips());
		chupaiSolution = shisanshuiPlayerValueObject.getChupaiSolution();
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

	public Map<String, Dao> getChupaiSolutionCandidates() {
		return chupaiSolutionCandidates;
	}

	public void setChupaiSolutionCandidates(Map<String, Dao> chupaiSolutionCandidates) {
		this.chupaiSolutionCandidates = chupaiSolutionCandidates;
	}

	public List<PaixingSolution> getChupaiSolutionForTips() {
		return chupaiSolutionForTips;
	}

	public void setChupaiSolutionForTips(List<PaixingSolution> chupaiSolutionForTips) {
		this.chupaiSolutionForTips = chupaiSolutionForTips;
	}

	public PaixingSolution getChupaiSolution() {
		return chupaiSolution;
	}

	public void setChupaiSolution(PaixingSolution chupaiSolution) {
		this.chupaiSolution = chupaiSolution;
	}

}
