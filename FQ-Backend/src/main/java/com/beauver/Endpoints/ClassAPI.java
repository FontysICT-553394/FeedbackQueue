package com.beauver.Endpoints;


import com.beauver.Classes.Class;
import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.Role;
import com.beauver.Enums.StatusCodes;
import com.beauver.Security.JwtUtil;
import com.beauver.Security.VerifyJwt;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/classes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClassAPI {

    @Inject
    JwtUtil jwtUtil;

    @GET
    @VerifyJwt
    @Path("/getAll")
    @RunOnVirtualThread
    public String getAllClasses() {
        List<Class> classes = Class.listAll();

        return new Result<>(StatusCodes.OK, classes).toJson();
    }

    @POST
    @VerifyJwt
    @Transactional
    @RunOnVirtualThread
    @Path("/joinClass/{classId}")
    public String joinClass(@PathParam("classId") Long classId, @HeaderParam("Authorization") String authorization) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        User user = User.findById(userId);
        Class c = Class.findById(classId);
        if(c == null){
            return new Result<>(StatusCodes.NOT_FOUND, "Class not found").toJson();
        }

        user.classEntity = c;
        user.persist();

        return new Result<>(StatusCodes.OK).toJson();
    }

    @GET
    @VerifyJwt
    @RunOnVirtualThread
    @Path("/getYourClass")
    public String getYourClass(@HeaderParam("Authorization") String authorization) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        User user = User.findById(userId);
        if(user.classEntity == null){
            return new Result<>(StatusCodes.NOT_FOUND, "User is not assigned to any class").toJson();
        }

        Long classId = user.classEntity.id;
        Class c = Class.findById(classId);

        if(c == null){
            return new Result<>(StatusCodes.NOT_FOUND, "Class not found").toJson();
        }
        return new Result<>(StatusCodes.OK, c).toJson();
    }
}
