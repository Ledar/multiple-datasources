package com.ledar.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ledar.db.repository.secondary", entityManagerFactoryRef = "secondaryEntityManagerFactory", transactionManagerRef = "secondaryTransactionManager")
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class SecondaryDatabaseConfiguration {

    private final JpaHibernateProperties jpaHibernateProperties;

    public SecondaryDatabaseConfiguration(JpaHibernateProperties jpaHibernateProperties) {
        this.jpaHibernateProperties = jpaHibernateProperties;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public HikariConfig secondaryHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource secondaryDataSource() {
        return new HikariDataSource(secondaryHikariConfig());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean secondaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
            .dataSource(secondaryDataSource())
            .packages("com.ledar.db.domain.secondary")
            .properties(jpaHibernateProperties.getVendorProperties())
            .build();
    }

    @Bean
    public PlatformTransactionManager secondaryTransactionManager(@Qualifier("secondaryEntityManagerFactory") EntityManagerFactory secondaryEntityManagerFactory) {
        return new JpaTransactionManager(secondaryEntityManagerFactory);
    }
}
