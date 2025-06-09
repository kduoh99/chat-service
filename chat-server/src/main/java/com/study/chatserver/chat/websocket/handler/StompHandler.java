package com.study.chatserver.chat.websocket.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import com.study.chatserver.chat.application.ChatService;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	@Value("${jwt.secret}")
	private String secret;

	private final ChatService chatService;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

		if (StompCommand.CONNECT == accessor.getCommand()) {
			log.info("Connect - Token validation started");
			String token = accessor.getFirstNativeHeader("Authorization").substring(7);

			Jwts.parserBuilder()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
				.getBody();

			log.info("Connect - Token validation completed");
		}

		if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
			log.info("Subscribe - Token validation started");
			String token = accessor.getFirstNativeHeader("Authorization").substring(7);
			String email = Jwts.parserBuilder()
				.setSigningKey(secret)
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();

			String roomId = accessor.getDestination().split("/")[2];
			if (!chatService.isRoomParticipant(email, Long.parseLong(roomId))) {
				throw new AuthenticationServiceException("해당 채팅방에 권한이 없습니다.");
			}

			log.info("Subscribe - Token validation completed");
		}

		return message;
	}
}
