package com.mooland.Config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpSession;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/entry/**").hasAnyRole("BJ", "ADMIN")
                    .requestMatchers("/bjregit/**").hasAnyRole("BJ", "ADMIN")
                    .requestMatchers("/update/**").hasAnyRole("ADMIN")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/boardregit").hasRole("ADMIN")
                    .requestMatchers("/board/update").hasRole("ADMIN")
                    .requestMatchers("/detail/delete").hasRole("ADMIN")
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
            )
            .logout(auth -> auth
                    .logoutUrl("/logout")
                    .addLogoutHandler((request, response, authentication) -> {
                    	HttpSession session = request.getSession();
                        session.invalidate();
                    })
                    .logoutSuccessHandler((request, response, authentication) ->
                    response.sendRedirect("/home"))
                    .deleteCookies("JSESSIONID", "remember-me") 
                )
            .formLogin(auth -> auth
                .loginPage("/login")
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginProcessingUrl("/loginProc")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(new MyAccessDeniedHandler())
            );
        	

        return http.build();
    }
	@Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
        		.requestMatchers("/bootstrap/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
