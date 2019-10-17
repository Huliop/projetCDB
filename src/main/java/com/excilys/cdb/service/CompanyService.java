package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

public class CompanyService {

	private static CompanyService instance;

	private final CompanyDAO companyDAO;

	private CompanyService(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public static CompanyService getInstance() {
		if (instance == null) {
			instance = new CompanyService(CompanyDAO.getInstance());
		}
		return instance;
	}

	public Optional<Company> get(Integer id) {
		return companyDAO.get(id);
	}

	public List<Company> get() {
		return companyDAO.get();
	}
}
