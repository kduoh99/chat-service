package com.study.chatserver.chat.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class StompController {

	@MessageMapping("/{roomId}")	// 클라이언트가 /publish/{roomId}로 메시지 발행
	@SendTo("/topic/{roomId}")		// 구독 중인 클라이언트에게 /topic/{roomId}로 메시지 전달
	public String sendMessage(@DestinationVariable Long roomId, String message) {
		log.info("message: {}", message);
		return message;
	}
}
