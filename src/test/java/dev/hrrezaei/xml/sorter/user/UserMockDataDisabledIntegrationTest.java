package dev.hrrezaei.xml.sorter.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for UserMockDataInitializer when disabled.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "app.user.mock-data.enabled=false"
})
class UserMockDataDisabledIntegrationTest {

    @Autowired
    private UserMockDataProperties userMockDataProperties;

    @Autowired 
    private UserService userService;

    @Test
    void shouldNotCreateUsersWhenDisabled() {
        // Verify that mock data is disabled
        assertThat(userMockDataProperties.isEnabled()).isFalse();
        
        // Verify no users were created
        assertThat(userService.count()).isEqualTo(0);
    }
}