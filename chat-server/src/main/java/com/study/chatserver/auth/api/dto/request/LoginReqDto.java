package com.study.chatserver.auth.api.dto.request;

public record LoginReqDto(
	String email,
	String password
) {
}
