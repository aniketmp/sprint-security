package org.aniket.authentication.provider;

import org.aniket.authentication.type.AccessTokenBasedAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class TokenBasedAuthenticationProvider implements AuthenticationProvider{

	
	@Value("${api.accessToken}")
	private String accessToken;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		AccessTokenBasedAuthenticationToken auth=(AccessTokenBasedAuthenticationToken) authentication;
		String authToken=auth.getAuthenticationToken();
		if(authToken==null)
		{
			return null; //In this case this cannot decide the authentication.
		}
		String authKey=auth.getPrincipal().toString();
		if(accessToken.equals(authToken))
		{
			UserDetails userDetails=userDetailsService.loadUserByUsername(authKey);
			Authentication authenticatedUser=new AccessTokenBasedAuthenticationToken(userDetails, null,userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			return authenticatedUser;
		}
		throw new BadCredentialsException("API token not matching!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return AccessTokenBasedAuthenticationToken.class.equals(authentication);
	}

}
