package models;

import data.testcontexts.QuizQuestionTestContext;
import domain.Playblock;
import enums.AnswerState;
import logic.repositories.QuestionRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayblockTest {
    private QuestionRepository questionRepository;
    private Playblock playblock;

    @Before
    public void initialize() {
        this.questionRepository = new QuestionRepository(new QuizQuestionTestContext());
        playblock = new Playblock(questionRepository.getQuestions());
    }

    @Test
    public void testPlayblockCounstructor() {
        assertEquals(5, playblock.getQuestions().size());
    }

    @Test
    public void testPlayblockAnswerQuestion(){
        assertEquals(AnswerState.INCORRECT, playblock.answerQuestion(questionRepository.getQuestions().get(1).getAnswers().get(1), questionRepository.getQuestions().get(0)));
        assertEquals(AnswerState.CORRECT, playblock.answerQuestion(questionRepository.getQuestions().get(0).getAnswers().get(4), questionRepository.getQuestions().get(0)));

    }
}
