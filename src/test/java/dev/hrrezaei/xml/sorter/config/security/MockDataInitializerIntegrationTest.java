package dev.hrrezaei.xml.sorter.config.security;

import dev.hrrezaei.xml.sorter.user.UserMockDataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for MockDataInitializer conditional bean.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "app.user.mock-data.enabled=true",
    "app.user.mock-data.users[0].username=security_test_user",
    "app.user.mock-data.users[0].email=security@test.com",
    "app.user.mock-data.users[0].first-name=Security",
    "app.user.mock-data.users[0].last-name=Test"
})
class MockDataInitializerIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void shouldCreateMockDataInitializerWhenUserMockDataInitializerExists() {
        // Verify that UserMockDataInitializer bean exists
        assertThat(applicationContext.containsBean("userMockDataInitializer")).isTrue();
        
        // Verify that MockDataInitializer bean exists (conditional on UserMockDataInitializer)
        assertThat(applicationContext.containsBean("mockDataInitializer")).isTrue();
        
        // Verify we can get the MockDataInitializer bean
        MockDataInitializer mockDataInitializer = applicationContext.getBean(MockDataInitializer.class);
        assertThat(mockDataInitializer).isNotNull();
        
        // Verify it has access to the UserMockDataInitializer
        UserMockDataInitializer userMockDataInitializer = mockDataInitializer.getUserMockDataInitializer();
        assertThat(userMockDataInitializer).isNotNull();
    }
}