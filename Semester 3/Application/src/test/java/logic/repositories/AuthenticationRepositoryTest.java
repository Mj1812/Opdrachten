package logic.repositories;

import data.testcontexts.QuizAuthenticationTestContext;
import domain.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import restapi.viewmodels.Login;

public class AuthenticationRepositoryTest {
    private AuthenticationRepository authenticationRepository;

    @Before
    public void initialize() {
        this.authenticationRepository = new AuthenticationRepository(new QuizAuthenticationTestContext());
    }

    @Test
    public void testLoginAccount() {
        Login login = new Login("Marco", "Test");
        Account account = this.authenticationRepository.loginAccount(login);
        Assert.assertEquals("Id", 1 , account.getId());
        Assert.assertEquals("Username", "Marco", account.getName());
    }

    @Test
    public void testRegisterAccount() {
        Login login = new Login("Marco", "Test");
        Account account = this.authenticationRepository.registerAccount(login);
        Assert.assertEquals("Id", 4 , account.getId());
        Assert.assertEquals("Admin", "Marco", account.getName());
    }

}
