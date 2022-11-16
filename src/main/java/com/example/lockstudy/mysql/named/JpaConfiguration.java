package com.example.lockstudy.mysql.named;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.lockstudy",
    entityManagerFactoryRef = "defaultEntityManagerFactory",
    transactionManagerRef = "defaultTransactionManager")
public class JpaConfiguration {

  @Value("${spring.jpa.show-sql}")
  private boolean showSql;

  @Value("${spring.jpa.hibernate.ddl-auto}")
  private String ddlAuto;


  @Bean
  public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
      @Qualifier("defaultDataSource") DataSource dsataSource) {
    return getJpaEntityManagerFactory(dsataSource, "com.example.lockstudy");
  }

  @Bean
  public PlatformTransactionManager defaultTransactionManager(
      @Qualifier("defaultDataSource") DataSource dataSource) {
    return new JpaTransactionManager(defaultEntityManagerFactory(dataSource).getObject());
  }

  protected HibernateJpaVendorAdapter getJpaVendorAdapter() {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setShowSql(showSql);
    jpaVendorAdapter.setGenerateDdl(true);

    return jpaVendorAdapter;
  }


  protected LocalContainerEntityManagerFactoryBean getJpaEntityManagerFactory(
      DataSource dataSource, String packageName) {
    LocalContainerEntityManagerFactoryBean factoryBean =
        new LocalContainerEntityManagerFactoryBean();
    factoryBean.setJpaVendorAdapter(getJpaVendorAdapter());

    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", ddlAuto);
    properties.put("hibernate.format_sql", true);

    factoryBean.setDataSource(dataSource);
    factoryBean.setPackagesToScan(packageName);
    factoryBean.setJpaPropertyMap(properties);

    return factoryBean;
  }
}
