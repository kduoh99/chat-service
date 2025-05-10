package com.study.chatserver.auth.application.oauth;

import com.study.chatserver.member.domain.SocialType;

public interface OAuthService {
	SocialType support();
	String getAccessToken(String code);
	String[] getMemberInfo(String accessToken);
}
