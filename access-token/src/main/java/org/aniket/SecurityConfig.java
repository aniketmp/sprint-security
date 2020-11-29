package org.aniket;

import org.aniket.authentication.filter.AuthenticationTokenFilter;
import org.aniket.authentication.provider.TokenBasedAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
    
    @Autowired
    private TokenBasedAuthenticationProvider tokenBasedAuthenticationProvider;
    
    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;
    
	
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
    @Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
    
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(tokenBasedAuthenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
         	.antMatchers(HttpMethod.GET, "/hello").permitAll()
         	.anyRequest().authenticated()
		.and()
			.addFilterBefore(authenticationTokenFilter, BasicAuthenticationFilter.class);
		
		http
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	


}
