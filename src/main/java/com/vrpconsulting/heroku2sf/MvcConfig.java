package com.vrpconsulting.heroku2sf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers( final ViewControllerRegistry registry ) {
        registry.addViewController( "securedPage1" );
        registry.addViewController( "securedPage2" );
        registry.addViewController( "loginFailure" );
    }
}
