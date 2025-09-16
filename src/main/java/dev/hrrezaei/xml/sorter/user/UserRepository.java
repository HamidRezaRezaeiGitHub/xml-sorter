package dev.hrrezaei.xml.sorter.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username.
     */
    User findByUsername(String username);
    
    /**
     * Check if user exists by username.
     */
    boolean existsByUsername(String username);
}