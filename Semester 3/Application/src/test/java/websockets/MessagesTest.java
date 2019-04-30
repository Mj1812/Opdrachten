package websockets;

import data.testcontexts.QuizQuestionTestContext;
import enums.AnswerState;
import enums.MessageType;
import logic.repositories.QuestionRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import websockets.messages.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MessagesTest {
    private QuestionRepository questionRepository;

    @Before
    public void initialize() {
        this.questionRepository = new QuestionRepository(new QuizQuestionTestContext());
    }
    @Test
    public void testMessage() {
        Message message = new Message(MessageType.STARTGAME);

        Assert.assertNotNull(message);
        Assert.assertEquals(MessageType.STARTGAME, message.getMessageType());

        message.setMessageType(MessageType.ANSWER);
        Assert.assertEquals(MessageType.ANSWER, message.getMessageType());
    }

    @Test
    public void initMessageTest() {
        InitMessage initMessage = new InitMessage(1, "Marco", MessageType.INITIALIZE);
        assertNotNull(initMessage);
        assertEquals(1, initMessage.getUserId());
        assertEquals("Marco", initMessage.getUsername());
    }

    @Test
    public void matchMadeMessageTest() {
        MatchMadeMessage matchMadeMessage = new MatchMadeMessage(2, "Mark", MessageType.MATCHMADE);
        assertNotNull(matchMadeMessage);
        assertEquals(2, matchMadeMessage.getOpponentId());
        assertEquals("Mark", matchMadeMessage.getOpponentName());
    }

    @Test
    public void playerReadyMessageTest() {
        PlayerReadyMessage playerReadyMessage = new PlayerReadyMessage(MessageType.PLAYERREADY, questionRepository.getQuestions());
        assertNotNull(playerReadyMessage);
        assertEquals(5, playerReadyMessage.getQuestions().size());
    }

    @Test
    public void startGameMessageTest() {
        StartGameMessage startGamePackage = new StartGameMessage(MessageType.STARTGAME, 1);
        assertNotNull(startGamePackage);
        assertEquals(1, startGamePackage.getStartingPlayer());
    }

    @Test
    public void answeredMessageTest() {
        AnsweredMessage answered = new AnsweredMessage(MessageType.ANSWER, questionRepository.getQuestions().get(0), questionRepository.getQuestions().get(0).getAnswers().get(0));
        assertNotNull(answered);
        assertEquals(questionRepository.getQuestions().get(0), answered.getQuestion());
        assertEquals(questionRepository.getQuestions().get(0).getAnswers().get(0), answered.getAnswer());
        answered.setQuestion(questionRepository.getQuestions().get(1));
        assertEquals(questionRepository.getQuestions().get(1), answered.getQuestion());
        answered.setAnswer(questionRepository.getQuestions().get(1).getAnswers().get(2));
        assertEquals(questionRepository.getQuestions().get(1).getAnswers().get(2), answered.getAnswer());
    }

    @Test
    public void shotResultMessageTest() {
        AnswerResultMessage answerResultMessage = new AnswerResultMessage(MessageType.ANSWERRESULT, AnswerState.CORRECT, 1, questionRepository.getQuestions().get(3) );
        assertNotNull(answerResultMessage);
        assertEquals(AnswerState.CORRECT, answerResultMessage.getAnswerState());
        assertEquals(1, answerResultMessage.getPlayerId());
        assertEquals(questionRepository.getQuestions().get(3), answerResultMessage.getQuestion());
    }

}
