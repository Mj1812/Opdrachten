package restapi.restservercontexten.authentication;

import restapi.responses.AuthenticationResponse;
import restapi.viewmodels.Login;

import javax.ws.rs.core.Response;

public interface IAuthenticationServer {
    Response loginUser(Login login);
    Response registerUser(Login login);
}
