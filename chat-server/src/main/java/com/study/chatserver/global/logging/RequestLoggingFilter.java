package com.study.chatserver.global.logging;

import java.io.IOException;
import java.net.InetAddress;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		try {
			HttpServletRequest httpRequest = (HttpServletRequest)request;

			String path = httpRequest.getRequestURI();
			String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			String hostname = InetAddress.getLocalHost().getHostName();

			MDC.put("path", path);
			MDC.put("userEmail", userEmail);
			MDC.put("hostname", hostname);

			log.info("Request - path={}, user={}, host={}", path, userEmail, hostname);

			chain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}
}
