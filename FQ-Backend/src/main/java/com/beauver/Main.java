package com.beauver;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Main {

    @GET
    public String root() {
        return "{\"status\":\"200\",\"message\":\"OK\"}";
    }
}