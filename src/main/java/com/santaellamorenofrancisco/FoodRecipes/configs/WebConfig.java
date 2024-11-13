package com.santaellamorenofrancisco.FoodRecipes.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Aplica CORS a todas las rutas
                .allowedOrigins("https://tu-frontend-en-railway.app")  // Cambia esto por el dominio de tu frontend en Railway
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
                .allowedHeaders("*")  // Cabeceras permitidas
                .allowCredentials(true); // Permitir cookies y autenticación de sesión
    }
}