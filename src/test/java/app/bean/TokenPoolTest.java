package app.bean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TokenPoolTest {

    private TokenPool tokenPool = new TokenPool();
    private String mockToken = "0acd44db-82b9-437a-b3a2-29ec15bd1d35";

    @BeforeEach
    public void setUp() {
        tokenPool.login(1L, mockToken);
    }

    @AfterEach
    public void revert() {
        tokenPool.logout(mockToken);
    }

    @Test
    public void testGetUserIdByToken() {
        Assertions.assertEquals(1, (long) tokenPool.getUserIdByToken(mockToken));
    }

    @Test
    public void testContainsToken() {
        Assertions.assertTrue(tokenPool.containsToken(mockToken));
        tokenPool.logout(mockToken);
        Assertions.assertFalse(tokenPool.containsToken(mockToken));
    }

    @Test
    public void manuallyTestGenerateToken() {
        System.out.println(tokenPool.generateToken());
    }

    @Test
    public void testTokenExpiryValidation() {
        String token = tokenPool.generateToken();
        Assertions.assertTrue(tokenPool.validateTokenExpiry(token, LocalDateTime.now()));
    }

    @Test
    public void testTokenExpiryValidationAssumingExpired() {
        String token = tokenPool.generateToken();
        Assertions.assertFalse(tokenPool.validateTokenExpiry(token, LocalDateTime.now().plusHours(3)));
    }
}
