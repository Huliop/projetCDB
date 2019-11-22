package persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import core.model.Company;

@Repository
public class CompanyDAO {

	private static final String SELECT_ALL = "from Company";
	private static final String SELECT_BY_ID = SELECT_ALL + " where id = ?0";
	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

	SessionFactory sessionFactory;

	@Autowired
	public CompanyDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	};

	public Optional<Company> get(Integer id) {

		Company company = null;
		Session session = sessionFactory.openSession();

		try {
			company = session.createQuery(SELECT_BY_ID, Company.class).setParameter(0, id).getResultList().get(0);
		} catch (Exception e) {
			LOG.error("Error getting company by id : " + e.getMessage());
		}

		return Optional.of(company);
	}

	public List<Company> get() {

		List<Company> result = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			result = session.createQuery(SELECT_ALL, Company.class).getResultList();
		} catch (Exception e) {
			LOG.error("Error getting all companies : " + e.getMessage());
		}

		return result;
	}

}
