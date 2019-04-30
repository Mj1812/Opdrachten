package domain;

import enums.AnswerState;

import java.util.List;

public class Question {
    //region Fields
    private int questionId;
    private String questionString;
    private List<Answer> answers;
    private AnswerState state;
    //endregion

    //region Constructor
    public Question() {

    }

    public Question(int questionId, String questionString, List<Answer> answers, AnswerState state) {
        this.questionId = questionId;
        this.questionString = questionString;
        this.answers = answers;
        this.state = state;
    }
    //endregion

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestionString() {
        return questionString;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public AnswerState getState() {
        return state;
    }

    public void setState(AnswerState state) {
        this.state = state;
    }
}
