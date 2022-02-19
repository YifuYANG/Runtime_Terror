package app.bean;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    public void testGetIdByToken() {
        //tokenPool.login(1L, mockToken);
        Assertions.assertEquals(1, (long) tokenPool.getUserIdByToken(mockToken));
    }

    @Test
    public void testContainsToken() {
        Assertions.assertTrue(tokenPool.containsToken(mockToken));
        tokenPool.logout(mockToken);
        Assertions.assertFalse(tokenPool.containsToken(mockToken));
    }

}
