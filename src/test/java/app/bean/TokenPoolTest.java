package app.bean;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TokenPoolTest {

    static final TokenPool tokenPool = new TokenPool();
    static final String mockToken = "0acd44db-82b9-437a-b3a2-29ec15bd1d35";

    @BeforeAll
    static void setUp() {
        tokenPool.login(1L, mockToken);
    }

    @Test
    public void testContainsToken() {
        Assert.assertTrue(tokenPool.containsToken(mockToken));
        tokenPool.logout(mockToken);
        Assert.assertFalse(tokenPool.containsToken(mockToken));
    }

}
