package com.google.calendar.app.config;

import com.google.calendar.app.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author mir00r on 24/9/20
 * @project IntelliJ IDEA
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/",
                        Constants.LOGIN_URL
                )
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login().loginPage(Constants.LOGIN_URL)
                .defaultSuccessUrl(Constants.EVENT_URL)
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher(Constants.LOGOUT_URL))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .permitAll();
    }
}
