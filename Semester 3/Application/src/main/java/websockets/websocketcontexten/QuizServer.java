package websockets.websocketcontexten;

import com.google.gson.Gson;
import domain.Match;
import domain.Playblock;
import domain.Player;
import enums.AnswerState;
import enums.MessageType;
import websockets.MatchPlayers;
import websockets.messages.*;
import websockets.sockets.ClientSocket;
import websockets.sockets.ServerSocket;
import websockets.websocketcontexten.interfaces.IQuizServer;

import javax.websocket.Session;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizServer implements IQuizServer {
    private List<Player> lobby;
    private List<Match> matches;
    private static Gson gson = new Gson();
    private Random random = new Random();

    public QuizServer(List<Player> lobby, List<Match> matches) {
        this.lobby = lobby;
        this.matches = matches;
    }

    public void initPlayer(InitMessage initMessage, Session session) {
        Player player = new Player(session, initMessage.getUserId(), initMessage.getUsername());
        lobby.add(player);
        Logger.getLogger(ServerSocket.class.getName()).log(Level.INFO, "Players in lobby: " ,lobby.size());
        Logger.getLogger(ServerSocket.class.getName()).log(Level.INFO, "Player " + player.getUsername() + " has joined the queue. (" + session.getId() + ")");

        Player opponent = null;

        for(int i = 0; i < lobby.size() - 1; i++) {
            if(!lobby.get(i).getUsername().equals(player.getUsername())) {
                opponent = lobby.get(i);
                i = lobby.size();
            }
        }

        if(opponent != null) {
            matches.add(new Match(opponent, player));
            lobby.remove(opponent);
            lobby.remove(player);

            try {
                opponent.getSession().getBasicRemote().sendText(gson.toJson(new MatchMadeMessage(player.getId(), player.getUsername(), MessageType.MATCHMADE)));
                player.getSession().getBasicRemote().sendText(gson.toJson(new MatchMadeMessage(opponent.getId(), opponent.getUsername(), MessageType.MATCHMADE)));
            } catch (Exception e) {
                Logger.getLogger(ClientSocket.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    private MatchPlayers getMatchPlayers(Session session) {
        MatchPlayers matchPlayers = null;
        for(Match match : matches) {
            if (match.getPlayerOne().getSession() == session) {
                matchPlayers = new MatchPlayers(match, match.getPlayerOne(), match.getPlayerTwo());
            } else if (match.getPlayerTwo().getSession() == session) {
                matchPlayers = new MatchPlayers(match, match.getPlayerTwo(), match.getPlayerOne());
            }
        }

        return matchPlayers;
    }

    public void playerReady(PlayerReadyMessage playerReadyMessage, Session session) {
        MatchPlayers matchPlayers = this.getMatchPlayers(session);
        matchPlayers.getPlayer().setPlayerBlock(new Playblock(playerReadyMessage.getQuestions()));
        if (matchPlayers.getPlayer().getPlayerBlock().getQuestions().size() == 5 && matchPlayers.getOpponent().getPlayerBlock().getQuestions().size() == 5) {
            int startingPlayer = random.nextInt() % 2;
            if (startingPlayer == 0) {
                startingPlayer = matchPlayers.getPlayer().getId();
            } else {
                startingPlayer = matchPlayers.getOpponent().getId();
            }
            StartGameMessage startGamePackage = new StartGameMessage(MessageType.STARTGAME, startingPlayer);
            String message = gson.toJson(startGamePackage);
            try {
                matchPlayers.getPlayer().getSession().getBasicRemote().sendText(message);
                matchPlayers.getOpponent().getSession().getBasicRemote().sendText(message);
            } catch (Exception e) {
                Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void playerAnswered(AnsweredMessage answerMessage, Session session) {
        int score = 0;
        MatchPlayers matchPlayers = this.getMatchPlayers(session);

        if (matchPlayers != null) {
            AnswerState state = matchPlayers.getPlayer().getPlayerBlock().answerQuestion(answerMessage.getAnswer(), answerMessage.getQuestion());
            if (state.equals(AnswerState.CORRECT)){
                score = score + 1;
                matchPlayers.getPlayer().setScore(score);
            }
            AnswerResultMessage answerResultMessage = new AnswerResultMessage(MessageType.ANSWERRESULT, state, matchPlayers.getPlayer().getId(), answerMessage.getQuestion());
            String message = gson.toJson(answerResultMessage);
            try {
                matchPlayers.getPlayer().getSession().getBasicRemote().sendText(message);
                matchPlayers.getOpponent().getSession().getBasicRemote().sendText(message);
            } catch (Exception e) {
                Logger.getLogger(ServerSocket.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }
}
