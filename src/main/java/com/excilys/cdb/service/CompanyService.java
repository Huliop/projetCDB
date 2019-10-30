package com.excilys.cdb.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

@Service
public class CompanyService {

	private final CompanyDAO companyDAO;

	private CompanyService(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public Optional<Company> get(Integer id) {
		return companyDAO.get(id);
	}

	public List<Company> get() {
		return companyDAO.get();
	}
}
