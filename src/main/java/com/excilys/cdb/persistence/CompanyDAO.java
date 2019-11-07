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
import org.springframework.stereotype.Repository;

import com.excilys.cdb.mappers.CompanyMapper;
import com.excilys.cdb.model.Company;

@Repository
public class CompanyDAO {

	private JdbcTemplate jdbcTemplate;

	private static final String SELECT_ALL = "SELECT id, name FROM company";
	private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
	private static final Logger LOG = LoggerFactory.getLogger(CompanyDAO.class);

	@Autowired
	public CompanyDAO(DataSource ds) {
		jdbcTemplate = new JdbcTemplate(ds);
	}

	public Optional<Company> get(Integer id) {
		Company company = null;
		try {
			company = (Company) jdbcTemplate.query(SELECT_BY_ID, new Object[] {id}, new CompanyMapper());
		} catch (DataAccessException e) {
			LOG.error("Error getting company by id : " + e.getMessage());
		}

		return Optional.of(company);
	}

	public List<Company> get() {
		List<Company> result = new ArrayList<>();
		try {
			result = (List<Company>) jdbcTemplate.query(SELECT_ALL, new CompanyMapper());
		} catch (DataAccessException e) {
			LOG.error("Error getting all companies : " + e.getMessage());
		}
		return result;
	}

}
