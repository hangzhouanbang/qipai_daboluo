package com.anbang.qipai.daboluo.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String DABOLUOGAMEROOM = "daboluoGameRoom";

	@Input
	SubscribableChannel daboluoGameRoom();
}
