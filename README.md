# db

This application was generated using JHipster 7.7.0, you can find documentation and help at [https://www.jhipster.tech](https://www.jhipster.tech).

spring boot with multiple datasources.

## Project Structure

### application-dev:

config two datasources(primary and secondary)

### config:

JpaHibernateProperties.java to get JPA and Hibernate properties from application.yml

PrimaryDatabaseConfiguration.java for primary datasource

SecondaryDatabaseConfiguration.java for secondary datasource

### domain and repository:

place into separate package, eg: domain.primary, domain.secondary, repository.primary, repository.primary, repository.secondary
