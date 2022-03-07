package com.ledar.db.repository.secondary;

import com.ledar.db.domain.secondary.SecAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link SecAuthority} entity.
 */
public interface SecAuthorityRepository extends JpaRepository<SecAuthority, String> {}
