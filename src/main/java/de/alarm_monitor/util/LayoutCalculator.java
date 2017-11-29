package de.alarm_monitor.util;

import java.awt.*;

public class LayoutCalculator {

    private final int heightPerPart;
    private final int widthPerPart;
    private final int widthParts;
    private final int heightParts;

    public LayoutCalculator(int width, int height, int widthParts, int heightParts) {
        this.widthPerPart = width / widthParts;
        this.heightPerPart = height / heightParts;
        this.widthParts = widthParts;
        this.heightParts = heightParts;
    }


    public Rectangle getRectangleForPosition(int widthStart, int heightStart, double width, double height) {
        if (widthStart + width > widthParts) {
            throw new IllegalArgumentException("Soviel Platz gibt es nicht: zu Breit");
        }

        if (heightStart + height > heightParts) {
            throw new IllegalArgumentException("Soviel Platz gibt es nicht: zu hoch");
        }

        return new Rectangle(widthStart * widthPerPart, heightStart * heightPerPart, (int) (width * widthPerPart), (int) (height * heightPerPart));
    }


}
