package webapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.DigestAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.DigestAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = { "service" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private UserDetailsService userDetailsService;

	@Autowired
	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(digestFilter(userDetailsService)).exceptionHandling()
				.authenticationEntryPoint(digestEntryPoint()).and().authorizeRequests().anyRequest().authenticated();
	}

	DigestAuthenticationFilter digestFilter(UserDetailsService userDetailsService) {
		DigestAuthenticationFilter daf = new DigestAuthenticationFilter();
		daf.setUserDetailsService(userDetailsService);
		daf.setAuthenticationEntryPoint(digestEntryPoint());

		return daf;
	}

	DigestAuthenticationEntryPoint digestEntryPoint() {
		DigestAuthenticationEntryPoint daep = new DigestAuthenticationEntryPoint();
		daep.setRealmName("Contacts Realm via Digest Authentication");
		daep.setKey("acegi");
		daep.setNonceValiditySeconds(100);

		return daep;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return rawPassword.toString();
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return rawPassword.toString().equals(encodedPassword);
			}
		};
	}
}
