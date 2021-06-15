package com.vrpconsulting.heroku2sf;

import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Configuration
@PropertySource("classpath:application-oauth2.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Override
    protected void configure( final HttpSecurity http ) throws Exception {
        http.authorizeRequests()
                .antMatchers("/oauth_login",
                             "/loginFailure",
                             "/",
                             "/webjars/**",
                             "/favicon.ico",
                             "/static/**")
                .permitAll()
                .anyRequest()
                .authenticated()
            .and()
                .sessionManagement()
                .sessionCreationPolicy( SessionCreationPolicy.IF_REQUIRED )
            .and()
                .oauth2Login()
                .loginPage("/oauth_login")
                .authorizationEndpoint()
                .baseUri( "/oauth2/authorize-client" )
                .authorizationRequestRepository( authorizationRequestRepository() )
            .and()
                .tokenEndpoint()
                .accessTokenResponseClient( accessTokenResponseClient() )
            .and()
                .defaultSuccessUrl( "/loginSuccess" )
                .failureUrl( "/loginFailure" )
            .and()
                .logout()
                .logoutUrl( "/logout" )
                .logoutSuccessUrl("/");
    }
    
    @Bean
    public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }
    
    @Bean
    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final var oidcUserService = new OidcUserService();
        oidcUserService.setAccessibleScopes( Set.of() );
        return oidcUserService;
    }
    
    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        return new DefaultAuthorizationCodeTokenResponseClient();
    }
}