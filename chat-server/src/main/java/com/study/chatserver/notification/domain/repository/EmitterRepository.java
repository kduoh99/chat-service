package com.study.chatserver.notification.domain.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

	void save(String emitterId, SseEmitter emitter);

	void deleteById(String emitterId);

	SseEmitter findById(String emitterId);
}
