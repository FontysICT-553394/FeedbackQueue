package com.beauver.tests.Unit.Account;

import com.beauver.Classes.User;
import com.beauver.Security.JwtUtil;
import com.beauver.Services.UserService;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class AccountTests {

    @Inject
    UserService userService;
    @Inject
    JwtUtil jwtUtil;

    @BeforeEach
    @Transactional
    public void cleanup() {
        User.deleteAll();
    }

    @Test
    @Transactional
    public void testRegisterSuccess() {
        // Arrange
        User user = new User();
        user.name = "testuser";
        user.email = "test@example.com";
        user.password = "password123";

        // Act
        boolean result = userService.register(user);

        // Assert
        assertTrue(result);
        User savedUser = User.find("email", user.email).firstResult();
        assertNotNull(savedUser);
        assertEquals("testuser", savedUser.name);
        assertEquals("test@example.com", savedUser.email);
        assertTrue(BcryptUtil.matches("password123", savedUser.password));
    }

    @ParameterizedTest
    @CsvSource({
            // Email exists
            "testuser, test@example.com, existingpassword, testuser1, test@example.com, password123",
            // Username exists
            "testuser, test2@example.com, existingpassword, testuser, test@example.com, password123"
    })
    @Transactional
    public void testRegister_Fail_DuplicateCredentials(String existingName, String existingEmail, String existingPassword,
                                                       String newName, String newEmail, String newPassword) {
        // Arrange
        User existingUser = new User();
        existingUser.name = existingName;
        existingUser.email = existingEmail;
        existingUser.password = BcryptUtil.bcryptHash(existingPassword);
        existingUser.persist();

        User user = new User();
        user.name = newName;
        user.email = newEmail;
        user.password = newPassword;

        // Act
        boolean result = userService.register(user);

        // Assert
        assertFalse(result);
    }

    @Test
    @Transactional
    public void testLogin_Success() {
        // Arrange
        User user = new User();
        user.name = "testuser";
        user.email = "test@example.com";
        user.password = "password123";
        userService.register(user);

        User loginUser = new User();
        loginUser.email = "test@example.com";
        loginUser.password = "password123";

        // Act
        String jwt = userService.logIn(loginUser);
        String userIdJwt = jwtUtil.getIdFromToken(jwt);
        User loggedInUser = User.findById(Long.parseLong(userIdJwt));

        // Assert
        assertNotNull(jwt);
        assertNotNull(userIdJwt);
        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.name);
        assertEquals("test@example.com", loggedInUser.email);
    }

    @ParameterizedTest
    @CsvSource({
            //wrong password
            "testuser, test@example.com, password123, test@example.com, wrongpassword",
            //wrong email
            "anotheruser, another@example.com, securepass456, another@example.net, securepass456",
            //both wrong
            "user3, user3@example.com, mypassword789, doesntexist@example.com, nopassword"
    })
    @Transactional
    public void testLogin_Fail_InvalidCredentials(String name, String email, String password, String email2, String password2) {
        // Arrange
        User user = new User();
        user.name = name;
        user.email = email;
        user.password = password;
        userService.register(user);

        User loginUser = new User();
        loginUser.email = email2;
        loginUser.password = password2;

        // Act
        String jwt = userService.logIn(loginUser);

        // Assert
        assertNull(jwt);
    }
}
