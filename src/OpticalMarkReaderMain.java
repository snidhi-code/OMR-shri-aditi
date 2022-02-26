import DTO.*;
import FileIO.PDFHelper;
import Filters.FixedThresholdFilter;
import core.DImage;
import processing.core.PImage;
import utils.Constants;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Author: David Dobervich (this is my edit)
// ANOTHER EDIT.
public class OpticalMarkReaderMain {
    // stats map, mapping Student ID to total number of correct answers
    private static Map<String, Integer> overAllStatsMap = new HashMap<>();

    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);

        /*
        Your code here to...
        (1).  Load the pdf
        (2).  Loop over its pages
        (3).  Create a DImage from each page and process its pixels
        (4).  Output 2 csv files
         */

        // Step1: Load the pdf file and get all the pages
        System.out.println("Loading pdf....");
        ArrayList<PImage> pImages = PDFHelper.getPImagesFromPdf(pathToPdf);

        // Step2: Load the answer key for the given test
        AnswerKey answerKey = new AnswerKey("src/utils/answerkey.csv");

        // Step3: Loop through the student's answer sheet pages and apply the filter and image processing
        for (int i = 0; i < pImages.size(); i++) {
            int pageNumber = i+1;

            // Step 3.1: Get PImage and Create DImage
            PImage in = pImages.get(i);
            DImage img = new DImage(in);

            // Step3.2: Apply the Fixed Threshold Filter
            System.out.println("Running filter on page : " + pageNumber );
            FixedThresholdFilter filter = new FixedThresholdFilter();
            DImage thresholdImage = filter.processImage(img);

            // Step3.3: Process the page and create csv file for each student with score stats for all the questions
            processAnswerPage(thresholdImage, answerKey, pageNumber,  Constants.WHITE_DISCOUNT_PERCENT, Constants.NUM_CHOICES);
        }

        // Step4: populateOverall Stats csv file for the class
        writeOverAllStatsToCSV(answerKey.getAnswersCount());

        // Step5: Output Overall Stats for the class
        printOverAllStats(answerKey.getAnswersCount());
    }

    /**
     *  For Each Page do the following:
     *  1. Scan the Student ID
     *  2. Scan the Teacher ID
     *  3. Scan the Multiple Choice Question choices
     *  4. Process the answers and validate them with the answer key
     *  5. Create CSV file with the score stats : studentid-teacherid.csv in the output directory
     * @param thresholdImage
     * @param answerKey
     * @param whiteDiscountPercent
     * @param numChoices
     */
    private static void processAnswerPage(DImage thresholdImage,  AnswerKey answerKey, int pageNumber, int whiteDiscountPercent, int numChoices) {
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
        student.setId(student.getId() + "-" + pageNumber);

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

    /**
     * Loop through each student in overAllStatsMap and get their corresponding total score
     * and generate the overall stats CSV file
     * @param totalAnswers
     */
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

    /**
     *  Loop through each student in overAllStatsMap and get their corresponding total score
     *  and display the over all stats table on the terminal
     * @param totalAnswers
     */
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

    /**
     *  Choose the file
     * @return
     */
    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir") + "/assets";
        System.out.println(userDirLocation);
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }

}
