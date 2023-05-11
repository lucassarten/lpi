package cyan.lpi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class FileService {
    
    private static final String STORAGE_LOCATION = "/var/images/";

    public void saveImage(String id, byte[] imageData) throws IOException {
        String fileName = id + ".jpg";
        // create storage location if it doesn't exist
        Path storagePath = Paths.get(STORAGE_LOCATION);
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        // write image to storage location
        Path path = Paths.get(STORAGE_LOCATION + fileName);
        Files.write(path, imageData);
    }

    public byte[] getImage(String id) {
        String fileName = id + ".jpg";
        Path path = Paths.get(STORAGE_LOCATION + fileName);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}