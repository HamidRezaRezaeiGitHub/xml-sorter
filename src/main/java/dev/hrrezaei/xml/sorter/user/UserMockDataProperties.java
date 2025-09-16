package dev.hrrezaei.xml.sorter.user;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Configuration properties for user mock data initialization.
 */
@ConfigurationProperties(prefix = "app.user.mock-data")
@Component
@Data
public class UserMockDataProperties {

    /**
     * Flag to enable/disable mock data initialization.
     */
    private boolean enabled = false;

    /**
     * List of mock users to create.
     */
    private List<MockUser> users;

    @Data
    public static class MockUser {
        private String username;
        private String email;
        private String firstName;
        private String lastName;
    }
}