package com.study.chatserver.chat.api;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.study.chatserver.chat.api.dto.MessageDto;
import com.study.chatserver.chat.application.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StompController {

	private final SimpMessageSendingOperations messageTemplate;
	private final ChatService chatService;

	// 방법 1. MessageMapping + SendTo (정적 브로커 경로 설정)
	// @MessageMapping("/{roomId}")	// 클라이언트가 /publish/{roomId}로 메시지 발행
	// @SendTo("/topic/{roomId}")		// 구독 중인 클라이언트에게 /topic/{roomId}로 메시지 전달
	// public String sendMessage(@DestinationVariable Long roomId, String message) {
	// 	log.info("message: {}", message);
	// 	return message;
	// }

	// 방법 2. MessageMapping + SimpMessageSendingOperations (동적 브로커 경로 설정, 유연한 제어 가능)
	@MessageMapping("/{roomId}")
	public void sendMessage(@DestinationVariable Long roomId, MessageDto messageDto) {
		log.info("message: {}", messageDto.message());
		chatService.saveMessage(roomId, messageDto);
		messageTemplate.convertAndSend("/topic/" + roomId, messageDto);
	}
}
