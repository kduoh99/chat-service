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

	public void send(String emitterId, List<UnreadCountResDto> counts) {
		SseEmitter emitter = emitterRepository.findById(emitterId);

		if (emitter == null) {
			log.info("SSE Skip - emitterId: {}, no active emitter", emitterId);
			return;
		}

		try {
			emitter.send(SseEmitter.event().name("unread-message").data(counts));
			log.info("SSE Send - emitterId: {}, unread update", emitterId);
		} catch (IOException e) {
			emitterRepository.deleteById(emitterId);
			log.warn("SSE Error - emitterId: {}, error: {}", emitterId, e.toString());
		}
	}
}
