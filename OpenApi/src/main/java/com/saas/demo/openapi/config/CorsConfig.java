package com.saas.demo.openapi.config;

import com.saas.demo.openapi.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * @author jb28755
 * @date 2017/11/13.
 */
@Configuration
public class CorsConfig {

    @Bean
    public FilterRegistrationBean filterCors() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(corsFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    public Filter corsFilter() {
        return new CorsFilter();
    }
}
