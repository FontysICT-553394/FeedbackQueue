package com.beauver.Interfaces;

import io.quarkus.elytron.security.common.BcryptUtil;
import com.beauver.Classes.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepo implements PanacheRepository<User> {

    /**
     * This verifies a user's login credentials
     * @param email User's email
     * @param password User's password
     * @return true if login is successful, false otherwise
     */
    public boolean verifyLogin(String email, String password) {
        User user = find("email", email).firstResult();
        if (user == null || user.password == null) {
            return false;
        }

        return BcryptUtil.matches(password, user.password);
    }

}
