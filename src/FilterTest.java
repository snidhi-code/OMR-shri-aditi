import DTO.*;
import FileIO.PDFHelper;
import Filters.FixedThresholdFilter;
import core.DImage;
import core.DisplayWindow;
import processing.core.PImage;
import utils.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FilterTest {
    public static final String CURRENTFOLDER = System.getProperty("user.dir") + "/";
    public static final String PDFPATH =  "assets/omrtest2.pdf";
    static Map<String, Integer> overAllStatsMap = new HashMap<>();

    public static void main(String[] args) {
        SaveAndDisplayExample();
        RunTheFilter();
    }

    private static void RunTheFilter() {
        System.out.println("Loading pdf....");
        ArrayList<PImage> pImages = PDFHelper.getPImagesFromPdf(PDFPATH);
        AnswerKey answerKey = new AnswerKey("src/utils/answerkey.csv");

        for (int i = 0; i < pImages.size(); i++) {
            int pageNumber = i+1;
            PImage in = pImages.get(i);
            DImage img = new DImage(in);

            System.out.println("Running filter on page : " + pageNumber );
            FixedThresholdFilter filter = new FixedThresholdFilter();
            DImage thresholdImage = filter.processImage(img);

            processAnswerPage(thresholdImage, answerKey, 5, 5);
        }

        // populateOverall Stats csv file for the class
        writeOverAllStatsToCSV(answerKey.getAnswersCount());

        //  Output Overall Stats for the class
        printOverAllStats(answerKey.getAnswersCount());
    }

    private static void processAnswerPage(DImage thresholdImage,  AnswerKey answerKey, int whiteDiscountPercent, int numChoices) {
        ImageProcessor processor = new ImageProcessor(thresholdImage, whiteDiscountPercent, numChoices);
        processor.processImage();

        int startRow = 467;
        int startCol = 126;
        int width = 17;
        int height = 19;
        int bubbleColGap = 20;
        int bubbleRowGap = 20;
        int questionGap = 117;
        int totalQuestions = 100;
        int questionsPerRow = 4;
        int numBubbles = 5;
        int questionsPerCol = totalQuestions / questionsPerRow;

        // 1. Scan Student
        ID student = processor.scanID(331, 77);

        // 2. Scan Teacher
        ID teacher = processor.scanID(331, 631);

        int runningRow = startRow;
        int runningCol = startCol;

        // 3. Scan  Multiple Choice Question answers
        AnswerSheet answerSheet = new AnswerSheet(student.getId(), teacher.getId(), totalQuestions);
        for (int q = 0; q < 25; q++) {
            MCQ mcq = new MCQ(numBubbles);
            //System.out.println("Question: " + (q+1));
            for (int c = 0; c < numBubbles; c++) {
                Choice choice = processor.scanChoice(runningRow, runningCol, width, height, c, Constants.TYPE_CHOICE);
                mcq.setChoiceAtIndex(choice, c);
                runningCol = runningCol + width + bubbleColGap;
            }
            answerSheet.setMCQAtIndex(mcq, q);
            runningRow = runningRow + height + bubbleRowGap;
            runningCol = startCol;
        }

        System.out.println(answerSheet);

        // 4. Validate Answer Sheet
        try {
            int correctAnswerCount = answerKey.validateAndCreateScoreFile(answerSheet);
            overAllStatsMap.put(student.getId(), correctAnswerCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeOverAllStatsToCSV(int totalAnswers) {
        try{
            String filePath = "src/output/overallstats.csv";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for (Map.Entry<String, Integer> entry: overAllStatsMap.entrySet()) {
                String line = "";
                line  = line + entry.getKey() + "," + entry.getValue() + "," + totalAnswers + "\n";
                writer.write(line);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(" Failed to write overall stats to CSV file!");
        }
    }

    private static void printOverAllStats(int totalAnswers) {
        System.out.println();
        System.out.println("===========================================================");
        System.out.println("StudentID \t | Scored \t | Total ");
        System.out.println("===========================================================");
        for (Map.Entry<String, Integer> entry: overAllStatsMap.entrySet()) {
            System.out.println(entry.getKey() + " \t\t |  " + entry.getValue() + " \t\t |  " + totalAnswers);
        }
        System.out.println("===========================================================");
    }

    private static void SaveAndDisplayExample() {
        PImage img = PDFHelper.getPageImage(PDFPATH,1);
        img.save(CURRENTFOLDER + "assets/page1.png");

        DisplayWindow.showFor("assets/page1.png");
    }
}