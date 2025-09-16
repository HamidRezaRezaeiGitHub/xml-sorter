package dev.hrrezaei.xml.sorter.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for UserMockDataInitializer.
 * This test verifies that the configuration properties are correctly bound
 * and that mock users are properly created in the database.
 */
@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "app.user.mock-data.enabled=true",
    "app.user.mock-data.users[0].username=integration_user1",
    "app.user.mock-data.users[0].email=integration1@test.com",
    "app.user.mock-data.users[0].first-name=Integration",
    "app.user.mock-data.users[0].last-name=User1",
    "app.user.mock-data.users[1].username=integration_user2", 
    "app.user.mock-data.users[1].email=integration2@test.com",
    "app.user.mock-data.users[1].first-name=Integration",
    "app.user.mock-data.users[1].last-name=User2"
})
class UserMockDataInitializerIntegrationTest {

    @Autowired
    private UserMockDataProperties userMockDataProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMockDataInitializer userMockDataInitializer;

    @Test
    void shouldBindConfigurationPropertiesCorrectly() {
        // Verify that configuration properties are bound correctly
        assertThat(userMockDataProperties.isEnabled()).isTrue();
        assertThat(userMockDataProperties.getUsers()).hasSize(2);
        
        // Verify first user properties
        UserMockDataProperties.MockUser firstUser = userMockDataProperties.getUsers().get(0);
        assertThat(firstUser.getUsername()).isEqualTo("integration_user1");
        assertThat(firstUser.getEmail()).isEqualTo("integration1@test.com");
        assertThat(firstUser.getFirstName()).isEqualTo("Integration");
        assertThat(firstUser.getLastName()).isEqualTo("User1");
        
        // Verify second user properties
        UserMockDataProperties.MockUser secondUser = userMockDataProperties.getUsers().get(1);
        assertThat(secondUser.getUsername()).isEqualTo("integration_user2");
        assertThat(secondUser.getEmail()).isEqualTo("integration2@test.com");
        assertThat(secondUser.getFirstName()).isEqualTo("Integration");
        assertThat(secondUser.getLastName()).isEqualTo("User2");
    }

    @Test
    void shouldCreateMockUsersInDatabase() {
        // Verify that mock users were created in the database
        assertThat(userService.count()).isGreaterThanOrEqualTo(2);
        
        // Verify specific users exist
        User user1 = userService.findByUsername("integration_user1");
        assertThat(user1).isNotNull();
        assertThat(user1.getEmail()).isEqualTo("integration1@test.com");
        assertThat(user1.getFirstName()).isEqualTo("Integration");
        assertThat(user1.getLastName()).isEqualTo("User1");
        
        User user2 = userService.findByUsername("integration_user2");
        assertThat(user2).isNotNull();
        assertThat(user2.getEmail()).isEqualTo("integration2@test.com");
        assertThat(user2.getFirstName()).isEqualTo("Integration");
        assertThat(user2.getLastName()).isEqualTo("User2");
    }

    @Test
    void shouldNotCreateDuplicateUsers() {
        // Get initial count
        long initialCount = userService.count();
        
        // Run the initializer again
        userMockDataInitializer.run();
        
        // Verify count hasn't increased (no duplicates created)
        assertThat(userService.count()).isEqualTo(initialCount);
    }
}