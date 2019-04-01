package com.anbang.qipai.daboluo.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.daboluo.msg.channel.DaboluoResultSource;
import com.anbang.qipai.daboluo.msg.msjobj.CommonMO;
import com.anbang.qipai.daboluo.msg.msjobj.PukeHistoricalJuResult;
import com.anbang.qipai.daboluo.msg.msjobj.PukeHistoricalPanResult;

@EnableBinding(DaboluoResultSource.class)
public class DaboluoResultMsgService {

	@Autowired
	private DaboluoResultSource daboluoResultSource;

	public void recordJuResult(PukeHistoricalJuResult juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("doudizhu ju result");
		mo.setData(juResult);
		daboluoResultSource.daboluoResult().send(MessageBuilder.withPayload(mo).build());
	}

	public void recordPanResult(PukeHistoricalPanResult panResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("doudizhu pan result");
		mo.setData(panResult);
		daboluoResultSource.daboluoResult().send(MessageBuilder.withPayload(mo).build());
	}
}
