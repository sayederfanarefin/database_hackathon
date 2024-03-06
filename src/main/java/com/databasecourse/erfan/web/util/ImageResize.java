package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.Constants;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
public class ImageResize {
    public static void resizeImage(MultipartFile inputFile, String outputPath) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile.getInputStream());

        BufferedImage outputImage = new BufferedImage(Constants.IMAGE_SAVE_WIDTH_PX, Constants.IMAGE_SAVE_HEIGHT_PX, inputImage.getType());

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, Constants.IMAGE_SAVE_WIDTH_PX, Constants.IMAGE_SAVE_HEIGHT_PX, null);
        g2d.dispose();

        File outputFile = new File(outputPath);
        ImageIO.write(outputImage, "jpg", outputFile);
    }

    public static void resizeAndCropCenter(MultipartFile inputFile, String outputPath) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputFile.getInputStream());

        int originalWidth = inputImage.getWidth();
        int originalHeight = inputImage.getHeight();

        // Resize to the smaller dimension (width or height)
        double scale = Math.min((double) Constants.IMAGE_SAVE_WIDTH_PX / originalWidth, (double) Constants.IMAGE_SAVE_HEIGHT_PX / originalHeight);
        int resizedWidth = (int) (originalWidth * scale);
        int resizedHeight = (int) (originalHeight * scale);

        BufferedImage resizedImage = new BufferedImage(resizedWidth, resizedHeight, inputImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, resizedWidth, resizedHeight, null);
        g2d.dispose();

        // Crop to the center
        int startX = Math.max(0, (resizedWidth - Constants.IMAGE_SAVE_WIDTH_PX) / 2);
        int startY = Math.max(0, (resizedHeight - Constants.IMAGE_SAVE_HEIGHT_PX) / 2);

        BufferedImage croppedImage = resizedImage.getSubimage(startX, startY, Constants.IMAGE_SAVE_WIDTH_PX, Constants.IMAGE_SAVE_HEIGHT_PX);

        File outputFile = new File(outputPath);
        ImageIO.write(croppedImage, "jpg", outputFile);
    }

}
