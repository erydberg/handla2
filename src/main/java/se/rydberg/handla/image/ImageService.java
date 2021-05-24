package se.rydberg.handla.image;

import org.imgscalr.Scalr;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

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
        MenuImage image = MenuImage.builder().name(fileName).type(file.getContentType()).build();
        image.setImageData(scaleImage(file.getBytes()));
        return imageRepository.save(image);
    }

    public void delete(String id) {
        imageRepository.deleteById(id);
    }

    public List<MenuImage> getAllImages() {
        return imageRepository.findAll();
    }

    public MenuImage rotate(MenuImage image) throws IOException {
        ByteArrayInputStream inputStreamImage = new ByteArrayInputStream(image.getImageData());
        BufferedImage bufferedImage = ImageIO.read(inputStreamImage);
        bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_90);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        image.setImageData(baos.toByteArray());
        return imageRepository.save(image);
    }

    private static byte[] scaleImage(byte[] originalImage) throws IOException {
        ByteArrayInputStream inputStreamImage = new ByteArrayInputStream(originalImage);
        BufferedImage bufferedImage = ImageIO.read(inputStreamImage);
        if (bufferedImage.getWidth() > 800) {
            bufferedImage = Scalr.resize(bufferedImage, 800);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        return baos.toByteArray();
    }
}
