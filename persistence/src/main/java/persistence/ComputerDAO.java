package persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import core.exceptions.InvalidDataException;
import core.model.Computer;
import core.model.Page;

@Repository
public class ComputerDAO {

	private static final String SELECT_ALL = "from Computer";
	private static final String SELECT_BY_ID = SELECT_ALL + " where id = ?0";
	// private static final String DELETE = "delete Computer where id = ?0";
	// private static final String UPDATE = "update Computer set name = ?0,
	// introduced = ?1, discontinued = ?2, company_id = ?3 where id = ?4";
	private static final String SEARCH = "from Computer where name like ?0 or company.name like ?1";
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

	@PersistenceContext
	EntityManager entityManager;
	
	private final SessionFactory sessionFactory;

	@Autowired
	public ComputerDAO(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Optional<Computer> get(Integer id) {

		Computer computer = null;
		Session session = sessionFactory.openSession();

		try {
			computer = session.createQuery(SELECT_BY_ID, Computer.class).setParameter(0, id).getResultList().get(0);
		} catch (Exception e) {
			LOG.error("Error getting computer by id : " + e.getMessage());
		}

		return Optional.ofNullable(computer);
	}

	public List<Computer> get() {

		List<Computer> result = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			result = session.createQuery(SELECT_ALL, Computer.class).getResultList();
		} catch (Exception e) {
			LOG.error("Error getting all computers : " + e.getMessage());
		}

		return result;
	}

	public void get(Page<Computer> page, String pattern) {

		List<Computer> newElements = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			if (pattern != null && pattern.trim().length() > 0) {
				newElements = session.createQuery(SEARCH, Computer.class).setParameter(0, "%" + pattern + "%")
						.setParameter(1, "%" + pattern + "%").setFirstResult(page.getFirstLimit())
						.setMaxResults(page.getOffset()).getResultList();
			} else {
				newElements = session.createQuery(SELECT_ALL, Computer.class).setFirstResult(page.getFirstLimit())
						.setMaxResults(page.getOffset()).getResultList();
			}
			page.setElements(newElements);
		} catch (Exception e) {
			LOG.error("Error getting computers paginated : " + e.getMessage());
		}
	}

	public void create(Computer computer) throws InvalidDataException {

		Session session = sessionFactory.openSession();

		try {
			session.save(computer);
		} catch (Exception e) {
			throw new InvalidDataException("Invalid Data" + e.getMessage());
		}
	}

	public void update(Computer computer) throws InvalidDataException {

		Session session = entityManager.unwrap(Session.class);

		try {
			session.update(computer);
		} catch (Exception e) {
			throw new InvalidDataException("Invalid Data" + e.getMessage());
		}
	}

	public void delete(Integer computerId) {

		Session session = entityManager.unwrap(Session.class);

		try {
			session.delete(get(computerId).get());
		} catch (NoSuchElementException e) {
			LOG.error("There is no computer with this ID : " + e.getMessage());
		} catch (Exception e) {
			LOG.error("Error deleting computer : " + e.getMessage());
		}
	}

	public List<Computer> search(String pattern) {

		List<Computer> result = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			result = session.createQuery(SEARCH, Computer.class).setParameter(0, "%" + pattern + "%")
					.setParameter(1, "%" + pattern + "%").getResultList();
		} catch (Exception e) {
			LOG.error("Error searching : " + e.getMessage());
		}

		return result;
	}
}
