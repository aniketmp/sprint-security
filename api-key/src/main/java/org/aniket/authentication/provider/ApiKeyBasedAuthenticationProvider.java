package org.aniket.authentication.provider;

import org.aniket.authentication.type.ApiKeyBasedAuthenticationToken;
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
public class ApiKeyBasedAuthenticationProvider implements AuthenticationProvider{

	
	@Value("${api.key}")
	private String apiKey;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		ApiKeyBasedAuthenticationToken auth=(ApiKeyBasedAuthenticationToken) authentication;
		String userProvidedKey=auth.getApiKey();
		if(userProvidedKey==null)
		{
			return null; //In this case this cannot decide the authentication.
		}
		String userId=auth.getPrincipal().toString();
		if(apiKey.equals(userProvidedKey))
		{
			UserDetails userDetails=userDetailsService.loadUserByUsername(userId);
			Authentication authenticatedUser=new ApiKeyBasedAuthenticationToken(userDetails, null,userDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
			return authenticatedUser;
		}
		throw new BadCredentialsException("API key is not matching!");
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return ApiKeyBasedAuthenticationToken.class.equals(authentication);
	}

}
