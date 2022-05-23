package com.zensar.jwt;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.event.OnUserLogoutSuccessEvent;
import com.exception.InvalidTokenRequestException;
import com.service.interfaces.LoginService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
public class JwtProvider {
private LoggedOutJwtTokenCache loggedOutJwtTokenCache;
	
	public void setLoggedOutJwtTokenCache(LoggedOutJwtTokenCache loggedOutJwtTokenCache) {
		this.loggedOutJwtTokenCache = loggedOutJwtTokenCache;
	}
	
	public Date getTokenExpiryFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey("secret".getBytes())
				.parseClaimsJws(token)
				.getBody();
		return claims.getExpiration();
	}
	
	public void validateTokenIsNotForALoggedOutDevice(String authToken) {
		OnUserLogoutSuccessEvent previouslyLoggedOutEvent = loggedOutJwtTokenCache.getLogoutEventForToken(authToken);
	    if (previouslyLoggedOutEvent != null) {
	        String userEmail = previouslyLoggedOutEvent.getUserEmail();
	        Date logoutEventDate = previouslyLoggedOutEvent.getEventTime();
	        String errorMessage = String.format("Token corresponds to an already logged out user [%s] at [%s]. Please login again", userEmail, logoutEventDate);
	        throw new InvalidTokenRequestException("JWT", authToken, errorMessage);
	    }
	}

	public String jwtCreateAccessToken(User user, Algorithm algorithm, HttpServletRequest request) {
		String access_token = JWT.create()
							.withSubject(user.getUsername())
							.withExpiresAt(new Date(System.currentTimeMillis() + 5*60*1000))//for 5 minutes 5*60*1000 or is for 5 sec 5*1000 
							.withIssuer(request.getRequestURL().toString())
							.withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
							.sign(algorithm);
		
		return access_token;
	}
	
	public String jwtCreateRefreshToken(User user, Algorithm algorithm, HttpServletRequest request) {
		String refresh_token = JWT.create()
							.withSubject(user.getUsername())
							.withExpiresAt(new Date(System.currentTimeMillis() + 20*60*1000))//for 20 minutes 20*60*1000 or is for 10sec 10*1000
							.withIssuer(request.getRequestURL().toString())
							.sign(algorithm);
		
		return refresh_token;
	}
	
	public Map<String, String> createResponse(String access_token, String refresh_token){ 
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", access_token);
		tokens.put("refresh_token", refresh_token);
		return tokens;
	}
	
//	public ResponseEntity<Map<String, String>> generateRefreshToken(String authorizationHeader, LoginService userService, HttpServletRequest request){
//		String refresh_token = authorizationHeader.substring("Bearer ".length());
//		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//		JWTVerifier verifier = JWT.require(algorithm).build();
//		DecodedJWT decodedJWT = verifier.verify(refresh_token);
//		String username = decodedJWT.getSubject();
//		User user = userService.loadUser(username);
////		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
////		authorities.add(new SimpleGrantedAuthority(appuser.getRole()));
////		User user = new User(email, password, authorities);
//		String access_token = jwtCreateAccessToken(user, algorithm, request);
//		Map<String, String> tokens = createResponse(access_token, refresh_token);
//		return ResponseEntity.ok(tokens);
//	}
	
	public ResponseEntity<Map<String, String>> generateRefreshToken(String refresh_token, LoginService userService, HttpServletRequest request){
		Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
		JWTVerifier verifier = JWT.require(algorithm).build();
		DecodedJWT decodedJWT = verifier.verify(refresh_token);
		String username = decodedJWT.getSubject();
		User user = userService.loadUser(username);
//		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//		authorities.add(new SimpleGrantedAuthority(appuser.getRole()));
//		User user = new User(email, password, authorities);
		String access_token = jwtCreateAccessToken(user, algorithm, request);
		Map<String, String> tokens = createResponse(access_token, refresh_token);
		return ResponseEntity.ok(tokens);
	}
	
	public Map<String, String> errorResponse(String key, String message){
		Map<String, String> res = new HashMap<>();
		res.put(key, message);
		return res;
	}
}
