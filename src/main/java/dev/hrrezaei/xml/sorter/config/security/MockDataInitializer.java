package dev.hrrezaei.xml.sorter.config.security;

import dev.hrrezaei.xml.sorter.user.UserMockDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * Security configuration class that handles mock data initialization.
 * This class is conditionally created only when UserMockDataInitializer bean is available.
 */
@Component
@ConditionalOnBean(UserMockDataInitializer.class)
@Slf4j
public class MockDataInitializer {

    private final UserMockDataInitializer userMockDataInitializer;

    public MockDataInitializer(UserMockDataInitializer userMockDataInitializer) {
        this.userMockDataInitializer = userMockDataInitializer;
        log.info("MockDataInitializer initialized with UserMockDataInitializer bean");
    }

    /**
     * Get the user mock data initializer instance.
     */
    public UserMockDataInitializer getUserMockDataInitializer() {
        return userMockDataInitializer;
    }
}