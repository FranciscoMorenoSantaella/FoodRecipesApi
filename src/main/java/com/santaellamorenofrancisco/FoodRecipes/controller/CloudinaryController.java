package com.santaellamorenofrancisco.FoodRecipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.santaellamorenofrancisco.FoodRecipes.services.CloudinaryService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "https://foodrecipesapi-production-07df.up.railway.app")
public class CloudinaryController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Operation(summary = "Sube una imagen a Cloudinary")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error al subir la imagen", 
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @Parameter(description = "Archivo de imagen a subir") 
            @RequestParam("file") MultipartFile file) {
        try {
            String result = cloudinaryService.uploadImage(file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
        }
    }

    @Operation(summary = "Elimina una imagen de Cloudinary")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Imagen eliminada exitosamente", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Error al eliminar la imagen", 
                     content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteImage(
            @Parameter(description = "ID público de la imagen a eliminar") 
            @RequestParam("public_id") String publicId) {
        try {
            Map result = cloudinaryService.delete(publicId);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al eliminar la imagen: " + e.getMessage());
        }
    }
}
