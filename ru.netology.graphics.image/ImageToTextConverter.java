package ru.netology.graphics;

import ru.netology.graphics.image.BadImageSizeException;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageToTextConverter implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;               //максимальная ширина
    private int maxHeight = Integer.MAX_VALUE;              //максимальная высота
    private double maxRatio = Double.MAX_VALUE;             //диапазон
    private TextColorSchema schema = new ColorSchema();     //схема

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        URL urlObject = new URL(url);
        BufferedImage image = ImageIO.read(urlObject);

        if (image == null) {
            throw new IOException("Не удалось загрузить изображение по URL: " + url);
        }

        double ratio = (double) image.getWidth() / image.getHeight();
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth = image.getWidth();
        int newHeight = image.getHeight();

        double widthRatio = (double) maxWidth / image.getWidth();
        double heightRatio = (double) maxHeight / image.getHeight();
        double scale = Math.min(widthRatio, heightRatio);

        newWidth = (int) (image.getWidth() * scale);
        newHeight = (int) (image.getHeight() * scale);

        BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        var graphics = scaledImg.createGraphics();
        graphics.drawImage(image, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        StringBuilder result = new StringBuilder();
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                int rgb = scaledImg.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                char c = schema.convert(gray);
                result.append(c);
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        if (schema == null) {
            this.schema = new ColorSchema();
        } else {
            this.schema = schema;
        }
    }
}
