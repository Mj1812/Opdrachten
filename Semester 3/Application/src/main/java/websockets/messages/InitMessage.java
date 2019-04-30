package websockets.messages;

import enums.MessageType;

public class InitMessage extends Message {
    //region Fields
    private int userId;
    private String username;
    //endregion

    //region Constructors
    public InitMessage(int userId, String username, MessageType type) {
        super(type);
        this.userId = userId;
        this.username = username;
    }
    //endregion

    //region Properties
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
    //endregion
}
