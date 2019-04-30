package data.mssqlcontexts;

import data.contextinterfaces.IQuestionContext;
import domain.Answer;
import domain.Question;
import enums.AnswerState;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizQuestionMSSQLContext extends QuizMSSQLContext implements IQuestionContext {
    //region Fields
    private ResultSet data;
    private Connection connection;
    private List<Question> questions = new ArrayList<>();

    private CallableStatement stmt;
    //endregion

    //region Methods

    @Override
    public List<Question> getQuestions() {
        this.connection = super.openConnection();
        try{
            String spGetQuestions = "EXEC getQuestions";
            this.stmt = this.connection.prepareCall(spGetQuestions);
            this.data = this.stmt.executeQuery();
            while (this.data.next()) {
                    this.questions.add(new Question(this.data.getInt("Id"), this.data.getString("QuestionString"), null, AnswerState.UNANSWERED));
            }
        } catch (Exception e){
            Logger.getLogger(QuizQuestionMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            super.closeConnection();
        }
        for (Question question: this.questions) {
            List<Answer>[] ans = (List<Answer>[]) new ArrayList[5];
        ans[this.questions.indexOf(question)] = getAnswers(question.getQuestionId());
        question.setAnswers(ans[this.questions.indexOf(question)]);
    }
        return this.questions;
    }

    private List<Answer> getAnswers(int questionId){
        List<Answer> answers = new ArrayList<>();
        this.connection = super.openConnection();
        try{
            String spGetAnswers = "EXEC getAnswers ?";
            this.stmt = this.connection.prepareCall(spGetAnswers);
            this.stmt.setInt(1, questionId);
            this.data = this.stmt.executeQuery();
            while (this.data.next()) {
                answers.add(new Answer(this.data.getInt("Id"), this.data.getString("AnswerString"), this.data.getBoolean("Correct")));
            }
        } catch (Exception e){
            Logger.getLogger(QuizMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            super.closeConnection();
        }
        return answers;
    }
    //endregion
}

