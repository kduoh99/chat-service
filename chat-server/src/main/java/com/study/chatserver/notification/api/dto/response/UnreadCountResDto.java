package com.study.chatserver.notification.api.dto.response;

import lombok.Builder;

@Builder
public record UnreadCountResDto(
	Long roomId,
	Long count
) {
	public static UnreadCountResDto of(Long roomId, Long count) {
		return UnreadCountResDto.builder()
			.roomId(roomId)
			.count(count)
			.build();
	}
}
