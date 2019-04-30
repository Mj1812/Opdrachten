package websockets.messages;

import domain.Question;
import enums.MessageType;

import java.util.List;

public class PlayerReadyMessage extends Message {
    //region Fields
    private List<Question> questions;
    //endregion

    //region Constructors
    public PlayerReadyMessage(MessageType type, List<Question> questions) {
        super(type);
        this.questions = questions;
    }
    //endregion

    //region Properties
    public List<Question> getQuestions() {
        return questions;
    }
    //endregion
}
