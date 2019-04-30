package gui;

import domain.Answer;
import domain.Question;
import enums.AnswerState;
import enums.UsageState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import logic.repositories.helperclasses.Algorithm;
import quizgame.IQuizClient;
import quizgame.QuizClient;

import javax.swing.*;

public class QuizGameApplication extends Application implements IQuizGameApplication {

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfPassword;

    @FXML
    private Button btLogin;
    //Enemy
    @FXML
    private Button btQuestionOne;

    @FXML
    private Button btQuestionTwo;

    @FXML
    private Button btQuestionThree;

    @FXML
    private Button btQuestionFour;

    @FXML
    private Button btQuestionFive;

    //Player
    @FXML
    private Label lbQuestion;

    @FXML
    private Button btAnswerOne;

    @FXML
    private Button btAnswerTwo;

    @FXML
    private Button btAnswerThree;

    @FXML
    private Button btAnswerFour;

    @FXML
    private TitledPane pnEnemyBlock;

    @FXML
    private TitledPane pnPlayerBlock;

    @FXML
    private Button btSkip;

    @FXML
    private Button btHint;

    @FXML
    private Button btNewGame;

    @FXML
    private Button btStartGame;

    @FXML
    private TextField tfConfirmPassword;

    @FXML
    private Button btRegister;

