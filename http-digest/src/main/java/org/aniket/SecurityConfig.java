package org.aniket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
	
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("aniket").password("aniket").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("admin").roles("ADMIN").build());
        manager.createUser(User.withUsername("api").password("").roles("API").build());
        manager.createUser(User.withUsername("apotek.no").password("").roles("PHARMACY").build());
        return manager;
    }  
  
    @Bean
    public PasswordEncoder passwordEncoder(){
    	PasswordEncoder encoder = NoOpPasswordEncoder.getInstance();
        return encoder;
    }
    
    
    @Bean
    DigestAuthenticationEntryPoint digestEntryPoint() {
        DigestAuthenticationEntryPoint bauth = new DigestAuthenticationEntryPoint();
        bauth.setRealmName("Digest WF Realm");
        bauth.setKey("MySecureKey");
        return bauth;
    }
    @Bean
    DigestAuthenticationFilter digestAuthenticationFilter() throws Exception {
        DigestAuthenticationFilter digestAuthenticationFilter = new DigestAuthenticationFilter();
        digestAuthenticationFilter.setUserDetailsService(userDetailsService());
        digestAuthenticationFilter.setAuthenticationEntryPoint(digestEntryPoint());        
        return digestAuthenticationFilter;
    }   
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
	   http.addFilter(digestAuthenticationFilter())              			   // register digest entry point
          .exceptionHandling().authenticationEntryPoint(digestEntryPoint());       // on exception ask for digest authentication
		
		http.authorizeRequests()
         	.antMatchers(HttpMethod.GET, "/hello").permitAll()
         	.anyRequest().authenticated();
		
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	


}
