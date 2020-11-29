package org.aniket.authentication.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aniket.authentication.type.AccessTokenBasedAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter{

	@Autowired
	AuthenticationManager manager;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		String accessKey=request.getHeader("access-key");
		String accessToken=request.getHeader("access-token");
		if(accessKey == null || accessToken == null)
		{
			filterChain.doFilter(request, response);
			return;
		}
		AccessTokenBasedAuthenticationToken authentication=new AccessTokenBasedAuthenticationToken(accessKey,accessToken);
		Authentication auth=manager.authenticate(authentication);
		if(auth.isAuthenticated())
		{
			SecurityContextHolder.getContext().setAuthentication(auth);	
		}
		
		filterChain.doFilter(request, response);
	}
}
