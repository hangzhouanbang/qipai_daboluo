package com.anbang.qipai.daboluo.cqrs.c.service.disruptor;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.highto.framework.disruptor.event.CommandEvent;
import com.highto.framework.disruptor.event.factory.CommandEventFactory;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 跟据业务产生并持有不同的disruptor
 * 
 * @author neo
 *
 */
@Component
public class DisruptorFactory {

	private Disruptor<CommandEvent> coreCmdDisruptor;

	@Autowired
	private ProcessCoreCommandEventHandler processCoreCommandEventHandler;

	@PostConstruct
	public void initForCore() {
		coreCmdDisruptor = new Disruptor<CommandEvent>(new CommandEventFactory(), 1024 * 1024,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI, new SleepingWaitStrategy());
		coreCmdDisruptor.handleEventsWith(processCoreCommandEventHandler);
		coreCmdDisruptor.start();

	}

	/**
	 * 核心相关命令处理的disruptor
	 * 
	 * @return
	 */
	public Disruptor<CommandEvent> getCoreCmdDisruptor() {
		return coreCmdDisruptor;
	}

}
