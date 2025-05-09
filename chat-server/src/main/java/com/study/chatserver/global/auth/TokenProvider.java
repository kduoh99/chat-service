package com.study.chatserver.global.auth;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.study.chatserver.global.auth.dto.TokenDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	public TokenDto generateToken(String email, String role) {
		String accessToken = generateAccessToken(email, role);

		return TokenDto.from(accessToken);
	}

	private String generateAccessToken(String email, String role) {
		Date now = new Date();

		return Jwts.builder()
			.setSubject(email)
			.claim("role", role)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + accessTokenExpireTime * 60 * 1000L))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token);

			return true;
		} catch (ExpiredJwtException e) {
			log.error("JWT expired");
		} catch (SignatureException e) {
			log.error("Invalid JWT signature");
		} catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
			log.error("Invalid JWT format");
		} catch (Exception e) {
			log.error("JWT validation error", e);
		}

		return false;
	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		List<GrantedAuthority> authorities = List.of(
			new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class)));
		UserDetails userDetails = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
	}

	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
