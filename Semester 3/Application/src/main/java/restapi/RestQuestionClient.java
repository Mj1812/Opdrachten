package restapi;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import restapi.responses.QuestionResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestQuestionClient {
    //region Fields
    private static final String QUESTIONLISTURL = "http://localhost:8090/questions/getQuestions";
    //endregion

    //region Methods
    public QuestionResponse executeQueryGet() {

        // Build the query for the REST service
        Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.INFO, "[Query Get] : " + QUESTIONLISTURL);
        // Execute the HTTP GET request
        HttpGet httpGet = new HttpGet(QUESTIONLISTURL);
        return executeHttpUriRequest(httpGet);
    }

    private QuestionResponse executeHttpUriRequest(HttpUriRequest httpUriRequest) {
        Gson gson = new Gson();
        QuestionResponse questionResponse = new QuestionResponse();
        // Execute the HttpUriRequest
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.INFO, "[Status Line] : " + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            final String entityString = EntityUtils.toString(entity);
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.INFO, "[Entity] : " , entityString);
            questionResponse = gson.fromJson(entityString, QuestionResponse.class);
        } catch (IOException e) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, "IOException : " + e.toString());
        } catch (JsonSyntaxException e) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, "JsonSyntaxException : " + e.toString());
        }

        return questionResponse;
    }
    //endregion
}
