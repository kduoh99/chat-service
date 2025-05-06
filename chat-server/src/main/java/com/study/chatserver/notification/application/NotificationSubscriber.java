package com.study.chatserver.notification.application;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.chatserver.notification.api.dto.response.UnreadCountResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationSubscriber implements MessageListener {

	private final ObjectMapper objectMapper;
	private final SseEmitterManager emitterManager;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String channel = new String(message.getChannel());
			String emitterId = channel.split(":")[1];

			String raw = new String(message.getBody());
			List<UnreadCountResDto> counts = objectMapper.readValue(raw, new TypeReference<>() {});

			emitterManager.send(emitterId, counts);
		} catch (IOException | ArrayIndexOutOfBoundsException e) {
			log.error("Redis Subscribe Fail - error: {}", e.toString());
		}
	}
}
