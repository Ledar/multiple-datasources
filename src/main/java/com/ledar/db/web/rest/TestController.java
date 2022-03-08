package com.ledar.db.web.rest;

import com.ledar.db.domain.primary.Authority;
import com.ledar.db.domain.secondary.SecAuthority;
import com.ledar.db.repository.primary.AuthorityRepository;
import com.ledar.db.repository.primary.UserRepository;
import com.ledar.db.repository.secondary.SecAuthorityRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;

@RestController
@RequestMapping("/api")
public class TestController {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final SecAuthorityRepository secAuthorityRepository;
    private final DataSource dataSource;

    public TestController(UserRepository userRepository, AuthorityRepository authorityRepository, SecAuthorityRepository secAuthorityRepository, DataSource dataSource) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.secAuthorityRepository = secAuthorityRepository;
        this.dataSource = dataSource;
    }

    @RequestMapping("/test")
    public String test() throws SQLException {

        dataSource.getLoginTimeout();

        Authority authority = new Authority();
        authority.setName("ROLE_1");
        authorityRepository.save(authority);

        SecAuthority secAuthority = new SecAuthority();
        secAuthority.setName("ROLE_2");
        secAuthorityRepository.save(secAuthority);

        System.out.println(authorityRepository.findAll());
        System.out.println(secAuthorityRepository.findAll());

        userRepository.deleteAll();
        authorityRepository.deleteAll();
        secAuthorityRepository.deleteAll();

        return "OK";
    }
}
