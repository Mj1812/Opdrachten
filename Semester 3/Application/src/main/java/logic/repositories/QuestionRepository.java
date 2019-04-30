package logic.repositories;

import data.contextinterfaces.IQuestionContext;
import domain.Question;

import java.util.List;

public class QuestionRepository {
    //region Fields
    private IQuestionContext iQuestionContext;
    //endregion

    //region Constructors
    public QuestionRepository(IQuestionContext iQuestionContext){
        this.iQuestionContext = iQuestionContext;
    }
    //endregion

    //region Methods
    public List<Question> getQuestions(){
        return iQuestionContext.getQuestions();
    }
}
