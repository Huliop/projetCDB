package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import persistence.UserDAO;

@Service
public class UsersService implements UserDetailsService {

	private final UserDAO userDAO;

	@Autowired
	public UsersService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		core.model.User user = userDAO.get(username);

		if (user != null) {
			return User.withUsername(user.getName()).password(user.getPassword()).roles("ADMIN").build();
		} else {
			throw new UsernameNotFoundException("Username : " + username + " doesn't exist!");
		}
	}

}
