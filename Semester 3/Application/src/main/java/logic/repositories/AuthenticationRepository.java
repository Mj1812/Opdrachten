package logic.repositories;

import data.contextinterfaces.IAuthenticationContext;
import domain.Account;
import logic.repositories.helperclasses.PasswordHasher;
import restapi.viewmodels.Login;

public class AuthenticationRepository {
    //region Fields
    private IAuthenticationContext iAuthenticationContext;
    private PasswordHasher hasher = new PasswordHasher();
    //endregion

    //region Constructors
    public AuthenticationRepository(IAuthenticationContext iAuthenticationContext){
        this.iAuthenticationContext = iAuthenticationContext;

    }
    //endregion

    //region Methods
    public Account loginAccount(Login login){
        Account account = new Account(-1, "");
        Account filledAccount;
        filledAccount = iAuthenticationContext.getAccountCredentials(login.getUsername(), hasher.hashPassword(login.getPassword()));
        if(hasher.checkPassword(login.getPassword(), filledAccount.getPassword())){
            account = filledAccount;
            return account;
        } else{
            return account;
        }
    }

    public Account registerAccount(Login login){
            return iAuthenticationContext.insertAccount(login.getUsername(), hasher.hashPassword(login.getPassword()));
    }
    //endregion
}
