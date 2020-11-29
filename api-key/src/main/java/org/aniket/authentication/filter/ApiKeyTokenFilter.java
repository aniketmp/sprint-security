package org.aniket.authentication.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aniket.authentication.type.ApiKeyBasedAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiKeyTokenFilter extends OncePerRequestFilter{

	@Autowired
	AuthenticationManager manager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String userId=request.getHeader("user-id");
		String apiKey=request.getHeader("api-key");
		
		if(userId == null || apiKey == null)
		{
			filterChain.doFilter(request, response);
			return;
		}
		ApiKeyBasedAuthenticationToken authentication=new ApiKeyBasedAuthenticationToken(userId,apiKey);
		Authentication auth=manager.authenticate(authentication);
		if(auth.isAuthenticated())
		{
			SecurityContextHolder.getContext().setAuthentication(auth);	
		}
		
		filterChain.doFilter(request, response);
	}
}
