package DAO;

public class Choice {
    private char choiceName;
    private int startRow;
    private int startCol;
    private int width, height;
    private boolean isSelected;

    public Choice(int startRow, int startCol, int width, int height, char choiceName) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.width = width;
        this.height = height;
        this.choiceName = choiceName;
    }

    public Choice(int startRow, int startCol, int width, int height, char choiceName, boolean isSelected) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.width = width;
        this.height = height;
        this.isSelected = isSelected;
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

    public char getChoiceName() {
        return choiceName;
    }

    public void setChoiceName(char choiceName) {
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
}
