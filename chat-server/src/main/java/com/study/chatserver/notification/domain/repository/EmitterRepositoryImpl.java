package com.study.chatserver.notification.domain.repository;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

	private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

	@Override
	public void save(Long memberId, SseEmitter emitter) {
		emitters.put(memberId, emitter);
	}

	@Override
	public SseEmitter get(Long memberId) {
		return emitters.get(memberId);
	}

	@Override
	public void delete(Long memberId) {
		emitters.remove(memberId);
	}
}
