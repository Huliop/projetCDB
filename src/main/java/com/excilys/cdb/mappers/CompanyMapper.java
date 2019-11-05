package com.excilys.cdb.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper implements RowMapper<Company> {

	public CompanyMapper() { }

	@Override
	public Company mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Company.CompanyBuilder().withId(rs.getInt(1))
				.withName(rs.getString(2))
				.build();
	}
}
