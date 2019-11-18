package persistence;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

@Configuration
@ComponentScan(basePackages = { "binding", "service", "persistence", "webapp.controllers", "core.model" })
@PropertySource(value = "classpath:application.properties")
public class TestConfig {

	@Autowired
	private Environment env;

	@Bean
	public DataSource getConnection() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("dataSource.driverClassName"));
		dataSource.setUrl(env.getProperty("dataSource.jdbcUrl"));
		dataSource.setUsername(env.getProperty("dataSource.user"));
		dataSource.setPassword(env.getProperty("dataSource.password"));
		return dataSource;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(getConnection());
		sessionFactory.setPackagesToScan(new String[] {"core.model"});

		return sessionFactory;
	}
}
