package com.study.chatserver.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/connect")
			.setAllowedOrigins("http://localhost:3000")
			.withSockJS();	// SockJS 라이브러리를 통해 HTTP 프로토콜에서도 WebSocket 연결 가능
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// 클라이언트가 메시지를 발행할 prefix 설정 (ex. /publish/{roomId})
		registry.setApplicationDestinationPrefixes("/publish");

		// 메시지를 구독할 prefix 설정 (ex. /topic/{roomId})
		registry.enableSimpleBroker("/topic");
	}
}
