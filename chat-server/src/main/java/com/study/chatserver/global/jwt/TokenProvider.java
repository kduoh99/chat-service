package com.study.chatserver.global.jwt;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.study.chatserver.global.jwt.dto.TokenDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class TokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expire-time.access-token}")
	private int accessTokenExpireTime;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	public TokenDto generateToken(String memberId, String role) {
		String accessToken = generateAccessToken(memberId, role);

		return TokenDto.from(accessToken);
	}

	public String generateAccessToken(String memberId, String role) {
		Date now = new Date();

		return Jwts.builder()
			.setSubject(memberId)
			.claim("role", role)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + accessTokenExpireTime * 60 * 1000L))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}
}
