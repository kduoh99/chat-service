package com.study.chatserver.chat.api.dto;

import lombok.Builder;

@Builder
public record MessageDto(
	Long roomId,
	String message,
	String sender
) {
	public static MessageDto of(String message, String sender) {
		return MessageDto.builder()
			.message(message)
			.sender(sender)
			.build();
	}

	public static MessageDto of(Long roomId, String message, String sender) {
		return MessageDto.builder()
			.roomId(roomId)
			.message(message)
			.sender(sender)
			.build();
	}
}
