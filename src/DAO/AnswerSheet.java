package DAO;

public class AnswerSheet {
    private int studentId;
    private int teacherId;
    MCQ[] mcqs;

    public AnswerSheet(int studentId, int teacherId, int numMCQs) {
        this.studentId = studentId;
        this.teacherId = teacherId;
        this.mcqs = new MCQ[numMCQs];
    }

    public void setMCQAtIndex(MCQ mcq, int index) {
        if (index < 0 || index >= mcqs.length) {
            return;
        }
        mcqs[index] = mcq;
    }

    public MCQ getMCQAtIndex(int index) {
        if (index < 0 || index >= mcqs.length) {
            return null;
        }
        return mcqs[index];
    }
}
