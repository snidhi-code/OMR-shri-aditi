package DTO;

import utils.Constants;

public class ID {
    private String id;
    private MCQ[] rows;

    public ID() {
        rows = new MCQ[Constants.STUDENT_NUM_ROWS];
    }

    public ID(int numRows) {
        rows = new MCQ[numRows];
    }

    public void setStudentRowAtIndex(MCQ mcq, int index) {
        if (index < 0 || index >= rows.length) {
            return;
        }
        rows[index] = mcq;
    }

    public MCQ getStudentRowAtIndex(int index) {
        if (index < 0 || index >= rows.length) {
            return null;
        }
        return rows[index];
    }

    public String getId() {
        String idString = "";
        for (int i = 0; i < rows.length; i++) {
            MCQ mcq = rows[i];
            if (mcq != null && mcq.getSelectedChoiceName() != null) {
                idString += mcq.getSelectedChoiceName();
            }
        }
        return idString;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
