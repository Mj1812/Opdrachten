package data.contextinterfaces;

import domain.Account;

public interface IAuthenticationContext {
    /**
     * Signs an account in
     * @param name The name of the player
     * @param password The password of the player
     * @return Account with the accountId
     */
    Account getAccountCredentials(String name, String password);

    /**
     * Signs an account up
     * @param name The name of the player
     * @param password The password of the player
     * @return Account with the new accountId
     */
    Account insertAccount(String name, String password);

}
