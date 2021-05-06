package se.rydberg.handla.image;

import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public MenuImage getImageById(String id) {
        return imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public MenuImage save(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        //scaleImage(file.getBytes());

        MenuImage image = MenuImage.builder()
                .name(fileName)
                .type(file.getContentType())
                .build();
        image.setImageData(scaleImage(file.getBytes()));
        return imageRepository.save(image);
    }

    public void delete(String id) {
        imageRepository.deleteById(id);
    }

    public List<MenuImage> getAllImages() {
        return imageRepository.findAll();
    }

    private static byte[] scaleImage(byte[] originalImage) throws IOException {
        ByteArrayInputStream inputStreamImage = new ByteArrayInputStream(originalImage);
        BufferedImage bufferedImage = ImageIO.read(inputStreamImage);
        if (bufferedImage.getWidth() > 600) {
            System.out.println("skalar om");
            bufferedImage = Scalr.resize(bufferedImage, 600);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        return baos.toByteArray();
    }
}
