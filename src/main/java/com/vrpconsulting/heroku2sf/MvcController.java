package com.vrpconsulting.heroku2sf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class MvcController {
    
    private final Logger logger = LoggerFactory.getLogger( MvcController.class );
    
    public static final String OAUTH_LOGIN_PAGE = "oauth_login";
    public static final String LOGIN_SUCCESS_PAGE = "loginSuccess";
    public static final String INDEX_PAGE = "index";
    
    private final InMemoryClientRegistrationRepository clientRegistrationRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;
    
    public MvcController( final InMemoryClientRegistrationRepository clientRegistrationRepository,
                          final OAuth2AuthorizedClientService authorizedClientService ) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizedClientService = authorizedClientService;
    }
    
    @RequestMapping( "/" )
    public String indexPage() {
        return INDEX_PAGE;
    }
    
    @GetMapping("/oauth_login")
    public String getLoginPage() {
        logger.info( "Supported providers: {}", StreamSupport.stream( clientRegistrationRepository.spliterator(), false )
                                                             .map( ClientRegistration::getClientName )
                                                             .collect( Collectors.joining( "," ) ) );
        return OAUTH_LOGIN_PAGE;
    }
    
    @GetMapping("/loginSuccess")
    public String getLoginSuccessPage( final Model model,
                                       final OAuth2AuthenticationToken authentication ) {
        final var client = authorizedClientService.loadAuthorizedClient( authentication.getAuthorizedClientRegistrationId(), authentication.getName() );
        logger.info( "Oauth token from oauth2client{} : {}", client.getClientRegistration().getClientName(), client.getAccessToken().getTokenValue() );
        model.addAllAttributes( requestDataFromOAuthProvider( client ) );
        return LOGIN_SUCCESS_PAGE;
    }
    
    private Map<String, Object> requestDataFromOAuthProvider( final OAuth2AuthorizedClient client ) {
        final var userInfoEndpointUri = client.getClientRegistration()
                                              .getProviderDetails()
                                              .getUserInfoEndpoint()
                                              .getUri();
        final var restTemplate = new RestTemplate();
        final var headers = new HttpHeaders();
        headers.add( HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken().getTokenValue() );
        final var entity = new HttpEntity<String>( headers );
        final var response = restTemplate.exchange( userInfoEndpointUri,
                                                    HttpMethod.GET,
                                                    entity,
                                                    Map.class );
        final var userAttributes = response.getBody();
        logger.info( "Data fetched from oauth provider is: {}", userAttributes );
        return Map.of( "name", userAttributes.get( "name" ) );
    }
}