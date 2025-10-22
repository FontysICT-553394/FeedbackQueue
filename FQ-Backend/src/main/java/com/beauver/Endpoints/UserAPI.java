package com.beauver.Endpoints;

import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.StatusCodes;
import com.beauver.Security.VerifyJwt;
import com.beauver.Services.UserService;
import com.google.gson.Gson;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {

    @Inject
    UserService userService;

    @GET
    @Path("/getAll")
    @VerifyJwt
    @RunOnVirtualThread
    public String getAllUsers() {
        return new Result<>(StatusCodes.OK, User.listAll()).toJson();
    }

    @POST
    @Path("/logIn")
    @Transactional
    @RunOnVirtualThread
    public String logIn(String json){
        User user = new Gson().fromJson(json, User.class);

        String jwt = userService.logIn(user);
        if(jwt == null){
            return new Result<>(StatusCodes.UNAUTHORIZED).toJson();
        }

        return new Result<>(StatusCodes.OK, jwt).toJson();
    }

    @POST
    @Path("/register")
    @Transactional
    @RunOnVirtualThread
    public String register(String json){
        User user = new Gson().fromJson(json, User.class);

        if(!userService.register(user)){
            return new Result<>(StatusCodes.CONFLICT, "Name or email already exists").toJson();
        }

        return new Result<>(StatusCodes.CREATED).toJson();
    }

}
