package com.example.lockstudy.mysql.named;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
public class DataSourceConfig {

  @Bean(name = "lockDataSourceProperties")
  @ConfigurationProperties(value = "spring.datasource.lock")
  public DataSourceProperties lockDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "defaultDataSourceProperties")
  @ConfigurationProperties(value = "spring.datasource.default")
  public DataSourceProperties defaultDataSourceProperties() {
    return new DataSourceProperties();
  }

  @Bean(name = "defaultDataSource")
  public DataSource defaultDataSource(
      @Qualifier("defaultDataSourceProperties")
      DataSourceProperties dataSourceProperties) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setPoolName("default-connection-pool");
    dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setJdbcUrl(dataSourceProperties.getUrl());

    return dataSource;
  }

  @Bean(name = "lockDataSource")
  public DataSource lockDataSource(
      @Qualifier("lockDataSourceProperties")
      DataSourceProperties dataSourceProperties) {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setPoolName("lock-connection-pool");
    dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
    dataSource.setUsername(dataSourceProperties.getUsername());
    dataSource.setPassword(dataSourceProperties.getPassword());
    dataSource.setJdbcUrl(dataSourceProperties.getUrl());

    return dataSource;
  }

  @Bean
  public DataSourceInitializer dataSourceInitializer(@Qualifier("defaultDataSource") DataSource datasource) {
    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
    resourceDatabasePopulator.addScript(new ClassPathResource("data.sql"));

    DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
    dataSourceInitializer.setDataSource(datasource);
    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
    return dataSourceInitializer;
  }
}
