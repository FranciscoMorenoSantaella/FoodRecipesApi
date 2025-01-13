package com.santaellamorenofrancisco.FoodRecipes.configs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Food Recipes API", 
        version = "1.0.0", 
        description = "API para la gestión de recetas de comida, ingredientes, y relaciones entre ellos. Permite crear, leer, actualizar y eliminar recetas y sus ingredientes asociados.", // Descripción detallada
        contact = @Contact(
            name = "santaellamorenofrancisco@gmail.com",
            email = "santaellamorenofrancisco@gmail.com"
        )
    )
)
public class SwaggerConfig {

    // Configuración de Swagger para la API general
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("food-recipes-api") // Nombre del grupo
                .pathsToMatch("/api/**") // Paths que se incluirán en la documentación
                .build();
    }

    // Configuración del servidor en Swagger con HTTPS
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .addServersItem(new Server().url("https://foodrecipesapi-production-07df.up.railway.app"));
    }
}
