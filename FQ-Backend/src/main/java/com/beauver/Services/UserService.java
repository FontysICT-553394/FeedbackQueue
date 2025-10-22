package com.beauver.Services;

import com.beauver.Classes.User;
import com.beauver.Interfaces.UserRepo;
import com.beauver.Security.JwtUtil;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepo userRepo;

    @Inject
    JwtUtil jwtUtil;

    public String logIn(User user){
        if(!userRepo.verifyLogin(user.email, user.password)){
            return null;
        }

        User foundUser = userRepo.find("email", user.email).firstResult();
        return getJwtToken(foundUser.id.toString());
    }

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
