package com.e_messenger.code.config.cloud;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary(@Value("${cloud.config.url}") String cloudinaryUrl){
        return new Cloudinary(cloudinaryUrl);
    }
}
