package websockets.messages;

import enums.MessageType;

public class MatchMadeMessage extends  Message {
    //region Fields
    private int opponentId;
    private String opponentName;
    //endregion

    //region Constructors
    public MatchMadeMessage(int opponentId, String opponentName, MessageType type) {
        super(type);
        this.opponentId = opponentId;
        this.opponentName = opponentName;
    }
    //endregion

    //region Properties
    public int getOpponentId() {
        return opponentId;
    }

    public String getOpponentName() {
        return opponentName;
    }

    //endregion
}
