package com.study.chatserver.chat.application;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.chatserver.chat.api.dto.MessageDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisPubSubService implements MessageListener {

	private final StringRedisTemplate stringRedisTemplate;
	private final SimpMessageSendingOperations messageTemplate;
	private final ObjectMapper objectMapper;

	public void publish(String channel, String message) {
		stringRedisTemplate.convertAndSend(channel, message);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String payload = new String(message.getBody());

		try {
			MessageDto messageDto = objectMapper.readValue(payload, MessageDto.class);
			messageTemplate.convertAndSend("/topic/" + messageDto.roomId(), messageDto);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
