package DAO;

public class MCQ {
    private Choice[] choices;

    private MCQ(int numChoices) {
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
}
