package com.beauver;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class Main {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String root() {
        return "{\"status\":\"200\",\"message\":\"OK\"}";
    }
}