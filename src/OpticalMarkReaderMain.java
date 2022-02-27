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
            processAnswerPage(thresholdImage, answerKey, Constants.WHITE_DISCOUNT_PERCENT, Constants.NUM_CHOICES);
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
    private static void processAnswerPage(DImage thresholdImage,  AnswerKey answerKey, int whiteDiscountPercent, int numChoices) {
        // 0: Createw ImageProcessor object
        ImageProcessor processor = new ImageProcessor(thresholdImage, whiteDiscountPercent, numChoices);
        processor.processImage();


        // 1. Scan Student
        ID student = processor.scanID(Constants.STUDENT_START_ROW, Constants.STUDENT_START_COL);

        // 2. Scan Teacher
        ID teacher = processor.scanID(Constants.TEACHER_START_ROW, Constants.TEACHER_START_COL);

        // 3. Scan  Multiple Choice Question answers and Create AnswerSheet Object
        AnswerSheet answerSheet = new AnswerSheet(student.getId(), teacher.getId(), Constants.TOTAL_QUESTIONS);
        for (int batch = 0; batch < Constants.QUESTIONS_PER_ROW; batch++) {
            int startIndex = Constants.QUESTIONS_PER_COL * batch;
            int endIndex = Constants.QUESTIONS_PER_COL + (Constants.QUESTIONS_PER_COL * batch);
            int startRow = Constants.MCQ_STARTROW;
            int startCol = Constants.MCQ_STARTCOL + batch * (Constants.NUM_CHOICES * Constants.CHOICE_WIDTH) +
                    batch * ((Constants.NUM_CHOICES-1) * Constants.CHOICE_COL_GAP) + (batch * Constants.QUESTIONS_COL_GAP);

            processor.scanMCQBatch(answerSheet, startIndex, endIndex, startRow, startCol, Constants.NUM_CHOICES,
                    Constants.CHOICE_WIDTH, Constants.CHOICE_HEIGHT,
                    Constants.CHOICE_COL_GAP, Constants.CHOICE_ROW_GAP);
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
