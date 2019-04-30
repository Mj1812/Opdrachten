package quizgame;

import com.google.gson.Gson;
import domain.*;
import enums.AnswerState;
import enums.MessageType;
import gui.IQuizGameApplication;
import restapi.RestAuthenticationClient;
import restapi.RestQuestionClient;
import restapi.responses.AuthenticationResponse;
import restapi.responses.QuestionResponse;
import restapi.viewmodels.Login;
import websockets.messages.*;
import websockets.sockets.ClientSocket;

import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizClient implements IQuizClient, Observer {
    //region Fields
    private Account account;
    private Playblock playBlock;
    protected Session session;
    protected WebSocketContainer container;
    protected ClientSocket clientSocket;
    private Player player;
    private int currentButtonNr = 0;
    private int count = 0;
    private RestAuthenticationClient restAuthenticationClient;
    private RestQuestionClient restQuestionClient;
    private boolean result = false;
    //endregion

    //region Constructor
    public QuizClient() {
        this.restAuthenticationClient = new RestAuthenticationClient();
        this.restQuestionClient = new RestQuestionClient();
        this.container = ContainerProvider.getWebSocketContainer();
        try {
            URI uri = URI.create("ws://localhost:8095/gameserver/");
            this.clientSocket = new ClientSocket();
            this.clientSocket.addObserver(this);
            this.session = this.container.connectToServer(this.clientSocket, uri);
        } catch (Exception e) {
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    //endregion

    //region Methods
    @Override
    public boolean login(String name, String password, IQuizGameApplication application) {
        if (name.length() == 0 || password.length() == 0 || application == null) {
            throw new IllegalArgumentException();
        } else {
            try {
                Login login = new Login(name, password);
                AuthenticationResponse authenticationResponse = restAuthenticationClient.loginUserRest(login);
                checkAuthentication(authenticationResponse, application);
            } catch (Exception e) {
                Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    @Override
    public boolean register(String name, String password, String confirmPassword, IQuizGameApplication application) {
        if (name.length() == 0 || password.length() == 0 || confirmPassword.length() == 0 || application == null) {
            throw new IllegalArgumentException();
        } else {
            try {
                if (password.equals(confirmPassword)) {
                    Login login = new Login(name, password);
                    AuthenticationResponse authenticationResponse = restAuthenticationClient.registerUserRest(login);
                    checkAuthentication(authenticationResponse, application);
                }
            } catch (Exception e) {
                Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    private boolean checkAuthentication(AuthenticationResponse response, IQuizGameApplication application) {
        try {
            int id = -1;
            if (response.getUserId() == -1) {
                application.showErrorMessage(0, response.getMessage());
            } else {
                id = response.getUserId();
            }
            if (id > 0) {
                result = true;
                this.account = new Account(id, response.getUsername(), application);
                application.showErrorMessage(this.account.getId(), "Searching for opponent!");
                Gson gson = new Gson();
                InitMessage initMessage = new InitMessage(this.account.getId(), this.account.getName(), MessageType.INITIALIZE);
                String message = gson.toJson(initMessage);
                this.session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return result;
    }

    @Override
    public boolean answerQuestion(int playerNr, int buttonNr, Answer answer, Question question) {
        boolean result = false;
        this.currentButtonNr = buttonNr;
        if (question.getState().equals(AnswerState.UNANSWERED)) {
            AnsweredMessage answeredMessage = new AnsweredMessage(MessageType.ANSWER, question, answer);
            Gson gson = new Gson();
            String message = gson.toJson(answeredMessage);
            try {
                this.session.getBasicRemote().sendText(message);
                result = true;
            } catch (Exception e) {
                Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return result;
    }

    @Override
    public void getQuestions() {
        try {
            QuestionResponse questionResponse = restQuestionClient.executeQueryGet();
            if (questionResponse.getQuestions().isEmpty()) {
                this.account.getApplication().showErrorMessage(0, questionResponse.getMessage());
            } else {
                this.playBlock = new Playblock(questionResponse.getQuestions());
                this.player = new Player(this.playBlock);
            }
        } catch (Exception e) {
            Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public Question getQuestion(int questionNr) {
        return this.playBlock.getQuestions().get(questionNr);
    }

    @Override
    public boolean notifyWhenReady(int playerNr) {
        boolean result = false;
        if (this.playBlock.getQuestions().size() == 5) {
            result = true;
            if (this.account.getId() == 0) {
                this.account.getApplication().notifyStartGame(this.account.getId(), this.account.getName());
            } else {
                PlayerReadyMessage playerReadyPackage = new PlayerReadyMessage(MessageType.PLAYERREADY, player.getPlayerBlock().getQuestions());
                Gson gson = new Gson();
                try {
                    this.session.getBasicRemote().sendText(gson.toJson(playerReadyPackage));
                } catch (Exception e) {
                    Logger.getLogger(QuizClient.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        }

        return result;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof MatchMadeMessage) {
            this.setupMatch((MatchMadeMessage) arg);
        } else if (arg instanceof StartGameMessage) {
            this.startGame((StartGameMessage) arg);
        } else if (arg instanceof AnswerResultMessage) {
            this.answerResult((AnswerResultMessage) arg);
        }
    }

    private void answerResult(AnswerResultMessage answerResultMessage) {
        AnswerState state = answerResultMessage.getAnswerState();
        if (answerResultMessage.getPlayerId() == this.account.getId()) {
            if (state == AnswerState.CORRECT) {
                this.account.getApplication().showAnswer(this.account.getId(), currentButtonNr, AnswerState.CORRECT);
            } else if (state == AnswerState.INCORRECT) {
                this.account.getApplication().showAnswer(this.account.getId(), currentButtonNr, AnswerState.INCORRECT);
            }
            this.account.getApplication().playerAnswers(this.account.getId(), state);
        } else {
            if (state == AnswerState.CORRECT) {
                this.account.getApplication().showAnswerOpponent(this.account.getId(), AnswerState.CORRECT, count);
            } else if (state == AnswerState.INCORRECT) {
                this.account.getApplication().showAnswerOpponent(this.account.getId(), AnswerState.INCORRECT, count);
            } else if (state == AnswerState.ALLANSWERED) {
                this.account.getApplication().showEndMatchOpponent(this.account.getId());
            }
            this.account.getApplication().opponentAnswers(this.account.getApplication().getOpponentNr(), state);
            this.count = count + 1;
        }
    }

    private void setupMatch(MatchMadeMessage matchMadePackage) {
        IQuizGameApplication application = this.account.getApplication();
        application.setPlayerNumber(this.account.getId(), this.account.getName());
        application.setOpponentName(matchMadePackage.getOpponentId(), matchMadePackage.getOpponentName());
    }

    private void startGame(StartGameMessage startGameMessage) {
        String name = "";


        if (startGameMessage.getStartingPlayer() == this.account.getId()) {
            name = this.account.getName();
        } else {
            name = this.account.getApplication().getOpponentName();
        }

        this.account.getApplication().setPlayerTurn(startGameMessage.getStartingPlayer());
        this.account.getApplication().notifyStartGame(this.account.getId(), name);
    }
    //endregion

}
