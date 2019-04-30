package restapi.restservercontexten.questions;

import restapi.responses.QuestionResponse;

import javax.ws.rs.core.Response;

public interface IQuestionServer {
    Response getQuestions();
}
