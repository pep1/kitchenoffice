package com.pep1.kitchenoffice.webservice.authentication;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public final class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
	
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized access");
	}
	
}