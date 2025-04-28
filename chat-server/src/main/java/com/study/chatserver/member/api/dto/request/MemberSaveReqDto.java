package com.study.chatserver.member.api.dto.request;

public record MemberSaveReqDto(
	String name,
	String email,
	String password
) {
}
