package com.beauver.Endpoints;

import com.beauver.Classes.User;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/api/users/")
public class UserAPI {

    @GET
    public String getAllUsers() {
        return User.findAll().toString();
    }

}
