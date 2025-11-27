package com.Tangana.microserviciosTangana.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // aplica a todos los endpoints
                .allowedOrigins("http://localhost:5173") // tu frontend Vite
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")      // permite cualquier header (incluye X-User-Id)
                .exposedHeaders("*")
                .allowCredentials(true);
    }
}
