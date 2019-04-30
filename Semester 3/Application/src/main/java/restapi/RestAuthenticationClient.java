package restapi;

import com.google.gson.Gson;
import restapi.responses.AuthenticationResponse;
import restapi.viewmodels.Login;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestAuthenticationClient {
    //region Fields
    private static final String LOGINURL = "http://localhost:8090/authenticate/login";
    private static final String REGISTERURL = "http://localhost:8090/authenticate/register";
    //endregion

    //region Methods
    public AuthenticationResponse loginUserRest(Login login){
        String output = "";
        Gson gson = new Gson();
        try {
            URL url = new URL(this.LOGINURL);
            String input = gson.toJson(login);

            output = this.restAPI(url, input);
        } catch (Exception e){
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return gson.fromJson(output, AuthenticationResponse.class);
    }

    public AuthenticationResponse registerUserRest(Login login){
        String output = "";
        Gson gson = new Gson();
        try {
            URL url = new URL(this.REGISTERURL);
            String input = gson.toJson(login);

            output = this.restAPI(url, input);
        } catch (Exception e){
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, null, e);
        }
        return gson.fromJson(output, AuthenticationResponse.class);
    }




    private String restAPI(URL url, String input) {
        String output = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);

            OutputStream outputStream = connection.getOutputStream();
            if(!input.equals("")) {
                outputStream.write(input.getBytes());
            }
            outputStream.flush();

            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed: HTTP error code is " + connection.getResponseCode());
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while((line = bufferedReader.readLine()) != null) {
                output += line;
            }

            connection.disconnect();
        } catch (Exception e) {
            Logger.getLogger(RestAuthenticationClient.class.getName()).log(Level.SEVERE, e.getMessage());
        }
        return output;
    }
    //endregion


}
