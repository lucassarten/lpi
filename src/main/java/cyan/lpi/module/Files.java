package cyan.lpi.module;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import cyan.lpi.service.FileService;

@ModuleDef(desc = "file storage management")
@Service
public final class Files implements Module {
    @Autowired
    static FileService fileService;

    @CommandDef(desc = "upload a file", params = {})
    public static String upload(Map<String, String> params, MultipartFile file) throws IOException {
        // Generate a unique ID for the image
        String id = UUID.randomUUID().toString();

        // Save the image to the server
        fileService.saveImage(id, file.getBytes());

        // Return the URL to the image
        return "http://192.9.163.175/" + id;
    }

    @CommandDef(desc = "retrieve a file", params = { "<id>" })
    public static byte[] get(Map<String, String> params, Map<String, String> headers) {
        // Get the ID of the image
        String id = params.get("0");

        // Get the image from the server
        byte[] imageData = fileService.getImage(id);

        // Return the image
        return imageData;
    }
}
