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
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestLoggingFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {

		try {
			HttpServletRequest httpRequest = (HttpServletRequest)request;

			MDC.put("path", httpRequest.getRequestURI());

			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			MDC.put("userEmail", email);

			String hostname = InetAddress.getLocalHost().getHostName();
			MDC.put("hostname", hostname);

			chain.doFilter(request, response);
		} finally {
			MDC.clear();
		}
	}
}
