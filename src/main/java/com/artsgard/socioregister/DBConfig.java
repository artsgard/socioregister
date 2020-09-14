package com.artsgard.socioregister;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author artsgard
 */
@Configuration
@ConfigurationProperties("spring.datasource")
public class DBConfig {

    @Profile("dev")
    @Bean(name = "postgresDataSource")
    public DataSource devDatabaseConnection() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:postgresql://localhost:5432/socio_db");
        //dataSourceBuilder.url("jdbc:postgresql://sociodb:5432/socio_db");
        dataSourceBuilder.username("postgres");
        dataSourceBuilder.password("admin");
        return dataSourceBuilder.build();
    }

    @Profile("test")
    @Bean(name = "h2DataSource")
    public DataSource testDatabaseConnection() {
          DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:h2:mem:socio_test");
        dataSourceBuilder.username("sa");
        dataSourceBuilder.password("sa");
        return dataSourceBuilder.build();
    }

    @Profile("prod")
    @Bean
    public DataSource prodDatabaseConnection() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:mysql://localhost:3306/socio_db?createDatabaseIfNotExist=true&useUnicode=yes&characterEncoding=UTF-8&useLegacyDatetimeCode=false&serverTimezone=UTC");
        //dataSourceBuilder.url("jdbc:postgresql://localhost:5432/socio_db");
        //dataSourceBuilder.url("jdbc:postgresql://sociodb:5432/socio_db");
        dataSourceBuilder.username("root");
        dataSourceBuilder.password("admin");
        return dataSourceBuilder.build();
    }
}

