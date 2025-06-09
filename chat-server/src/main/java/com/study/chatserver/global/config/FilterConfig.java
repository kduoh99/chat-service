package com.study.chatserver.global.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.study.chatserver.global.filter.RequestLoggingFilter;

@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
		FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(new RequestLoggingFilter());
		registration.setOrder(1);
		registration.addUrlPatterns("/*");
		return registration;
	}
}
