package com.study.chatserver.chat.api.dto.request;

public record MessageReqDto(
	String message,
	String sender
) {
}
