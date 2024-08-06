package com.mooland.Config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void  addResourceHandlers(ResourceHandlerRegistry registry) {
    	
        registry.addResourceHandler("/bjimg/**") // --1
        .addResourceLocations("file:///home/ubuntu/bjimg/"); //--2
        //.addResourceLocations("file:C://img/"); 
    }

}