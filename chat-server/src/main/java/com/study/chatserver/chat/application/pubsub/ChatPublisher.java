package com.study.chatserver.chat.application.pubsub;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatPublisher {

	private final StringRedisTemplate stringRedisTemplate;

	public void publish(String channel, String payload) {
		stringRedisTemplate.convertAndSend(channel, payload);
		log.info("Redis Publish - channel: {}, message published", channel);
	}
}
