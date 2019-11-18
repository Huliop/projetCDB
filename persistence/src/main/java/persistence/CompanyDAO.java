package persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;

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
	private static final String SELECT_BY_ID = SELECT_ALL + " where id = ?";
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
			TypedQuery<Company> query = session.createQuery(SELECT_BY_ID, Company.class).setParameter(0, id);
			List<Company> list = query.getResultList();
			company = list.get(0);
		} catch (Exception e) {
			LOG.error("Error getting company by id : " + e.getMessage());
		} finally {
			session.close();
		}

		return Optional.of(company);
	}

	public List<Company> get() {

		List<Company> result = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			TypedQuery<Company> query = session.createQuery(SELECT_ALL, Company.class);
			result = query.getResultList();
		} catch (Exception e) {
			LOG.error("Error getting all companies : " + e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
