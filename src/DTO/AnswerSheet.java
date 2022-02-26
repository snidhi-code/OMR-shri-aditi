package DTO;

public class AnswerSheet {
    private String studentId;
    private String teacherId;
    private MCQ[] mcqs;

    public AnswerSheet(String studentId, String teacherId, int numMCQs) {
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

    public MCQ[] getMcqs() {
        return mcqs;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        String mcqResult = "";
        for (int i = 0; i < mcqs.length; i++) {
            if (getMCQAtIndex(i) != null && getMCQAtIndex(i).getSelectedChoiceName() != null) {
                mcqResult += (i + 1) + " => " + getMCQAtIndex(i).getSelectedChoiceName() + ", ";
            }
        }
        return "AnswerSheet{" +
                "studentId=" + studentId +
                ", teacherId=" + teacherId +
                ", mcqs=[ " + mcqResult + " ]" +
                '}';
    }
}
