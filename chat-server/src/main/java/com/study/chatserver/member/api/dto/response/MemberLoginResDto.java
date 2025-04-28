package com.study.chatserver.member.api.dto.response;

import lombok.Builder;

@Builder
public record MemberLoginResDto(
	Long memberId,
	String accessToken
) {
	public static MemberLoginResDto of(Long memberId, String accessToken) {
		return MemberLoginResDto.builder()
			.memberId(memberId)
			.accessToken(accessToken)
			.build();
	}
}
