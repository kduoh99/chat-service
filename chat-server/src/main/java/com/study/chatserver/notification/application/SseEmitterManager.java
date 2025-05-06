package com.study.chatserver.notification.application;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.study.chatserver.notification.api.dto.response.UnreadCountResDto;
import com.study.chatserver.notification.domain.repository.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SseEmitterManager {

	private final EmitterRepository emitterRepository;

	public void send(Long memberId, String email, List<UnreadCountResDto> counts) {
		SseEmitter emitter = emitterRepository.get(memberId);

		if (emitter == null) {
			log.info("SSE Skip - to: {}, no active emitter", email);
			return;
		}

		try {
			emitter.send(SseEmitter.event()
				.name("unread-message")
				.data(counts));
			log.info("SSE Send - to: {}, unread update", email);
		} catch (IOException e) {
			emitterRepository.delete(memberId);
			log.warn("SSE Error - to: {}, error: {}", email, e.toString());
		}
	}
}
