package com.study.chatserver.auth.application.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.chatserver.member.domain.SocialType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoogleOAuthService implements OAuthService {

	private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
	private static final String PROFILE_URI = "https://openidconnect.googleapis.com/v1/userinfo";

	private final RestClient restClient;
	private final ObjectMapper objectMapper;

	@Value("${oauth.google.client-id}")
	private String clientId;

	@Value("${oauth.google.client-secret}")
	private String clientSecret;

	@Value("${oauth.google.redirect-uri}")
	private String redirectUri;

	@Override
	public SocialType support() {
		return SocialType.GOOGLE;
	}

	@Override
	public String getAccessToken(String code) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("client_id", clientId);
		params.add("client_secret", clientSecret);
		params.add("redirect_uri", redirectUri);
		params.add("code", code);
		params.add("grant_type", "authorization_code");

		String response = restClient.post()
			.uri(TOKEN_URI)
			.header("Content-Type", "application/x-www-form-urlencoded")
			.body(params)
			.retrieve()
			.body(String.class);

		try {
			return objectMapper.readTree(response).path("access_token").asText();
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String[] getMemberInfo(String accessToken) {
		String response = restClient.get()
			.uri(PROFILE_URI)
			.header("Authorization", "Bearer " + accessToken)
			.retrieve()
			.body(String.class);

		try {
			JsonNode jsonNode = objectMapper.readTree(response);
			return new String[] {
				jsonNode.path("sub").asText(),
				jsonNode.path("email").asText(),
				jsonNode.path("name").asText()
			};
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
