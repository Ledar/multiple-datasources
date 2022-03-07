package com.ledar.db.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JpaHibernateProperties {

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;
    private final List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers;

    public JpaHibernateProperties(
        JpaProperties jpaProperties,
        HibernateProperties hibernateProperties,
        ConfigurableListableBeanFactory beanFactory,
        ObjectProvider<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers
    ) {
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
        this.hibernatePropertiesCustomizers = determineHibernatePropertiesCustomizers(
            beanFactory,
            hibernatePropertiesCustomizers.orderedStream().collect(Collectors.toList())
        );
    }

    private List<HibernatePropertiesCustomizer> determineHibernatePropertiesCustomizers(
        ConfigurableListableBeanFactory beanFactory,
        List<HibernatePropertiesCustomizer> hibernatePropertiesCustomizers
    ) {
        List<HibernatePropertiesCustomizer> customizers = new ArrayList<>();
        if (ClassUtils.isPresent("org.hibernate.resource.beans.container.spi.BeanContainer",
            getClass().getClassLoader())) {
            customizers.add((properties) -> properties.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory)));
        }
        customizers.addAll(hibernatePropertiesCustomizers);
        return customizers;
    }

    public Map<String, Object> getVendorProperties() {
        return new LinkedHashMap<>(
            this.hibernateProperties
                .determineHibernateProperties(jpaProperties.getProperties(),
                    new HibernateSettings()
                        // Spring Boot's HibernateDefaultDdlAutoProvider is not available here
                        .hibernatePropertiesCustomizers(this.hibernatePropertiesCustomizers)
                )
        );
    }
}
