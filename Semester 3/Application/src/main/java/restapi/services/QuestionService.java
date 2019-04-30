package restapi.services;

import com.google.gson.Gson;
import data.mssqlcontexts.QuizQuestionMSSQLContext;
import domain.Question;
import logic.repositories.QuestionRepository;
import restapi.responses.QuestionResponse;
import restapi.restservercontexten.questions.IQuestionServer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("questions")
public class QuestionService implements IQuestionServer {
    //region Fields
    private QuestionRepository repository = new QuestionRepository((new QuizQuestionMSSQLContext()));
    private QuestionResponse questionResponse = new QuestionResponse();
    //endregion

    //region Methods
    @GET
    @Path("/getQuestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuestions() {
        Gson gson = new Gson();
        List<Question> questions;
        questions = repository.getQuestions();
        if(questions != null) {
            this.questionResponse.setQuestions(questions);
        } else {
            this.questionResponse.setQuestions(null);
            this.questionResponse.setMessage("There were no questions retrieved.");
        }
        String output = gson.toJson(this.questionResponse);
        return Response.status(200).entity(output).build();
    }
    //endregion
}
