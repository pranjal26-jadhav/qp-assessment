package com.grocery.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Datasource {

    @Bean
    public HikariDataSource datasourceConfig() {
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername("gravity");
        dataSource.setPassword("gravity-26");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/demo");
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setMaximumPoolSize(100);
        dataSource.setMinimumIdle(2);
        dataSource.setConnectionTimeout(300000);
        dataSource.setConnectionTestQuery("SELECT 1");
        return dataSource;

    }
}
