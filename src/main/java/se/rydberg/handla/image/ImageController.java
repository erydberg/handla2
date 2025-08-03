package se.rydberg.handla.image;

import org.owasp.encoder.Encode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/image")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("imageupload") MultipartFile file) {
        String message = "";
        try {

            imageService.save(file);
            message = "Sparat filen " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

        } catch (IOException e) {
            message = "Kunde inte spara filen :-(";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") String id) {
        MenuImage image = imageService.getImageById(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getName() + "\"")
                .body(image.getImageData());
    }

    @GetMapping("/getall")
    public ResponseEntity<List<ImageFileMetaData>> getListFiles() {
        List<ImageFileMetaData> images = imageService.getAllImages().stream().map(image -> {
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/image/")
                    .path(String.valueOf(image.getId()))
                    .toUriString();

            return new ImageFileMetaData(image.getName(), fileDownloadUri, image.getType(),
                    image.getImageData().length);
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(images);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteImage (@PathVariable("id") String id){
        imageService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Bild borttagen"));
    }

    @GetMapping("/rotate/{id}")
    public ResponseEntity<String> rotateImage (@PathVariable("id") String id) throws IOException {
        String imageId = Encode.forJava(id);
        MenuImage image = imageService.getImageById(imageId);
        MenuImage rotatedImage = imageService.rotate(image);
        return ResponseEntity.status(HttpStatus.OK).body(rotatedImage.getId());
    }
}
