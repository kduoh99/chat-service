package com.study.chatserver.notification.application;

import java.util.List;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.chatserver.notification.api.dto.response.UnreadCountResDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationPublisher {

	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;

	public void publish(String emitterId, List<UnreadCountResDto> counts) {
		try {
			String payload = objectMapper.writeValueAsString(counts);
			stringRedisTemplate.convertAndSend("sse:" + emitterId, payload);
			log.info("Redis Publish - emitterId: {}, message published", emitterId);
		} catch (JsonProcessingException e) {
			log.error("Redis Publish Fail - emitterId: {}, error: {}", emitterId, e.toString());
		}
	}
}
