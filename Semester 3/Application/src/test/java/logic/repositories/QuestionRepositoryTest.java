package logic.repositories;

import data.testcontexts.QuizQuestionTestContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuestionRepositoryTest {
    private QuestionRepository questionRepository;

    @Before
    public void initialize() {
        this.questionRepository = new QuestionRepository(new QuizQuestionTestContext());
    }

    @Test
    public void testGetQuestions() {
        Assert.assertEquals(5,this.questionRepository.getQuestions().size());
    }
}
