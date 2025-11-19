package com.Tangana.microserviciosTangana.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:5173")
public class FileController {

    // Ruta a la carpeta 'uploads' que creaste
    private static final String UPLOAD_DIR = "uploads";

    // 1. Endpoint para SUBIR imágenes
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Asegurar que la carpeta existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único para no sobrescribir fotos (ej: "uuid_foto.jpg")
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            
            // Guardar el archivo en disco
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Devolver la URL relativa que guardaremos en la BD
            // OJO: Empieza con "/api/files/" para que sepamos que viene del backend
            return ResponseEntity.ok("/api/files/" + fileName);

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error al subir archivo: " + e.getMessage());
        }
    }

    // 2. Endpoint para LEER/SERVIR imágenes
    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                // Determinar el tipo de contenido (imagen/jpeg, image/png, etc.)
                String contentType = "image/jpeg"; // Por defecto
                try {
                    contentType = Files.probeContentType(filePath);
                } catch (IOException ex) {
                    // ignorar
                }

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }
}