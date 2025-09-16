package dev.hrrezaei.xml.sorter.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for user operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    /**
     * Save a user to the database.
     */
    public User save(User user) {
        log.debug("Saving user: {}", user.getUsername());
        return userRepository.save(user);
    }

    /**
     * Find all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Find user by username.
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Check if user exists by username.
     */
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Get total count of users.
     */
    public long count() {
        return userRepository.count();
    }
}