package data.testcontexts;

import data.contextinterfaces.IAuthenticationContext;
import domain.Account;

import java.util.ArrayList;
import java.util.List;

public class QuizAuthenticationTestContext implements IAuthenticationContext {
    //region Fields
    private List<Account> accounts = new ArrayList<>();

    //endregion

    //region Constructor
    public QuizAuthenticationTestContext() {
        this.accounts.add(new Account(1,"Marco", "$2a$10$wRw6mvayrAODxLbm9rNwyOSn22uTcqHPFZIwm7zLqaIPgVq2Xh322"));
        this.accounts.add(new Account(2,"Mark", "Hallo123"));
        this.accounts.add(new Account(3, "Jelle", "School123"));
    }

    @Override
    public Account getAccountCredentials(String name, String password) {
        Account classAccount= null;
            for (Account user : this.accounts) {
                if (user.getName().equals(name)) {
                    classAccount = user;
                }
            }
        return classAccount;
    }

    @Override
    public Account insertAccount(String name, String password) {
        Account classAccount = null;
        if (!accounts.contains(name)) {
            classAccount = new Account(4,name, password);
            accounts.add(classAccount);
        }
        return classAccount;
    }
    //endregion
}
