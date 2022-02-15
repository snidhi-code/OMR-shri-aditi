package DAO;

public class Choice {
    private char choiceName;
    private int centerRow;
    private int centerCol;
    private int width, height;
    private boolean isSelected;

    public Choice(int centerRow, int centerCol, int width, int height, char choiceName) {
        this.centerRow = centerRow;
        this.centerCol = centerCol;
        this.width = width;
        this.height = height;
        this.choiceName = choiceName;
    }

    public Choice(int centerRow, int centerCol, int width, int height,  char choiceName, boolean isSelected) {
        this.centerRow = centerRow;
        this.centerCol = centerCol;
        this.width = width;
        this.height = height;
        this.isSelected = isSelected;
    }

    public int getCenterRow() {
        return centerRow;
    }

    public void setCenterRow(int centerRow) {
        this.centerRow = centerRow;
    }

    public int getCenterCol() {
        return centerCol;
    }

    public void setCenterCol(int centerCol) {
        this.centerCol = centerCol;
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
