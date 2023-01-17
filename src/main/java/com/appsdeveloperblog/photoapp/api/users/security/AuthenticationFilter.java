package com.appsdeveloperblog.photoapp.api.users.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.appsdeveloperblog.photoapp.api.users.service.UsersService;
import com.appsdeveloperblog.photoapp.api.users.shared.UserDto;
import com.appsdeveloperblog.photoapp.api.users.ui.model.LoginRequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private UsersService usersService;
	private Environment environment;
	
	
	
	public AuthenticationFilter(UsersService usersService, Environment environment,AuthenticationManager authenticationManager ) {
		super();
		this.usersService = usersService;
		this.environment = environment;
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException{
		try {
		LoginRequestModel creds =new ObjectMapper()
				.readValue(req.getInputStream(), LoginRequestModel.class);
		System.out.println("input credentials "+req.getInputStream()  +"   "+ creds.getEmail() +" "+ creds.getPassword());
		return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
				creds.getEmail(), 
				creds.getPassword(),
				new ArrayList<>())
				);
	}
		catch (IOException e) {
			throw new RuntimeException();
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req,
			                                HttpServletResponse res,
			                                FilterChain chain,
			                                Authentication auth) {
		 String userName= ((User) auth.getPrincipal()).getUsername();
		 UserDto userDetails = usersService.getUserDetailsByEmail(userName);
		 System.out.println("userId "+ userDetails.getUserId());
		 String token = Jwts.builder()
				 .setSubject(userDetails.getUserId())
				 .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
				 .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
				 .compact();
		 System.out.println("token :" + token);
		 res.addHeader("token", token);
		 res.addHeader("userId", userDetails.getUserId());
	}
	
	
}
