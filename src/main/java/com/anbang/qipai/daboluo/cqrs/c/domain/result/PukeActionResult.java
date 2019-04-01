package com.anbang.qipai.daboluo.cqrs.c.domain.result;

import com.anbang.qipai.daboluo.cqrs.c.domain.PukeGameValueObject;
import com.dml.shisanshui.pan.PanActionFrame;

public class PukeActionResult {
	private PukeGameValueObject pukeGame;
	private PanActionFrame panActionFrame;
	private DaboluoPanResult panResult;
	private DaboluoJuResult juResult;

	public PukeGameValueObject getPukeGame() {
		return pukeGame;
	}

	public void setPukeGame(PukeGameValueObject pukeGame) {
		this.pukeGame = pukeGame;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public DaboluoPanResult getPanResult() {
		return panResult;
	}

	public void setPanResult(DaboluoPanResult panResult) {
		this.panResult = panResult;
	}

	public DaboluoJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(DaboluoJuResult juResult) {
		this.juResult = juResult;
	}

}
