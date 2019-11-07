package configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "com.excilys.cdb.mappers", "com.excilys.cdb.service", "com.excilys.cdb.persistence", "controle" })
@PropertySource("classpath:application.properties")
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private Environment env;

    @Bean
 	public DataSource getConnection() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("dataSource.driverClassName"));
		ds.setUrl(env.getProperty("dataSource.jdbcUrl"));
		ds.setUsername(env.getProperty("dataSource.user"));
		ds.setPassword(env.getProperty("dataSource.password"));
		return ds;
	 }

    @Bean
    public ViewResolver viewResolver() {
       InternalResourceViewResolver bean = new InternalResourceViewResolver();

       bean.setViewClass(JstlView.class);
       bean.setPrefix("/WEB-INF/views/");
       bean.setSuffix(".jsp");

       return bean;
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }
}
