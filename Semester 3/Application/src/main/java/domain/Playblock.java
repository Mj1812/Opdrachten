package domain;

import enums.AnswerState;

import java.util.List;

public class Playblock {
    //region Fields
    private List<Question> questions;
    //endregion

    //region Constructor
    public Playblock(List<Question> questions) {
        this.questions = questions;
    }
    //endregion

    //region Properties
    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    //endregion

    //region Methods
    public AnswerState answerQuestion(Answer answer, Question answeredQuestion) {
        int index = -1;
        for (Question question:this.questions) {
            if (question.getQuestionString().equals(answeredQuestion.getQuestionString())) {
                index = this.questions.indexOf(question);
            }
        }
        Question question = this.questions.get(index);
        AnswerState state = AnswerState.UNANSWERED;
        if(answeredQuestion.getState() == AnswerState.UNANSWERED) {
            if (answer.isCorrect() && question.getQuestionString().equals(answeredQuestion.getQuestionString())) {
                    answeredQuestion.setState(AnswerState.CORRECT);
                }
            } else if (!answer.isCorrect() && question.getQuestionString().equals(answeredQuestion.getQuestionString()) ) {
                    answeredQuestion.setState(AnswerState.INCORRECT);
            }
            if (answer.isCorrect()) {
                state = AnswerState.CORRECT;
            } else if (!answer.isCorrect()) {
                state = AnswerState.INCORRECT;
            } else {
                state = state.UNANSWERED;
            }
        return state;
    }
    //endregion
}
