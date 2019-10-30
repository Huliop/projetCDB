package com.excilys.cdb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.ComputerDAO;

@Service
public class ComputerService {

	private final ComputerDAO computerDao;

	private ComputerService(ComputerDAO computerDAO) {
		this.computerDao = computerDAO;
	}

	public Computer get(Integer id) throws ComputerNotFoundException {
		return computerDao.get(id).orElseThrow(() -> new ComputerNotFoundException());
	}

	public List<Computer> get() {
		return computerDao.get();
	}

	public void get(Page<Computer> page, String query, String pattern, boolean isSearch) {
		computerDao.get(page, query, pattern, isSearch);
	}

	public void create(Computer computer) throws InvalidDataException {
		try {
			computerDao.create(computer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new InvalidDataException("You must give a valid date");
		}
	}

	public void update(Computer computer) throws InvalidDataException {
		try {
			computerDao.update(computer);
		} catch (InvalidDataException e) {
			e.printStackTrace();
			throw new InvalidDataException("You must give a valid date");
		}
	}
	
	public List<Computer> search(String pattern) {
		return computerDao.search(pattern);
	}

	public void delete(int computerId) {
		computerDao.delete(computerId);
	}
}
