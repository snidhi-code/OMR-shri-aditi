package DTO;

import utils.Constants;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AnswerKey {
    private  Map<Integer, Character> answers;

    public AnswerKey(String answerFile) {
        answers = new HashMap<>();
        populateAnswers(answerFile);
    }

    public Map<Integer, Character> getAnswers() {
        return answers;
    }

    public int getAnswersCount() {
        return answers.size();
    }

    public void populateAnswers(String answerFile) {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(answerFile));
            String line = bufferedReader.readLine();
            while ( line != null) {
                String[] tokens = line.split(",");
                answers.put(Integer.parseInt(tokens[0].trim()), tokens[1].trim().charAt(0));
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateAnswerForQuestion(int questionId, Character selectedChoice) {
        if ( answers.get(questionId) == selectedChoice ) {
            return true;
        }
        return false;
    }

    public int validateAndCreateScoreFile(AnswerSheet studentAnswerSheet) throws IOException{
        int correctAnswerCount = 0;

        if (studentAnswerSheet == null) {
            return correctAnswerCount;
        }

        String studentId = studentAnswerSheet.getStudentId();
        String teacherId = studentAnswerSheet.getTeacherId();
        MCQ[] mcqs = studentAnswerSheet.getMcqs();

        String filePath = "src/output/" + studentId + "-" + teacherId + ".csv";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (int q = 0; q < mcqs.length; q++) {
            String line = "";
            if (mcqs[q] != null && validateAnswerForQuestion( (q+1), mcqs[q].getSelectedChoice().getChoiceName())) {
                correctAnswerCount++;
                line += (q+1) + ", " + Constants.CORRECT_ANSWER;
            } else {
                line += (q+1) + ", " + Constants.WRONG_ANSWER;
            }
            writer.write(line + "\n");
        }
        writer.close();

        return correctAnswerCount;
    }

    public void createOverallStats() {

    }

    @Override
    public String toString() {
        return "AnswerKey{" +
                "answers=" + answers +
                '}';
    }

}
