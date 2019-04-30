package websockets.websocketcontexten.interfaces;

import websockets.messages.AnsweredMessage;
import websockets.messages.InitMessage;
import websockets.messages.PlayerReadyMessage;

import javax.websocket.Session;

public interface IQuizServer {
    void initPlayer(InitMessage initMessage, Session session);
    void playerReady(PlayerReadyMessage playerReadyMessage, Session session);
    void playerAnswered(AnsweredMessage answerMessage, Session session);

}
