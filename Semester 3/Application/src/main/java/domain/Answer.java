package domain;

import enums.UsageState;

public class Answer {
    //region Fields
    private int answerId;
    private int questionId;
    private String answerString;
    private boolean correct;
    private UsageState state = UsageState.NOTUSED;
    //endregion

    //region Constructor
    public Answer() {

    }
    public Answer(int answerId, String answerString, boolean correct) {
        this.answerId = answerId;
        this.answerString = answerString;
        this.correct = correct;
    }

    public Answer(int answerId, int questionId, String answerString, boolean correct) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.answerString = answerString;
        this.correct = correct;
    }
    //endregion

    //region Properties
    public int getAnswerId() {
        return answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getAnswerString() {
        return answerString;
    }

    public boolean isCorrect() {
        return correct;
    }

    public UsageState getState() {
        return state;
    }

    public void setState(UsageState state) {
        this.state = state;
    }
    //endregion
}
