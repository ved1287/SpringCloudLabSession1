package com.zensar.security.filters;
import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zensar.jwt.JwtProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;
	
	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		String username = request.getParameter("username");
		String password= request.getParameter("password");
		log.info("user: {}, pass: {}", username, password);
		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authentication) throws IOException, ServletException {
		User user = (User)authentication.getPrincipal();
		setJwtProvider(request);
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		String access_token = jwtProvider.jwtCreateAccessToken(user, algorithm, request);
		
		String refresh_token = jwtProvider.jwtCreateRefreshToken(user, algorithm, request);
		Map<String, String> tokens = jwtProvider.createResponse(access_token, refresh_token);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		//converts the tokens into JSON format and getOutputStream is used for sending the response in the character format
		new ObjectMapper().writeValue(response.getOutputStream(), tokens);
	}
	
	private void setJwtProvider(HttpServletRequest request) {
		ServletContext sc = request.getServletContext();
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(sc);
		jwtProvider = wc.getBean(JwtProvider.class);
	}
}
