package DTO;

public class MCQ {
    private Choice[] choices;

    public MCQ(int numChoices) {
        choices = new Choice[numChoices];
    }

    public Choice[] getChoices() {
        return choices;
    }

    public void setChoices(Choice[] choices) {
        this.choices = choices;
    }

    public void setChoiceAtIndex(Choice choice, int index) {
        if (index < 0 || index >= choices.length) {
            return;
        }
        choices[index] = choice;
    }

    public Choice getChoiceAtIndex(int index) {
        if (index < 0 || index >= choices.length) {
            return null;
        }
        return choices[index];
    }

    public Choice getSelectedChoice() {
        return scanAndFixSelectedChoice();
    }

    public Choice scanAndFixSelectedChoice() {
        int maxBlackCountIndex = 0;
        int maxBlackCount = Integer.MIN_VALUE;
        //System.out.println("scanAndFixSelectedChoice: " + Arrays.deepToString(choices));
        for (int i = 0; i < choices.length; i++) {
            if (choices[i].getBlackCount() > maxBlackCount) {
                maxBlackCount = choices[i].getBlackCount();
                maxBlackCountIndex = i;
            }
        }
        choices[maxBlackCountIndex].setSelected(true);
        return choices[maxBlackCountIndex];
    }

    public Character getSelectedChoiceName() {
        Character selected = getSelectedChoice().getChoiceName();
        //System.out.println("selected choice: " + selected);
        return selected;
    }

    @Override
    public String toString() {
        return String.valueOf(getSelectedChoiceName());
    }
}
