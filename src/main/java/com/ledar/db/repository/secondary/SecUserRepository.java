package com.ledar.db.repository.secondary;

import com.ledar.db.domain.secondary.SecUser;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link SecUser} entity.
 */
@Repository
public interface SecUserRepository extends JpaRepository<SecUser, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<SecUser> findOneByActivationKey(String activationKey);
    List<SecUser> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<SecUser> findOneByResetKey(String resetKey);
    Optional<SecUser> findOneByEmailIgnoreCase(String email);
    Optional<SecUser> findOneByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<SecUser> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<SecUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<SecUser> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
