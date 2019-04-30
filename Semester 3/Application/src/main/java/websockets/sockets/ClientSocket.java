package websockets.sockets;

import com.google.gson.Gson;
import enums.MessageType;
import websockets.messages.AnswerResultMessage;
import websockets.messages.MatchMadeMessage;
import websockets.messages.Message;
import websockets.messages.StartGameMessage;

import javax.websocket.*;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint
public class ClientSocket extends Observable {
    @OnOpen
    public void onConnect() {
        Logger.getLogger(ClientSocket.class.getName()).log(Level.INFO, "[Connected]");
    }

    @OnClose
    public void onDisconnect(CloseReason reason) {
        Logger.getLogger(ClientSocket.class.getName()).log(Level.INFO, "[Closed]: {0} " ,reason);
    }

    @OnMessage
    public void onMessage(String message) {
        Gson gson = new Gson();
        Message aMessage = gson.fromJson(message, Message.class);
        if(aMessage.getMessageType().equals(MessageType.MATCHMADE)) {
            aMessage = gson.fromJson(message, MatchMadeMessage.class);
        } else if(aMessage.getMessageType().equals(MessageType.STARTGAME)) {
            aMessage = gson.fromJson(message, StartGameMessage.class);
        } else if(aMessage.getMessageType().equals(MessageType.ANSWERRESULT)) {
            aMessage = gson.fromJson(message, AnswerResultMessage.class);
        }
        this.setChanged();
        this.notifyObservers(aMessage);
    }

    @OnError
    public void onError(Throwable cause) {
        Logger.getLogger(ClientSocket.class.getName()).log(Level.INFO, "[ERROR]: " + cause.getMessage());
    }
}
