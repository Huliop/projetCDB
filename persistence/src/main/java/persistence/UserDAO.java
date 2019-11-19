package persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import core.model.User;

@Repository
public class UserDAO {
	private static final String SELECT_BY_NAME = "from User where name = ?0";
	private static final Logger LOG = LoggerFactory.getLogger(UserDAO.class);

	SessionFactory sessionFactory;

	@Autowired
	public UserDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	};

	public User get(String name) {

		User user = null;
		Session session = sessionFactory.openSession();

		try {
			user = session.createQuery(SELECT_BY_NAME, User.class).setParameter(0, name).getResultList().get(0);
		} catch (Exception e) {
			LOG.error("Error getting user : " + e.getMessage());
		} finally {
			session.close();
		}

		return user;
	}
}
