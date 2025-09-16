package dev.hrrezaei.xml.sorter.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Component that initializes mock user data based on configuration properties.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserMockDataInitializer implements CommandLineRunner {

    private final UserMockDataProperties userMockDataProperties;
    private final UserService userService;

    @Override
    public void run(String... args) {
        if (!userMockDataProperties.isEnabled()) {
            log.debug("User mock data initialization is disabled");
            return;
        }

        if (userMockDataProperties.getUsers() == null || userMockDataProperties.getUsers().isEmpty()) {
            log.warn("No mock users configured");
            return;
        }

        log.info("Initializing mock user data...");
        
        for (UserMockDataProperties.MockUser mockUser : userMockDataProperties.getUsers()) {
            if (!userService.existsByUsername(mockUser.getUsername())) {
                User user = new User(
                    mockUser.getUsername(),
                    mockUser.getEmail(),
                    mockUser.getFirstName(),
                    mockUser.getLastName()
                );
                
                userService.save(user);
                log.info("Created mock user: {}", user.getUsername());
            } else {
                log.debug("User {} already exists, skipping", mockUser.getUsername());
            }
        }
        
        log.info("Mock user data initialization completed. Total users: {}", userService.count());
    }
}