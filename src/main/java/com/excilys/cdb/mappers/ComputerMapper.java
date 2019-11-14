package com.excilys.cdb.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;

@Component
public class ComputerMapper implements RowMapper<Computer> {

	public ComputerMapper() {
	}

	public Computer fromComputerDTO(ComputerDTO computer) {
		return new Computer.ComputerBuilder().withId(computer.getId()).withName(computer.getName())
				.withIntroduced(computer.getIntroduced() != null ? LocalDate.parse(computer.getIntroduced()) : null)
				.withDiscontinued(
						computer.getDiscontinued() != null ? LocalDate.parse(computer.getDiscontinued()) : null)
				.withCompany(new Company.CompanyBuilder().withId(computer.getCompanyId())
						.withName(computer.getCompanyName()).build())
				.build();
	}

	public ComputerDTO toDTO(Computer computer) {
		return new ComputerDTO.ComputerDTOBuilder().withId(computer.getId()).withName(computer.getName())
				.withIntroduced(computer.getIntroduced() != null ? computer.getIntroduced().toString() : null)
				.withDiscontinued(computer.getDiscontinued() != null ? computer.getDiscontinued().toString() : null)
				.withCompanyName(computer.getCompany() != null ? computer.getCompany().getName() : null)
				.withCompanyId(computer.getCompany() != null ? computer.getCompany().getId() : null).build();
	}

	@Override
	public Computer mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new Computer.ComputerBuilder().withId(rs.getInt(1)).withName(rs.getString(2))
				.withIntroduced(rs.getDate(3) != null ? rs.getDate(3).toLocalDate() : null)
				.withDiscontinued(rs.getDate(4) != null ? rs.getDate(4).toLocalDate() : null)
				.withCompany(new Company.CompanyBuilder().withId(rs.getInt(5)).withName(rs.getString(6)).build())
				.build();
	}
}
