package com.anbang.qipai.daboluo.web.vo;

public class PanActionFrameVO {
	private int no;
	private PanValueObjectVO panAfterAction;
	private long actionTime;

	public PanActionFrameVO() {

	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public PanValueObjectVO getPanAfterAction() {
		return panAfterAction;
	}

	public void setPanAfterAction(PanValueObjectVO panAfterAction) {
		this.panAfterAction = panAfterAction;
	}

	public long getActionTime() {
		return actionTime;
	}

	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

}
