package com.example.community.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String POST_UPLOAD_DIR = System.getProperty("user.dir") + "/upload-posts/";
    private static final String PROFILE_UPLOAD_DIR = System.getProperty("user.dir") + "/upload-images/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image-server/posts/**")
                .addResourceLocations("file:" + POST_UPLOAD_DIR);
        registry.addResourceHandler("/image-server/users/**")
                .addResourceLocations("file:" + PROFILE_UPLOAD_DIR);
    }
}
