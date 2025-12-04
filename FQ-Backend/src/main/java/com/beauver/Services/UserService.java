package com.beauver.Services;

import com.beauver.Classes.User;
import com.beauver.Interfaces.UserRepo;
import com.beauver.Security.JwtUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * This service handles user login and registration business logic
 *
 * @author Beau
 * @see UserRepo
 * @see JwtUtil
 */
@ApplicationScoped
public class UserService {

    @Inject
    UserRepo userRepo;

    @Inject
    JwtUtil jwtUtil;

    /**
     * This logs in a user and returns a JWT token if successful
     * @param user User to log in
     * @return JWT token if login was successful, null otherwise
     */
    public String logIn(User user){
        if(!userRepo.verifyLogin(user.email, user.password)){
            return null;
        }

        User foundUser = userRepo.find("email", user.email).firstResult();
        return getJwtToken(String.valueOf(foundUser.id));
    }

    /**
     * This registers a user to the database
     * @param user User to register
     * @return true if registration was successful, false if username or email is already taken
     */
    public boolean register(User user){
        if(userRepo.count("name", user.name) > 0){
            return false;
        }
        if(userRepo.count("email", user.email) > 0){
            return false;
        }

        user.password = BcryptUtil.bcryptHash(user.password, 12);
        userRepo.persist(user);
        return true;
    }

    private String getJwtToken(String id){
        return jwtUtil.generateToken(id);
    }

}
