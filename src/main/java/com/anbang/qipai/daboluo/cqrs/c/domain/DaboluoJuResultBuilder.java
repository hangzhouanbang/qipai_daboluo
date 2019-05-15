package com.anbang.qipai.daboluo.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuPlayerResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoJuResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanPlayerResult;
import com.anbang.qipai.daboluo.cqrs.c.domain.result.DaboluoPanResult;
import com.dml.shisanshui.ju.Ju;
import com.dml.shisanshui.ju.JuResult;
import com.dml.shisanshui.ju.JuResultBuilder;
import com.dml.shisanshui.pan.PanResult;

public class DaboluoJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		DaboluoJuResult juResult = new DaboluoJuResult();
		juResult.setFinishedPanCount(ju.countFinishedPan());
		if (ju.countFinishedPan() > 0) {
			Map<String, DaboluoJuPlayerResult> juPlayerResultMap = new HashMap<>();
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				DaboluoPanResult doudizhuPanResult = (DaboluoPanResult) panResult;
				for (DaboluoPanPlayerResult panPlayerResult : doudizhuPanResult.getPanPlayerResultList()) {
					DaboluoJuPlayerResult juPlayerResult = juPlayerResultMap.get(panPlayerResult.getPlayerId());
					if (juPlayerResult == null) {
						juPlayerResult = new DaboluoJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					if (panPlayerResult.getJiesuanScore().hasTeshupaixing()) {
						juPlayerResult.increaseTspx();
					}
					if (panPlayerResult.getJiesuanScore().isQuanleida()) {
						juPlayerResult.increaseQld();
					}
					juPlayerResult.increaseTotalScore(panPlayerResult.getJiesuanScore().getValue());
				}
			}

			DaboluoJuPlayerResult dayingjia = null;
			DaboluoJuPlayerResult datuhao = null;
			for (DaboluoJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			juResult.setDatuhaoId(datuhao.getPlayerId());
			juResult.setDayingjiaId(dayingjia.getPlayerId());
			juResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return juResult;
	}

}
