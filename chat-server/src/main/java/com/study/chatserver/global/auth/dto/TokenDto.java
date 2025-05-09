package com.study.chatserver.global.auth.dto;

import lombok.Builder;

@Builder
public record TokenDto(
	String accessToken
) {
	public static TokenDto from(String accessToken) {
		return TokenDto.builder()
			.accessToken(accessToken)
			.build();
	}
}
