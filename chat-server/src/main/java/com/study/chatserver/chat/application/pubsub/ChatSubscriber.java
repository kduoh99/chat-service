package com.study.chatserver.chat.application.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.chatserver.chat.api.dto.MessageDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSubscriber implements MessageListener {

	private final SimpMessageSendingOperations messageTemplate;
	private final ObjectMapper objectMapper;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String payload = new String(message.getBody());

		try {
			MessageDto messageDto = objectMapper.readValue(payload, MessageDto.class);
			messageTemplate.convertAndSend("/topic/" + messageDto.roomId(), messageDto);
			log.info("Redis Subscribe - roomId: {}, message delivered", messageDto.roomId());
		} catch (JsonProcessingException e) {
			log.error("Redis Subscribe Fail - error: {}", e.toString());
		}
	}
}
