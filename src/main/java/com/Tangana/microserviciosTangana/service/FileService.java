package com.Tangana.microserviciosTangana.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService { // <-- Es una CLASE concreta, sin 'implements'

    // Definimos la carpeta raíz donde se guardarán las fotos
    private static final String UPLOAD_DIR = "uploads";

    /**
     * Guarda un archivo en el disco con un nombre único.
     * @return El nombre del archivo generado (ej: "uuid_foto.jpg")
     */
    public String save(MultipartFile file) throws IOException {
        // 1. Asegurar que la carpeta 'uploads' existe
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 2. Generar nombre único para evitar colisiones
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // 3. Guardar (copiar) el archivo
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

    /**
     * Carga un archivo desde el disco como un Recurso (Resource).
     * Se usa para que el Controlador pueda devolver la imagen al navegador.
     */
    public Resource load(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Borra un archivo del disco físico.
     * Se usa cuando actualizas o borras un producto.
     */
    // ... (tus otros métodos save y load) ...

    public void delete(String filename) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename);
            Files.deleteIfExists(filePath); // <--- Esto borra el archivo real
            System.out.println("✅ Archivo borrado del disco: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Error al borrar archivo: " + filename);
        }
    }
}