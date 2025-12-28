package ru.netology.graphics;
import ru.netology.graphics.image.TextColorSchema;

public class ColorSchema implements TextColorSchema {
    private final char[] symbols = {'#', '$', '@', '%', '*', '+', '-', ' '};

    @Override
    public char convert(int color) {
        color = Math.max(0, Math.min(255, color));
        int segmentSize = 256 / symbols.length;
        int index = color / segmentSize;

        if (index >= symbols.length) {
            index = symbols.length - 1;
        }
        return symbols [index];
    }
}
