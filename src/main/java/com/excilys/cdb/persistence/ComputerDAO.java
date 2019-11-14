package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@Repository
public class ComputerDAO {

	private static final String SELECT_ALL = "from Computer";
	private static final String SELECT_BY_ID = SELECT_ALL + " where id = ?0";
	// private static final String DELETE = "delete Computer where id = ?0";
	// private static final String UPDATE = "update Computer set name = ?0, introduced = ?1, discontinued = ?2, company_id = ?3 where id = ?4";
	private static final String SEARCH = "from Computer where name like ?0 or company.name like ?1";
	private static final Logger LOG = LoggerFactory.getLogger(ComputerDAO.class);

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	public ComputerDAO() {
	}

	public Optional<Computer> get(Integer id) {

		Computer computer = null;
		Session session = sessionFactory.openSession();

		try {
			computer = session.createQuery(SELECT_BY_ID, Computer.class).setParameter(0, id).getResultList().get(0);
		} catch (Exception e) {
			LOG.error("Error getting computer by id : " + e.getMessage());
		} finally {
			session.close();
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
		} finally {
			session.close();
		}
		return result;
	}

	public void get(Page<Computer> page, String pattern, boolean isSearch) {

		List<Computer> newElements = new ArrayList<>();
		Session session = sessionFactory.openSession();

		try {
			if (isSearch) {
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
		} finally {
			session.close();
		}
	}

	public void create(Computer computer) throws InvalidDataException {

		Session session = sessionFactory.openSession();

		try {
			session.save(computer);
		} catch (Exception e) {
			LOG.error("Error creating computer : " + e.getMessage());
			throw new InvalidDataException("Invalid Data");
		} finally {
			session.close();
		}
	}

	public void update(Computer computer) throws InvalidDataException {

		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			/*
			 * session.createQuery(UPDATE).setParameter(0,
			 * computer.getName()).setParameter(1, computer.getIntroduced())
			 * .setParameter(2, computer.getDiscontinued()).setParameter(3,
			 * computer.getCompany().getId()) .setParameter(4,
			 * computer.getId()).executeUpdate();
			 */
			session.update(computer);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			LOG.error("Error updating computer : " + e.getMessage() + e.getClass());
			throw new InvalidDataException("Invalid Data");
		} finally {
			session.close();
		}
	}

	public void delete(int computerId) {

		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			session.delete(get(computerId).get());
			tx.commit();
		} catch (NoSuchElementException e) {
			LOG.error("There is no computer with this ID : " + e.getMessage());
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			LOG.error("Error deleting computer : " + e.getMessage());
		} finally {
			session.close();
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
		} finally {
			session.close();
		}
		return result;
	}
}
