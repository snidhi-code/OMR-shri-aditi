package Filters;

import Interfaces.PixelFilter;
import core.DImage;

/**
 * SimpleDownSampling : reduce the original image by half by copying only alternate row,col values into the new grid.
 */
public class SimpleDownSampling implements PixelFilter {

    @Override
    public DImage processImage(DImage img) {
        int[][] grid = img.getColorPixelGrid();
        int[][] downGrid = new int[grid.length/2][grid[0].length/2];

        int rowIndex = 0;
        int colIndex = 0;
        for (int r = 0; r < grid.length; r=r+2, rowIndex++) {
            for (int c = 0; c < grid[0].length; c=c+2, colIndex++) {
                downGrid[rowIndex][colIndex] = grid[r][c];
            }
            colIndex = 0;
        }

        img.setPixels(downGrid);
        return img;
    }
}
