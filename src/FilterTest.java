import FileIO.PDFHelper;
import Filters.DisplayInfoFilter;
import Filters.FixedThresholdFilter;
import Filters.SimpleDownSampling;
import Interfaces.PixelFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;

import java.util.Arrays;

public class FilterTest {
    public static String currentFolder = System.getProperty("user.dir") + "/";

    public static void main(String[] args) {
         //SaveAndDisplayExample();

        RunTheFilter();
    }

    private static void RunTheFilter() {
        System.out.println("Loading pdf....");
        PImage in = PDFHelper.getPageImage("assets/omrtest2.pdf",1);
        DImage img = new DImage(in);       // you can make a DImage from a PImage
        System.out.println("img: " + img.getWidth() + " " + img.getHeight());

//        System.out.println("STEP1: Running SimpleDownSampling filter on page 1....");
//        SimpleDownSampling filter = new SimpleDownSampling();
//        DImage downImage = filter.processImage(img);
//        System.out.println("downImage: " + downImage.getWidth() + " " + downImage.getHeight());

        System.out.println("STEP1: Running FixedThreshold filter on page 1....");
        FixedThresholdFilter fixedThresholdFilter = new FixedThresholdFilter();
        DImage thresholdImage = fixedThresholdFilter.processImage(img);

        short[][]pixels = thresholdImage.getBWPixelGrid();
       // System.out.println(Arrays.deepToString(pixels));

        System.out.println("STEP3: Apply CustomFilter on thresholdImage on page 1....");
        //Step 3.1: Loop through the pixels 2d array and Extract the Choice objects for a given MCQ
        /*
              1. Find the start row, col for the 1MCQ and 1 choice bubble
              2. Compute the width, height
         */
        //Step 3.2: Create MCQ object
        //Step 3.3: Create AnswerSheet object


    }

    private static void SaveAndDisplayExample() {
        PImage img = PDFHelper.getPageImage("assets/omrtest2.pdf",1);
        img.save(currentFolder + "assets/page1.png");

        DisplayWindow.showFor("assets/page1.png");
    }
}