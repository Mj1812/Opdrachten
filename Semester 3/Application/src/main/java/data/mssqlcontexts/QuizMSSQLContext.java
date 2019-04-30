package data.mssqlcontexts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuizMSSQLContext {
    //region Fields
    private Connection connection;
    //endregion

    //region Constructors
    //endregion

    //region Methods
    public Connection openConnection(){
        try{
            if (connection == null || connection.isClosed()){
                String userName = "dbi380705_uiz";
                String password = "DeQuiz";

                String url = "jdbc:sqlserver://mssql.fhict.local;database=dbi380705_uiz";
                connection = DriverManager.getConnection(url, userName, password);
            }
        } catch (Exception e){
            Logger.getLogger(QuizMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        }
        return connection;
    }

    public void closeConnection(){
        try{
            if (connection != null && !connection.isClosed()){
                connection.close();
            }
        } catch (Exception e){
            Logger.getLogger(QuizMSSQLContext.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    //endregion
}
