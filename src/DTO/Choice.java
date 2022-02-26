package DTO;

public class Choice {
    private Character choiceName;
    private int startRow;
    private int startCol;
    private int width, height;
    private boolean isSelected;
    private int blackCount;
    private int whiteCount;

    public Choice(int startRow, int startCol, int width, int height, Character choiceName) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.width = width;
        this.height = height;
        this.choiceName = choiceName;
    }

    public Choice(int startRow, int centerCol, int width, int height,  char choiceName) {
        this.startRow = startRow;
        this.startCol = centerCol;
        this.width = width;
        this.height = height;
        this.choiceName = choiceName;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public void setStartCol(int startCol) {
        this.startCol = startCol;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Character getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(Character choiceName) {
        this.choiceName = choiceName;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBlackCount() {
        return blackCount;
    }

    public void setBlackCount(int blackCount) {
        this.blackCount = blackCount;
    }

    public int getWhiteCount() {
        return whiteCount;
    }

    public void setWhiteCount(int whiteCount) {
        this.whiteCount = whiteCount;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "choiceName=" + choiceName +
                ", blackCount=" + getBlackCount() +
                ", whiteCount=" + getWhiteCount() +
                ", isSelected=" + isSelected +
                '}';
    }
}
