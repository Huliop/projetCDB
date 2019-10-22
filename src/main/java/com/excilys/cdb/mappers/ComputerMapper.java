package com.excilys.cdb.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;

public class ComputerMapper {

	private static ComputerMapper instance;

	private ComputerMapper() { }

	public static ComputerMapper getInstance() {
		if (instance == null) {
			instance = new ComputerMapper();
		}
		return instance;
	}

	public Computer fromResultSet(ResultSet resultSet) throws SQLException {
		return new Computer.ComputerBuilder().withId(resultSet.getInt(1))
				.withName(resultSet.getString(2))
				.withIntroduced(resultSet.getDate(3) != null ? resultSet.getDate(3).toLocalDate() : null)
				.withDiscontinued(resultSet.getDate(4) != null ? resultSet.getDate(4).toLocalDate() : null)
				.withCompany(new Company.CompanyBuilder()
										.withId(resultSet.getInt(5))
										.withName(resultSet.getString(6))
										.build())
				.build();
	}
	
	public Computer fromComputerDTO(ComputerDTO computer) {
		return new Computer.ComputerBuilder().withId(computer.getId())
				.withName(computer.getName())
				.withIntroduced(computer.getIntroduced() != null ? LocalDate.parse(computer.getIntroduced()) : null)
				.withDiscontinued(computer.getDiscontinued() != null ? LocalDate.parse(computer.getDiscontinued()) : null)
				.withCompany(new Company.CompanyBuilder()
						.withId(computer.getCompanyId())
						.withName(computer.getCompanyName())
						.build())
				.build();
	}
}
