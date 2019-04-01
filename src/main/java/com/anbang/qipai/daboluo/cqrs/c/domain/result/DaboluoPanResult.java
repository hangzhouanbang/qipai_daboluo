package com.anbang.qipai.daboluo.cqrs.c.domain.result;

import java.util.List;

import com.dml.shisanshui.pan.PanResult;

public class DaboluoPanResult extends PanResult {

	private List<DaboluoPanPlayerResult> panPlayerResultList;

	public List<DaboluoPanPlayerResult> getPanPlayerResultList() {
		return panPlayerResultList;
	}

	public void setPanPlayerResultList(List<DaboluoPanPlayerResult> panPlayerResultList) {
		this.panPlayerResultList = panPlayerResultList;
	}

}
