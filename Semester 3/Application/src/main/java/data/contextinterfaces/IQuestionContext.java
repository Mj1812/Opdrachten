package data.contextinterfaces;

import domain.Question;

import java.util.List;

public interface IQuestionContext {
    /**
     * Gets a list of 5 random questions from the database
     * @return The list of questions
     */
    List<Question> getQuestions();
}
