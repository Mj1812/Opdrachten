package logic.repositories.helperclasses;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean checkPassword(String password, String hashed){
        return BCrypt.checkpw(password, hashed);
    }
}
