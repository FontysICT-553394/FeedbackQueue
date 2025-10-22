package com.beauver.Security;

import com.beauver.Classes.Result;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtInterceptor implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Singleton
    private final JwtUtil jwtUtil;

    @Inject
    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        if (resourceInfo == null) {
            return;
        }

        boolean methodAnnotated = resourceInfo.getResourceMethod() != null &&
                resourceInfo.getResourceMethod().isAnnotationPresent(VerifyJwt.class);
        boolean classAnnotated = resourceInfo.getResourceClass() != null &&
                resourceInfo.getResourceClass().isAnnotationPresent(VerifyJwt.class);

        if (!methodAnnotated && !classAnnotated) {
            return;
        }

        String auth = ctx.getHeaderString("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new Result<>(Response.Status.UNAUTHORIZED.getStatusCode(), "Missing or invalid Authorization header").toJson())
                    .build());
            return;
        }

        String token = auth.substring(7).trim();
        if (!jwtUtil.validateToken(token)) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new Result<>(Response.Status.UNAUTHORIZED.getStatusCode(), "Invalid token").toJson())
                    .build());
            return;
        }

        String userId = jwtUtil.getIdFromToken(token);
        ctx.setProperty("userId", userId);
    }
}