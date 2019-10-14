package com.excilys.cdb.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.excilys.cdb.connection.DBConnection;
import com.excilys.cdb.mappers.CompanyMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;

public class CompanyDAO {
	
	private static final String SELECT_ALL = "SELECT id, name FROM company";
	private static final String SELECT_BY_ID = SELECT_ALL + " WHERE id = ?";
	private static CompanyDAO instance;
	
	private final CompanyMapper companyMapper;

	public CompanyDAO(CompanyMapper companyMapper) {
		this.companyMapper = companyMapper;
	}
	
	public static CompanyDAO getInstance() {
		if (instance == null) {
			instance = new CompanyDAO(CompanyMapper.getInstance());
		}
		
		return instance;
	}
	
	public Optional<Company> get(Integer id) {
		Optional<Company> company = Optional.empty();
		try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(SELECT_BY_ID)) {
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				company = Optional.of(companyMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error getting company by id : " + e.getMessage());
		}
		
		return company;
	}
	
	public List<Company> get() {
		List<Company> result = new ArrayList<>();
		try (Statement stmt = DBConnection.getConnection().createStatement()) {
			ResultSet resultSet = stmt.executeQuery(SELECT_ALL);
			
			while (resultSet.next()) {
				
				result.add(companyMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error getting all companies : " + e.getMessage());
		}
		return result;
	}

}
