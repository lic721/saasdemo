package com.saas.demo.openapi.config;

import com.saas.demo.openapi.filter.GLogIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author jb28755
 * @date 2017/11/13.
 */
@Configuration
public class AutoGLogIdConfig {

    @Bean
    public FilterRegistrationBean<Filter> filterGLogIdConfig() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new GLogIdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

}
