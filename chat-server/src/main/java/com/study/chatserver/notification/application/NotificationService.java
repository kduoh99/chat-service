package com.study.chatserver.notification.application;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.study.chatserver.member.domain.Member;
import com.study.chatserver.member.domain.repository.MemberRepository;
import com.study.chatserver.notification.domain.repository.EmitterRepository;

import jakarta.persistence.EntityNotFoundException;
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
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

		Long memberId = member.getId();
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

		emitter.onCompletion(() -> {
			emitterRepository.delete(memberId);
			log.info("SSE Complete - to: {}", email);
		});

		emitter.onTimeout(() -> {
			emitterRepository.delete(memberId);
			log.info("SSE Timeout - to: {}", email);
		});

		emitter.onError(e -> {
			emitterRepository.delete(memberId);
			log.warn("SSE Error - to: {}, error: {}", email, e.toString());
		});

		emitterRepository.save(memberId, emitter);

		try {
			emitter.send(SseEmitter.event().name("connect").data("connected"));
			log.info("Subscribe - to: {}, connected", email);
		} catch (IOException e) {
			emitterRepository.delete(memberId);
			log.warn("Subscribe Fail - to: {}, error: {}", email, e.toString());
		}

		return emitter;
	}
}
