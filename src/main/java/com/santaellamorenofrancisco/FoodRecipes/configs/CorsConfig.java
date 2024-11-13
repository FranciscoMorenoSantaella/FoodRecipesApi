package com.santaellamorenofrancisco.FoodRecipes.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // Configuración de CORS
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // Permite enviar credenciales (como cookies)
        config.addAllowedOrigin("*");      // Permite cualquier origen (esto se puede personalizar)
        config.addAllowedHeader("*");      // Permite cualquier encabezado
        config.addAllowedMethod("*");      // Permite todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)

        // Registra la configuración CORS para rutas específicas (por ejemplo, /v2/api-docs)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/v2/api-docs", config); // Swagger API docs
        source.registerCorsConfiguration("/**", config); // Habilita CORS globalmente para todas las rutas

        return new CorsFilter(source);
    }
}
