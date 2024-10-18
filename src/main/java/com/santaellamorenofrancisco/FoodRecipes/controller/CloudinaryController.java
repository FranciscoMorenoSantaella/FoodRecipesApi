package com.santaellamorenofrancisco.FoodRecipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.santaellamorenofrancisco.FoodRecipes.services.CloudinaryService;
import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class CloudinaryController {

	@Autowired
	private CloudinaryService cloudinaryService;

	@PostMapping("/upload")
	public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
		try {
			String result = cloudinaryService.uploadImage(file);
			return ResponseEntity.ok(result);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error al subir la imagen: " + e.getMessage());
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteImage(@RequestParam("public_id") String publicId) {
		try {
			Map result = cloudinaryService.delete(publicId);
			return ResponseEntity.ok(result);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Error al eliminar la imagen: " + e.getMessage());
		}
	}
}
