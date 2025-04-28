package com.study.chatserver.member.api.dto.request;

public record MemberLoginReqDto(
	String email,
	String password
) {
}
