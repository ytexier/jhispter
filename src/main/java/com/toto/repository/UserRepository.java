package com.toto.repository;

import com.toto.domain.User;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.time.Instant;

import static com.toto.config.Constants.ID_DELIMITER;

/**
 * Spring Data Couchbase repository for the User entity.
 */
@Repository
public interface UserRepository extends N1qlCouchbaseRepository<User, String> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(Instant dateTime);

    Optional<User> findOneByResetKey(String resetKey);

    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneByEmailIgnoreCase(String email);

    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    default Optional<User> findOneByLogin(String login) {
        return Optional.ofNullable(findOne(User.PREFIX + ID_DELIMITER + login));
    }

    Page<User> findAllByLoginNot(Pageable pageable, String login);
}
