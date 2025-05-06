package com.study.chatserver.notification.domain.repository;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface EmitterRepository {

	void save(Long memberId, SseEmitter emitter);
	SseEmitter get(Long memberId);
	void delete(Long memberId);
}
