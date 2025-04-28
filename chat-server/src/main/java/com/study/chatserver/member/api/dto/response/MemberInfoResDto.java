package com.study.chatserver.member.api.dto.response;

import lombok.Builder;

@Builder
public record MemberInfoResDto(
	Long memberId,
	String name,
	String email
) {
	public static MemberInfoResDto of(Long memberId, String name, String email) {
		return MemberInfoResDto.builder()
			.memberId(memberId)
			.name(name)
			.email(email)
			.build();
	}
}
