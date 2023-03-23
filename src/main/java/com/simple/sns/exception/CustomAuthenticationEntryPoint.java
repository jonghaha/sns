package com.simple.sns.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.simple.sns.controller.response.Response;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json");
		response.setStatus(ErrorCode.INVALID_TOKEN.getStatus().value());
		response.getWriter().write(Response.error(ErrorCode.INVALID_TOKEN.name()).toStream());
	}
}
