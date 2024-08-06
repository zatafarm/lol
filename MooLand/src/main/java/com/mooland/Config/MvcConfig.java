package com.mooland.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**")
        .addResourceLocations("classpath:/static/bootstrap/");

        registry.addResourceHandler("/js/**")
        .addResourceLocations("classpath:/static/js/");
    }
}