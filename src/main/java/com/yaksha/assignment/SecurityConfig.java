package com.yaksha.assignment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

	// Define the security filter chain to configure HTTP security
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				// Use AntPathRequestMatcher for all the URL patterns
				.requestMatchers(new AntPathRequestMatcher("/admin/**")).hasRole("ADMIN")
				.requestMatchers(new AntPathRequestMatcher("/user/**")).hasRole("USER")
				// Allow access to the H2 Console for development
				.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll().anyRequest().authenticated() // Any
																														// other
																														// request
																														// requires
																														// authentication
				.and().formLogin().permitAll() // Allow everyone to access the login page
				.and().httpBasic(); // Enable basic authentication

		return http.build();
	}

	// Define a PasswordEncoder bean for encoding passwords
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);

		authenticationManagerBuilder.inMemoryAuthentication().withUser("admin")
				.password(passwordEncoder().encode("admin")).roles("ADMIN").and().withUser("user")
				.password(passwordEncoder().encode("password")).roles("USER");

		return authenticationManagerBuilder.build();
	}

}
