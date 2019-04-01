package com.anbang.qipai.daboluo.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.daboluo.msg.channel.WatchRecordSource;
import com.anbang.qipai.daboluo.msg.msjobj.CommonMO;
import com.dml.mpgame.game.watch.WatchRecord;

/**
 * @author yins
 * @Description: 观战记录消息发送
 */
@EnableBinding(WatchRecordSource.class)
public class WatchRecordMsgService {
	@Autowired
	private WatchRecordSource watchRecordSource;

	public void joinWatch(WatchRecord watchRecord) {
		CommonMO mo = new CommonMO();
		mo.setMsg("joinWatch");
		mo.setData(watchRecord);
		watchRecordSource.watchRecordSink().send(MessageBuilder.withPayload(mo).build());
	}

	public void leaveWatch(WatchRecord watchRecord) {
		CommonMO mo = new CommonMO();
		mo.setMsg("leaveWatch");
		mo.setData(watchRecord);
		watchRecordSource.watchRecordSink().send(MessageBuilder.withPayload(mo).build());
	}
}
