package service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import core.exceptions.ComputerNotFoundException;
import core.exceptions.InvalidDataException;
import core.model.Computer;
import core.model.ComputerDTO;
import core.model.Page;
import persistence.ComputerDAO;
import binding.ComputerMapper;

@Service
public class ComputerService {

	private final ComputerDAO computerDao;
	private final ComputerMapper instanceMapper;

	public ComputerService(ComputerDAO computerDAO, ComputerMapper computerMapper) {
		this.computerDao = computerDAO;
		this.instanceMapper = computerMapper;
	}

	@Transactional
	public ComputerDTO get(Integer id) throws ComputerNotFoundException {
		return instanceMapper.toDTO(computerDao.get(id).orElseThrow(() -> new ComputerNotFoundException()));
	}

	@Transactional
	public List<ComputerDTO> get() {

		List<ComputerDTO> result = new ArrayList<ComputerDTO>();
		computerDao.get().stream().forEach(c -> result.add(instanceMapper.toDTO(c)));

		return result;
	}

	@Transactional
	public List<ComputerDTO> get(Page<Computer> page, String pattern) {

		List<ComputerDTO> result = new ArrayList<ComputerDTO>();
		computerDao.get(page, pattern);

		page.getElements().stream().forEach(c -> result.add(instanceMapper.toDTO(c)));

		return result;
	}

	@Transactional
	public void create(ComputerDTO computer) throws InvalidDataException {

		computerDao.create(instanceMapper.fromComputerDTO(computer));
	}

	@Transactional
	public void update(ComputerDTO computer) throws InvalidDataException {

		computerDao.update(instanceMapper.fromComputerDTO(computer));
	}

	@Transactional
	public List<ComputerDTO> search(String pattern) {

		List<ComputerDTO> result = new ArrayList<ComputerDTO>();
		computerDao.search(pattern).stream().forEach(c -> result.add(instanceMapper.toDTO(c)));

		return result;
	}

	@Transactional
	public void delete(Integer computerId) {
		computerDao.delete(computerId);
	}
}
