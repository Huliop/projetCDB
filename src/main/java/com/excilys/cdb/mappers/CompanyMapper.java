package com.excilys.cdb.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.excilys.cdb.model.Company;

public class CompanyMapper {
	private static CompanyMapper instance;

	private CompanyMapper() {}
	
	public static CompanyMapper getInstance() {
		if (instance == null) {
			instance = new CompanyMapper();
		}
		return instance;
	}
	
	public Company fromResultSet(ResultSet result) throws SQLException {
		return new Company.CompanyBuilder().withId(result.getInt(1))
											.withName(result.getString(2))
											.build();
		
	}
}
