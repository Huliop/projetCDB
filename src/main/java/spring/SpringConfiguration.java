package spring;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb", "controle" })
@PropertySource("classpath:application.properties")
public class SpringConfiguration extends AbstractContextLoaderInitializer {
	
	@Autowired
	private Environment env;
	
    @Override
    protected WebApplicationContext createRootApplicationContext() {
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.register(SpringConfiguration.class);
    return rootContext;
    }
    
    @Bean
 	public DataSource getConnection() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("dataSource.driverClassName"));
		ds.setUrl(env.getProperty("dataSource.jdbcUrl"));
		ds.setUsername(env.getProperty("dataSource.user"));
		ds.setPassword(env.getProperty("dataSource.password"));
		return ds;
	 }
}
