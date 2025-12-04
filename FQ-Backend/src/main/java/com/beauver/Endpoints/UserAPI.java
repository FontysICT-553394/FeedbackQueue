package com.beauver.Endpoints;

import com.beauver.Classes.AuthTokens;
import com.beauver.Classes.RefreshRequest;
import com.beauver.Classes.Result;
import com.beauver.Classes.User;
import com.beauver.Enums.Role;
import com.beauver.Enums.StatusCodes;
import com.beauver.Security.JwtUtil;
import com.beauver.Security.VerifyJwt;
import com.beauver.Services.UserService;
import com.google.gson.Gson;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAPI {

    @Inject
    UserService userService;
    @Inject
    JwtUtil jwtUtil;

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

        String accessJwt = userService.logIn(user);
        if(accessJwt == null){
            return new Result<>(StatusCodes.UNAUTHORIZED).toJson();
        }

        String userId = jwtUtil.getIdFromToken(accessJwt);
        String refreshToken = jwtUtil.generateRefreshToken(userId);

        AuthTokens tokens = new AuthTokens(accessJwt, refreshToken);
        return new Result<>(StatusCodes.OK, tokens).toJson();
    }

    @POST
    @Path("/refresh")
    @RunOnVirtualThread
    public String refresh(String json) {
        RefreshRequest req = new Gson().fromJson(json, RefreshRequest.class);
        if (req == null || req.refreshToken == null || req.refreshToken.trim().isEmpty()) {
            return new Result<>(StatusCodes.BAD_REQUEST, "Missing refreshToken").toJson();
        }

        String refresh = req.refreshToken.trim();
        if (!jwtUtil.validateRefreshToken(refresh)) {
            return new Result<>(StatusCodes.UNAUTHORIZED, "Invalid or expired refresh token").toJson();
        }

        String userId = jwtUtil.getIdFromRefreshToken(refresh);

        String newAccess = jwtUtil.generateToken(userId);
        String newRefresh = jwtUtil.generateRefreshToken(userId);

        AuthTokens tokens = new AuthTokens(newAccess, newRefresh);
        return new Result<>(StatusCodes.OK, tokens).toJson();
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

    @GET
    @VerifyJwt
    @Path("/getAllTeachers")
    @RunOnVirtualThread
    public String getAllTeachers() {
        List<User> teachers = User.list("role", Role.TEACHER);

        for (User teacher : teachers) {
            teacher.password = null;
            teacher.email = null;
        }

        return new Result<>(StatusCodes.OK, teachers).toJson();
    }

    @GET
    @VerifyJwt
    @Path("/getAllYourTeachers")
    @RunOnVirtualThread
    public String getAllYourTeachers(@HeaderParam("Authorization") String authorization) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        User student = User.findById(userId);
        if(student.classEntity == null){
            return new Result<>(StatusCodes.NOT_FOUND, "User is not assigned to any class").toJson();
        }

        var teachers = User.getEntityManager()
                .createNativeQuery("SELECT u.* FROM users u INNER JOIN class_teachers ct ON u.id = ct.teacher_id WHERE ct.class_id = ?1", User.class)
                .setParameter(1, student.classEntity.id)
                .getResultList();

        for (User teacher : (List<User>) teachers) {
            teacher.password = null;
            teacher.email = null;
        }

        return new Result<>(StatusCodes.OK, teachers).toJson();
    }

    @GET
    @Path("/getYourself")
    @VerifyJwt
    @RunOnVirtualThread
    public String getUser(@HeaderParam("Authorization") String authorization) {
        String userId = jwtUtil.getIdFromHeader(authorization);

        User user = User.findById(userId);
        user.password = null; // Hide password
        return new Result<>(StatusCodes.OK, user).toJson();
    }
}
