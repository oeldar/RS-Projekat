package grupa5.support_classes;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image tmp = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(tmp, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizedImage;
    }

    public static void saveResizedImage(File inputFile, File outputFile, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputFile);
        BufferedImage resizedImage = resizeImage(originalImage, width, height);
        ImageIO.write(resizedImage, "png", outputFile);
    }

    public static void cropImage(File inputFile, File outputFile, int cropWidth, int cropHeight) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputFile);
        
        // Izračunavanje početnih koordinata za kropovanje
        int cropStartX = (originalImage.getWidth() - cropWidth) / 2;
        int cropStartY = (originalImage.getHeight() - cropHeight) / 2;

        // Kropovanje slike
        BufferedImage croppedImage = originalImage.getSubimage(cropStartX, cropStartY, cropWidth, cropHeight);

        // Sačuvaj kropovanu sliku
        ImageIO.write(croppedImage, "png", outputFile);
    }

    public static void cropImageToSquare(File inputFile, File outputFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputFile);
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        int newDimension = Math.min(width, height);

        int cropStartX = (width > height) ? (width - height) / 2 : 0;
        int cropStartY = (height > width) ? (height - width) / 2 : 0;

        // Kropovanje slike
        BufferedImage croppedImage = originalImage.getSubimage(cropStartX, cropStartY, newDimension, newDimension);

        // Sačuvaj kropovanu sliku
        ImageIO.write(croppedImage, "png", outputFile);
    }
}
