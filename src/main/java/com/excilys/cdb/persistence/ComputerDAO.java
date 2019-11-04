package com.excilys.cdb.persistence;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.excilys.cdb.connection.DBConnection;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;

@Repository
public class ComputerDAO {

	@Autowired
	private DBConnection dbConnection;

	private final ComputerMapper computerMapper;

	private final String SELECT_ALL = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";
	private final String SELECT_BY_ID = SELECT_ALL + " WHERE computer.id = ?";
	private final String CREATE = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES (?, ?, ?, ?)";
	private final String DELETE = "DELETE FROM computer WHERE id = ?";
	private final String UPDATE = "UPDATE computer SET name = ?, introduced = ?, discontinued = ?, company_id = ? WHERE id = ?";
	private final String SEARCH = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";

	private ComputerDAO(ComputerMapper computerMapper) {
		this.computerMapper = computerMapper;
	}

	public Optional<Computer> get(Integer id) {
		Optional<Computer> computer = Optional.empty();
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(SELECT_BY_ID)) {
			stmt.setInt(1, id);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				computer = Optional.of(computerMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error getting computer by id : " + e.getMessage());
		}
		return computer;
	}

	public List<Computer> get() {
		List<Computer> result = new ArrayList<>();
		try (Statement stmt = dbConnection.getConnection().createStatement()) {
			ResultSet resultSet = stmt.executeQuery(SELECT_ALL);

			while (resultSet.next()) {
				result.add(computerMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error getting all computers : " + e.getMessage());
		}
		return result;
	}

	public void get(Page<Computer> page, String query, String pattern, boolean isSearch) {
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(query + " LIMIT " + page.getFirstLimit() + "," + page.getOffset())) {
			if (isSearch) {
				stmt.setString(1, "%" + pattern + "%");
				stmt.setString(2, "%" + pattern + "%");
			}
			ResultSet resultSet = stmt.executeQuery();

			List<Computer> newElements = new ArrayList<>();
			page.setElements(newElements);

			while (resultSet.next()) {
				page.getElements().add(computerMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error getting computers paginated : " + e.getMessage());
		} finally {
			DBConnection.closeConnection();
		}
	}

	public void create(Computer computer) throws Exception {
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
			setStatementParameters(computer, stmt);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                computer.setId(rs.getInt(1));
            }
		} catch (SQLException e) {
			System.out.println("Error creating computer : " + e.getMessage());
			throw new InvalidDataException("Invalid Data");
		}
	}

	public void update(Computer computer) throws InvalidDataException {
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(UPDATE)) {
			setStatementParameters(computer, stmt);
			stmt.setInt(5, computer.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error updating computer : " + e.getMessage());
			throw new InvalidDataException("Invalid Data");
		}
	}

	public void delete(int computerId) {
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(DELETE)) {
			stmt.setInt(1, computerId);

			stmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error deleting computer : " + e.getMessage());
		}
	}

	public List<Computer> search(String pattern) {
		List<Computer> result = new ArrayList<>();
		try (PreparedStatement stmt = dbConnection.getConnection().prepareStatement(SEARCH)) {
			stmt.setString(1, "%" + pattern + "%");
			stmt.setString(2, "%" + pattern + "%");

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				result.add(computerMapper.fromResultSet(resultSet));
			}
		} catch (SQLException e) {
			System.out.println("Error searching : " + e.getMessage());
		}
		return result;
	}

	private void setStatementParameters(Computer computer, PreparedStatement stmt) throws SQLException {
		stmt.setString(1, computer.getName());
		if (computer.getIntroduced() != null) {
			stmt.setDate(2, Date.valueOf(computer.getIntroduced()));
		} else {
			stmt.setDate(2,  null);
		}
		if (computer.getDiscontinued() != null) {
			stmt.setDate(3, Date.valueOf(computer.getDiscontinued()));
		} else {
			stmt.setDate(3,  null);
		}
		if (computer.getCompany().getId() != null) {
			stmt.setInt(4, computer.getCompany().getId());
		} else {
			stmt.setDate(4,  null);
		}
	}
}
