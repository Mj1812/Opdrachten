package websockets.sockets;

import com.google.gson.Gson;
import domain.Match;
import domain.Player;
import enums.MessageType;
import websockets.messages.AnsweredMessage;
import websockets.messages.InitMessage;
import websockets.messages.Message;
import websockets.messages.PlayerReadyMessage;
import websockets.websocketcontexten.QuizServer;
import websockets.websocketcontexten.interfaces.IQuizServer;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/gameserver/")
public class ServerSocket {
    private static HashSet<Session> sessions = new HashSet<>();
    private static ArrayList<Player> lobby = new ArrayList<>();
    private static ArrayList<Match> matches = new ArrayList<>();
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);
    private static Gson gson = new Gson();
    private IQuizServer server = new QuizServer(lobby, matches);

    @OnOpen
    public void onUserConnect(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onUserDisconnect(CloseReason reason, Session session) {
        for(int i = 0; i < lobby.size(); i++) {
            if(lobby.get(i).getSession() == session) {
                lobby.remove(i);
                i = lobby.size();
            }
        }
        sessions.remove(session);
    }

    @OnMessage
    public void onMessageReceived(String message, Session session) {
        Message mess = gson.fromJson(message, Message.class);
        threadPool.submit(new Runnable() {
            @Override
            public void run() {
                if(mess.getMessageType().equals(MessageType.INITIALIZE)) {
                    server.initPlayer(gson.fromJson(message, InitMessage.class), session);
                } else if(mess.getMessageType().equals(MessageType.ANSWER)) {
                    server.playerAnswered(gson.fromJson(message, AnsweredMessage.class), session);
                } else if(mess.getMessageType().equals(MessageType.PLAYERREADY)){
                    server.playerReady(gson.fromJson(message, PlayerReadyMessage.class), session);
                }
            }
        });
    }

    @OnError
    public void onError(Throwable cause, Session session) {
        Logger.getLogger(ClientSocket.class.getName()).log(Level.INFO, "[ERROR]: " + cause.getMessage());
    }


}
