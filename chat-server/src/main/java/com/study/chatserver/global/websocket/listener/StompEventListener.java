package com.study.chatserver.global.websocket.listener;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import lombok.extern.slf4j.Slf4j;

// Stomp 연결/해제 이벤트를 감지하여 세션 수 추적
@Slf4j
@Component
public class StompEventListener {

	private final Set<String> sessions = ConcurrentHashMap.newKeySet();

	@EventListener
	public void handleConnect(SessionConnectEvent event) {
		String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
		sessions.add(sessionId);
		log.info("Connect - sessionId: {}, total: {}", sessionId, sessions.size());
	}

	@EventListener
	public void handleDisconnect(SessionDisconnectEvent event) {
		String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
		sessions.remove(sessionId);
		log.info("Disconnect - sessionId: {}, total: {}", sessionId, sessions.size());
	}
}
