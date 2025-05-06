package com.study.chatserver.notification.domain.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepositoryImpl implements EmitterRepository {

	private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

	@Override
	public void save(String emitterId, SseEmitter emitter) {
		emitters.put(emitterId, emitter);
	}

	@Override
	public void deleteById(String emitterId) {
		emitters.remove(emitterId);
	}

	@Override
	public SseEmitter findById(String emitterId) {
		return emitters.get(emitterId);
	}
}
