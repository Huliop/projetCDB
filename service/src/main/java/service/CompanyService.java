package service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import core.model.Company;
import persistence.CompanyDAO;

@Service
public class CompanyService {

	private final CompanyDAO companyDAO;

	public CompanyService(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	@Transactional
	public Optional<Company> get(Integer id) {
		return companyDAO.get(id);
	}

	@Transactional
	public List<Company> get() {
		return companyDAO.get();
	}
}
