package com.excilys.cdb.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;

@Component
public class CompanyMapper {

	private CompanyMapper() { }

	public Company fromResultSet(ResultSet result) throws SQLException {
		return new Company.CompanyBuilder().withId(result.getInt(1))
											.withName(result.getString(2))
											.build();
	}
}
