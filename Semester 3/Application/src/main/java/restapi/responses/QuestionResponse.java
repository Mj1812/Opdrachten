package restapi.responses;

import domain.Answer;
import domain.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionResponse {
    //region Fields
    private List<Question> questions = new ArrayList<>();
    private List<Answer> answers = new ArrayList<>();
    private String message;
    //endregion

    //region Properties

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    //endregion
}
