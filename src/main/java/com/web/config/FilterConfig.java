package com.web.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RoleAuthorizationFilter> roleFilter() {
        FilterRegistrationBean<RoleAuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RoleAuthorizationFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}