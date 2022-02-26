package DTO;

import core.DImage;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class ImageProcessor {
    private DImage img;
    private int whiteDiscountPercent;
    private int numChoicesPerQuestion;
    private  Map<Integer, Character> choiceNameMap;
    private  Map<Integer, Character> IdMap;

    public ImageProcessor(DImage img, int whiteDiscountPercent, int numChoices) {
        this.img = img;
        this.whiteDiscountPercent = whiteDiscountPercent;
        this.numChoicesPerQuestion = numChoices;
        populateChoiceNameMap();
        populateIdMap();
    }

    public void populateChoiceNameMap() {
        choiceNameMap = new HashMap<>();
        for (int i = 0; i < numChoicesPerQuestion; i++) {
            choiceNameMap.put(i, (char) (i + 'A'));
        }
    }

    public void populateIdMap() {
        IdMap = new HashMap<>();
        for (int i = 0; i < Constants.TOTAL_IDS; i++) {
            IdMap.put(i, (char) (i + '0'));
        }
    }

    public void processImage() {
        //System.out.println(img.getHeight() + " " + img.getWidth());
    }

    public Choice scanChoice(int startRow, int startCol, int width, int height, int choiceIndex, int typeId) {
        short[][] pixels = img.getBWPixelGrid();

        int blackCount = 0;
        int whiteCount = 0;
        for (int r = startRow; r < startRow+height; r++) {
            for (int c = startCol; c < startCol+width; c++) {
                if (pixels[r][c] == Constants.BLACK) {
                    blackCount++;
                } else if(pixels[r][c] == Constants.WHITE) {
                    whiteCount++;
                }
            }
        }
        whiteCount = whiteCount  - (whiteCount * whiteDiscountPercent/100);

        Choice choice = null;
        if (typeId == Constants.TYPE_CHOICE) {
            choice = new Choice(startRow, startCol, width, height, choiceNameMap.get(choiceIndex));
        } else if (typeId == Constants.TYPE_ID) {
            choice = new Choice(startRow, startCol, width, height, IdMap.get(choiceIndex));
        }
        choice.setBlackCount(blackCount);
        choice.setWhiteCount(whiteCount);
        //System.out.println(choice);
        return choice;
    }

    public ID scanID(int startRow, int startCol) {
        int width = 17;
        int height = 19;
        int bubbleColGap = 36;
        int bubbleRowGap = 7;
        int numIds = 10;


        int runningRow = startRow;
        int runningCol = startCol;
        ID id = new ID();
        for (int r = 0; r < Constants.STUDENT_NUM_ROWS; r++) {
            MCQ idRow = new MCQ(numIds);
            for (int c = 0; c < numIds; c++) {
                Choice choice = scanChoice(runningRow, runningCol, width, height, c, Constants.TYPE_ID);
                idRow.setChoiceAtIndex(choice, c);
                runningCol = runningCol + width + bubbleColGap;
            }
            runningRow = runningRow + height + bubbleRowGap;
            runningCol = startCol;
            id.setStudentRowAtIndex(idRow, r);
        }
        return id;
    }


}
