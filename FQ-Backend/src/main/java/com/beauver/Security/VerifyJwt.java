package com.beauver.Security;

import jakarta.ws.rs.HttpMethod;

import java.lang.annotation.*;

/**
 * This annotation automatically checks for a valid JWT token. If it's invalid it returns a 401 Unauthorized response.
 *
 * @author Beau
 * @see JwtInterceptor
 * @see JwtUtil
 * @see com.beauver.Classes.Result
 * @returns results with status code 401 if JWT is invalid, nothing when it is valid
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VerifyJwt {
}
