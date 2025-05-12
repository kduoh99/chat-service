package com.study.chatserver.notification.application;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.repository.MemberRepository;
import com.study.chatserver.member.exception.MemberNotFoundException;
import com.study.chatserver.notification.domain.repository.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

	private final EmitterRepository emitterRepository;
	private final MemberRepository memberRepository;

	public SseEmitter subscribe() {
		Member member = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
			.orElseThrow(MemberNotFoundException::new);

		String emitterId = String.valueOf(member.getId());
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

		emitter.onCompletion(() -> {
			emitterRepository.deleteById(emitterId);
			log.info("SSE Complete - emitterId: {}", emitterId);
		});

		emitter.onTimeout(() -> {
			emitterRepository.deleteById(emitterId);
			log.info("SSE Timeout - emitterId: {}", emitterId);
		});

		emitter.onError(e -> {
			emitterRepository.deleteById(emitterId);
			log.warn("SSE Error - emitterId: {}, error: {}", emitterId, e.toString());
		});

		emitterRepository.save(emitterId, emitter);

		try {
			emitter.send(SseEmitter.event().name("connect").data("connected"));
			log.info("SSE Subscribe - emitterId: {}, connected", emitterId);
		} catch (IOException e) {
			emitterRepository.deleteById(emitterId);
			log.warn("SSE Subscribe Fail - emitterId: {}, error: {}", emitterId, e.toString());
		}

		return emitter;
	}
}
