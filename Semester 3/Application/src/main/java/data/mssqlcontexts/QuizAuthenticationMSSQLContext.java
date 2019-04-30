package data.mssqlcontexts;

import data.contextinterfaces.IAuthenticationContext;
import domain.Account;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizAuthenticationMSSQLContext extends QuizMSSQLContext implements IAuthenticationContext {
    //region Fields
    private ResultSet data;
    private Connection connection;
    private Account account;
    private CallableStatement stmt;
    //endregion

    //region Methods
    public Account getAccountCredentials(String name, String password) {
        this.connection = super.openConnection();
        try{
            String spLogin = "EXEC LoginAccount ?";
            this.stmt = this.connection.prepareCall(spLogin);
            this.stmt.setString(1, name);
            this.data = this.stmt.executeQuery();
            while (this.data.next()) {
                this.account = new Account(this.data.getInt("Id"), this.data.getString("Username"), this.data.getString("Password"));
            }
        } catch (Exception e){
            Logger.getLogger(QuizAuthenticationMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            super.closeConnection();
        }
        return this.account;
    }

    public Account insertAccount(String name, String password) {
        this.connection = super.openConnection();
        try{
            String spRegister = "EXEC RegisterAccount ?, ?";
            this.stmt = this.connection.prepareCall(spRegister);
            this.stmt.setString(1, name);
            this.stmt.setString(2, password);
            this.data = this.stmt.executeQuery();
            while (this.data.next()) {
                this.account = new Account(this.data.getInt("Id"), this.data.getString("Username"));
            }
        } catch (Exception e){
            Logger.getLogger(QuizAuthenticationMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            super.closeConnection();
        }
        return this.account;
    }
    //endregion
}