    private IQuizClient game;
    private String playerName;
    private int playerNr;
    private String opponentName;
    private int opponentNr;
    private int playerTurn;
    private boolean playingMode = false;
    private int count = 0;
    private Question currentQuestion = new Question();
    private Answer answerOne = new Answer();
    private Answer answerTwo = new Answer();
    private Answer answerThree = new Answer();
    private Answer answerFour = new Answer();
    private boolean gameEnded = false;
    private Integer[] counters = new Integer[5];
    private static final String ERROR = "Error";

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIQuiz.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        this.game = new QuizClient();
        tfUsername.setDisable(false);
        tfPassword.setDisable(false);
        btLogin.setDisable(false);
        btSkip.setDisable(true);
        btHint.setDisable(true);
        btNewGame.setDisable(true);
        pnEnemyBlock.setDisable(true);
        pnPlayerBlock.setDisable(true);
        btStartGame.setDisable(true);
        counters[0] = 0;
        counters[1] = 0;
        counters[2] = 0;
        counters[3] = 0;
    }

    @FXML
    public void onRegister_Click(ActionEvent actionEvent){
        if (tfUsername.getText().equals("")) {
            infoBox("Enter your name before you register", ERROR);
        }
        this.playerName = tfUsername.getText();
        if (tfPassword.equals("") || tfConfirmPassword.equals("")) {
            infoBox("Enter your password before you register", ERROR);
        }
        String confirmPassword = tfConfirmPassword.getText();
        String playerPassword = tfPassword.getText();
        game.register(this.playerName, playerPassword, confirmPassword ,this);
    }

    @FXML
    public void onLogin_Click(ActionEvent actionEvent) {
        if (tfUsername.getText().equals("")) {
            infoBox("Enter your name before logging in", ERROR);
        }
        this.playerName = tfUsername.getText();
        if (tfUsername.getText().equals("")) {
            infoBox("Enter your password before logging in", ERROR);
        }
        String playerPassword = tfPassword.getText();
        if(game.login(this.playerName, playerPassword, this)){
            btLogin.setDisable(true);
            tfUsername.setDisable(true);
            tfPassword.setDisable(true);
            tfConfirmPassword.setDisable(true);
            btRegister.setDisable(true);
            btStartGame.setDisable(false);
        }
    }

    @FXML
    public void onStartGame_Click(ActionEvent actionEvent) {
        game.getQuestions();
        notifyWhenReady();
        setQuestion(this.count);
    }

    @FXML
    public void onAnswerOne_Click(ActionEvent actionEvent) {
    this.counters[0] = this.counters[0] + 1;
        turn(1, answerOne);
    }

    @FXML
    public void onAnswerTwo_Click(ActionEvent actionEvent) {
        this.counters[1] = this.counters[1] + 1;
        turn(2, answerTwo);

    }

    @FXML
    public void onAnswerThree_Click(ActionEvent actionEvent) {
        this.counters[2] = this.counters[2] + 1;
        turn(3, answerThree);

    }

    @FXML
    public void onAnswerFour_Click(ActionEvent actionEvent) {
        this.counters[3] = this.counters[3] + 1;
        turn(4, answerFour);

    }

    public void turn(int buttonNr, Answer answer){
        if (playingMode && !gameEnded) {
            // Game is in playing mode
            if (playersTurn()) {
                if(game.answerQuestion(playerNr, buttonNr, answer, currentQuestion)) {
                    // Opponent's turn
                    switchTurn();
                    count = count + 1;
                    setQuestion(count);
                }
            }
            else {
                // It is not this player's turn yet
                infoBox("Wait till " + opponentName + " has answered", "Warning");
            }
        }
        else {
            if (gameEnded) {
                infoBox("Press Start new game", "Message");
            }
        }
    }

    @Override
    public void setPlayerNumber(int playerNr, String name) {
        // Check identification of player
        if (!this.playerName.equals(name)) {
            infoBox("ERROR: Wrong player name method setPlayerNumber()", ERROR);
            return;
        }
        this.playerNr = playerNr;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pnPlayerBlock.setText(name + "\'s block");
            }
        });
        infoBox("Player " + name + " registered", "Name");
    }

    @Override
    public void setOpponentName(int playerNr, String name) {
        // Check identification of player
        if (playerNr == this.playerNr) {
            infoBox("ERROR: Wrong player number method setOpponentName()", ERROR);
            return;
        }
        infoBox("Your opponent is " + name, "Name");
        opponentName = name;
        opponentNr = playerNr;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pnEnemyBlock.setText(opponentName + "\'s block");
            }
        });
    }

    public void showErrorMessage(int playerNr, String errorMessage) {
        // Show the error message as an alert message
        infoBox(errorMessage, ERROR);
    }

    private void notifyWhenReady() {
        // Notify that the player is ready is start the game.
        if(!game.notifyWhenReady(playerNr)) {
            infoBox("Questions have not been retrieved", ERROR);
        }
    }


    public static void infoBox(String infoMessage, String titleBar) {
        JOptionPane.showMessageDialog(null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    @Override
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void setQuestion(int count) {
        Algorithm algorithm;
        algorithm = new Algorithm(this.counters);
        Question question = algorithm.algorithmButtons(game.getQuestion(count));
        btAnswerOne.setText("");
        btAnswerTwo.setText("");
        btAnswerThree.setText("");
        btAnswerFour.setText("");
        lbQuestion.setText(question.getQuestionString());
        currentQuestion = question;
        setButtons(currentQuestion);
    }

    private void setButtons(Question question){
        for (int i = 0; i < 4; i++) {
            for (int x = 0; x < question.getAnswers().size(); x++) {
                Answer answer = question.getAnswers().get(x);
                if (i == 0 && btAnswerOne.getText().equals("") && !answer.getState().equals(UsageState.USED)) {
                    btAnswerOne.setText(answer.getAnswerString());
                    answer.setState(UsageState.USED);
                    this.answerOne = answer;
                } else if (i == 1 && btAnswerTwo.getText().equals("") && !answer.getState().equals(UsageState.USED)) {
                    btAnswerTwo.setText(question.getAnswers().get(x).getAnswerString());
                    answer.setState(UsageState.USED);
                    this.answerTwo = answer;
                } else if (i == 2 && btAnswerThree.getText().equals("") && !answer.getState().equals(UsageState.USED)) {
                    btAnswerThree.setText(question.getAnswers().get(x).getAnswerString());
                    answer.setState(UsageState.USED);
                    this.answerThree = answer;
                } else if (i == 3 && btAnswerFour.getText().equals("") && !answer.getState().equals(UsageState.USED)) {
                    btAnswerFour.setText(question.getAnswers().get(x).getAnswerString());
                    answer.setState(UsageState.USED);
                    this.answerFour = answer;
                }
            }
        }
    }

    @Override
    public void notifyStartGame(int playerNr, String startPlayerName) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            infoBox("ERROR: Wrong player number method notifyStartGame()", ERROR);
            return;
        }

        // Set playing mode and disable placing/removing of ships
        btStartGame.setDisable(true);
        btSkip.setDisable(false);
        btHint.setDisable(false);
        pnPlayerBlock.setDisable(false);
            playingMode = true;
            infoBox(startPlayerName + " starts the game off.", "Information");
    }

    /**
     * Method to switch player's turn.
     * This method is synchronized because switchTurn() may be
     * called by the Java FX Application thread or by another thread
     * handling communication with the game server.
     */
    private synchronized void switchTurn() {
        if (playerTurn == playerNr) {
            playerTurn = opponentNr;
        } else {
            playerTurn = playerNr;
        }
    }

    public void showAnswer(int playerNr, int buttonNr, AnswerState answerState) {
        // Check identification of player
        if (playerNr != this.playerNr) {
                infoBox("ERROR: Wrong player number method showSquarePlayer()", ERROR);
            return;
        }
        if(answerState.equals(answerState.CORRECT)){
            infoBox("Correct ;)", "Correct");
        } else if (answerState.equals(answerState.INCORRECT)){
            infoBox("Incorrect :(", "Incorrect");
        }
    }

    public void showAnswerOpponent(int playerNr, AnswerState answerState, int number) {
        if (playerNr != this.playerNr) {
            infoBox("ERROR: Wrong player ", ERROR);
            return;
        }
        switch (answerState) {
            case CORRECT:
                getButtonOpponent(number).setStyle("-fx-base: green;");
                break;
            case INCORRECT:
                getButtonOpponent(number).setStyle("-fx-base: red;");
                break;
            default:
                getButtonOpponent(number).setStyle("");
                break;
        }
    }

    public Button getButtonOpponent(int buttonNr) {
        Button button = new Button();
        if (buttonNr == 0 ) {
            button = btQuestionOne;
        } else if (buttonNr == 1) {
            button = btQuestionTwo;
        } else if (buttonNr == 2) {
            button = btQuestionThree;
        } else if (buttonNr == 3) {
            button = btQuestionFour;
        } else if (buttonNr == 4 || buttonNr == 5) {
            button = btQuestionFive;
        }
        return button;
    }

    private synchronized boolean playersTurn() {
        return playerNr == playerTurn;
    }

    @Override
    public void playerAnswers(int playerNr, AnswerState answerState) {
        // Check identification of player
        if (playerNr != this.playerNr) {
            infoBox("ERROR: Wrong player number method playerAnswers()", ERROR);
            return;
        }
        if (answerState.equals(AnswerState.ALLANSWERED)) {
            infoBox("Winner: " + playerName + ".\nPress Start new game to continue", "CONGRATULATIONS");
            gameEnded = true;
        }
    }

    @Override
    public void opponentAnswers(int playerNr, AnswerState answerState) {
        // Check identification of player
        if (playerNr != this.opponentNr) {
            infoBox("ERROR: Wrong player number method opponentAnswers()", ERROR);
            return;
        }
        if (answerState.equals(AnswerState.ALLANSWERED)) {
            infoBox("Winner: " + opponentName + ".\nPress Start new game to continue", "CONGRATULATIONS");
            gameEnded = true;
        }
        // Player's turn
        switchTurn();
    }
    @Override
    public int getOpponentNr() {
        return this.opponentNr;
    }
    @Override
    public void showEndMatchOpponent(int playerNr){
        infoBox("You have answered your last question", "Message");
        pnPlayerBlock.setDisable(true);
        pnEnemyBlock.setDisable(true);
        btSkip.setDisable(true);
        btHint.setDisable(true);
        btNewGame.setDisable(false);
    }

}
