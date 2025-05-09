package com.study.chatserver.auth.api.dto.response;

import lombok.Builder;

@Builder
public record LoginResDto(
	Long memberId,
	String accessToken
) {
	public static LoginResDto of(Long memberId, String accessToken) {
		return LoginResDto.builder()
			.memberId(memberId)
			.accessToken(accessToken)
			.build();
	}
}
