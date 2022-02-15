package Filters;

import Interfaces.Drawable;
import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;
import utils.Constants;

import java.awt.*;
import java.util.Arrays;

public class FixedThresholdFilter implements PixelFilter, Interactive, Drawable {
    private int threshold;

    public FixedThresholdFilter() {
        threshold = 200;
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = Constants.WHITE;
                } else {
                    grid[r][c] = Constants.BLACK;
                }
            }
        }

        System.out.println("height: " + grid.length + " width: " + grid[0].length);
        img.setPixels(grid);
        //printPixels(grid);
        return img;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short[][] pixles = img.getBWPixelGrid();
        if (mouseX >=0 && mouseX < pixles[0].length && mouseY >= 0 && mouseY < pixles.length) {
            short val = pixles[mouseY][mouseX];
            System.out.println("value at " + mouseX + " " + mouseY + " is " + val);
            System.out.println(getColor(mouseX, mouseY, pixles));
        }

    }

    @Override
    public void keyPressed(char key) {
        //System.out.println("you pressed :" + key);
        if(key == '+') {
            threshold += 20;
        } else if (key == '-') {
            threshold -= 20;
        }
        //System.out.println("threshold: " + threshold);
    }

    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        /*window.fill(window.color(100,25,250));
        window.textSize(40);
        window.text("Hello There!", 100, 100);
        window.fill(window.color(255,0,0));
        window.ellipse(0,0,30,30);
        window.fill(window.color(0,255,0));
        window.ellipse(original.getWidth()/2, original.getHeight()/2, 30,30);

         */
    }

    private void printPixels(short[][] pixels) {
        System.out.println(Arrays.deepToString(pixels));
    }

    private short getColor(int mouseX, int mouseY, short[][] pixles) {
        if (pixles[mouseY][mouseX] > threshold) {
            return  Constants.WHITE;
        } else {
            return  Constants.BLACK;
        }
    }
}

