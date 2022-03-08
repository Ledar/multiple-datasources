package com.ledar.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.h2.H2ConfigurationHelper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ledar.db.repository.primary", entityManagerFactoryRef = "primaryEntityManagerFactory", transactionManagerRef = "primaryTransactionManager")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class PrimaryDatabaseConfiguration {

    private final Logger log = LoggerFactory.getLogger(PrimaryDatabaseConfiguration.class);

    private final Environment env;

    private final JpaHibernateProperties jpaHibernateProperties;

    public PrimaryDatabaseConfiguration(Environment env, JpaHibernateProperties jpaHibernateProperties) {
        this.env = env;
        this.jpaHibernateProperties = jpaHibernateProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public HikariConfig primaryHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    public DataSource primaryDataSource() {
        return new HikariDataSource(primaryHikariConfig());
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
            .dataSource(primaryDataSource())
            .packages("com.ledar.db.domain.primary")
            .properties(jpaHibernateProperties.getVendorProperties())
            .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

    /**
     * Open the TCP port for the H2 database, so it is available remotely.
     *
     * @return the H2 database TCP server.
     * @throws SQLException if the server failed to start.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    @Profile(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT)
    public Object h2TCPServer() throws SQLException {
        String port = getValidPortForH2();
        log.debug("H2 database is available on port {}", port);
        return H2ConfigurationHelper.createServer(port);
    }

    private String getValidPortForH2() {
        int port = Integer.parseInt(env.getProperty("server.port"));
        if (port < 10000) {
            port = 10000 + port;
        } else {
            if (port < 63536) {
                port = port + 2000;
            } else {
                port = port - 2000;
            }
        }
        return String.valueOf(port);
    }
}
