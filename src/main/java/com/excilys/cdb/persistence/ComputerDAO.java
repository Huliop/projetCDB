package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@Repository
public class ComputerDAO {

	private JdbcTemplate jdbcTemplate;

	private final String SELECT_ALL = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";
	private final String SELECT_BY_ID = SELECT_ALL + " WHERE computer.id = ?";
	private final String DELETE = "DELETE FROM computer WHERE id = ?";
	private final String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	private final String SEARCH = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";
	private static final Logger log = LoggerFactory.getLogger(CompanyDAO.class);
	private SimpleJdbcInsert insertComputer;

	@Autowired
	private ComputerDAO(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
		this.insertComputer = new SimpleJdbcInsert(ds)
                .withTableName("computer")
                .usingGeneratedKeyColumns("id");
	}

	public Optional<Computer> get(Integer id) {
		Computer computer = null;
		try {
			computer = (Computer) jdbcTemplate.queryForObject(SELECT_BY_ID, new Object [] { id }, new ComputerMapper());
		} catch (DataAccessException e) {
			log.error("Error getting computer by id : " + e.getMessage());
		}
		return Optional.ofNullable(computer);
	}

	public List<Computer> get() {
		List<Computer> result = new ArrayList<>();
		try {
			result = (List<Computer>) jdbcTemplate.query(SELECT_ALL, new ComputerMapper());
		} catch (DataAccessException e) {
			log.error("Error getting all computers : " + e.getMessage());
		}
		return result;
	}

	public void get(Page<Computer> page, String query, String pattern, boolean isSearch) {
		try {
			List<Computer> newElements = new ArrayList<>();
			
			if (isSearch) {
				newElements = (List<Computer>) jdbcTemplate.query(query + " LIMIT " + page.getFirstLimit() + "," + page.getOffset(), new Object[] {"%" + pattern + "%", "%" + pattern + "%"}, new ComputerMapper());
			} else {
				newElements = (List<Computer>) jdbcTemplate.query(query + " LIMIT " + page.getFirstLimit() + "," + page.getOffset(), new ComputerMapper());
			}
			page.setElements(newElements);
		} catch (DataAccessException e) {
			log.error("Error getting computers paginated : " + e.getMessage());
		}
	}

	public void create(Computer computer) throws Exception {
		try {
			SqlParameterSource parameters = new MapSqlParameterSource()
					.addValue("name", computer.getName())
					.addValue("introduced", computer.getIntroduced())
					.addValue("discontinued", computer.getDiscontinued())
					.addValue("company_id", computer.getCompany().getId());
	        Number newId = insertComputer.executeAndReturnKey(parameters);
	        computer.setId(newId.intValue());
		} catch (DataAccessException e) {
			log.error("Error creating computer : " + e.getMessage());
			throw new InvalidDataException("Invalid Data");
		}
	}

	public void update(Computer computer) throws InvalidDataException {
		try {
			Object[] params = { computer.getName(), computer.getIntroduced(), computer.getDiscontinued(), computer.getCompany().getId(), computer.getId() };
			jdbcTemplate.update(UPDATE, params);
		} catch (DataAccessException e) {
			log.error("Error updating computer : " + e.getMessage());
			throw new InvalidDataException("Invalid Data");
		}
	}

	public void delete(int computerId) {
		try {
			jdbcTemplate.update(DELETE, computerId);
		} catch (DataAccessException e) {
			log.error("Error deleting computer : " + e.getMessage());
		}
	}

	public List<Computer> search(String pattern) {
		List<Computer> result = new ArrayList<>();
		try {
			result = (List<Computer>) jdbcTemplate.query(SEARCH, new Object[] {"%" + pattern + "%", "%" + pattern + "%"}, new ComputerMapper());
		} catch (DataAccessException e) {
			System.out.println("Error searching : " + e.getMessage());
		}
		return result;
	}
}
