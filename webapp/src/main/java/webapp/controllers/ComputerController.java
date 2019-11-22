package webapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import core.exceptions.ComputerNotFoundException;
import core.exceptions.InvalidDataException;
import core.model.ComputerDTO;
import service.ComputerService;
import webapp.controllers.utils.FieldsValidator;

@RestController
@RequestMapping("/computers")
public class ComputerController {

	private final ComputerService instanceService;
	private final FieldsValidator instanceValidator;

	private static final Logger LOG = LoggerFactory.getLogger(ComputerController.class);

	private Map<String, String> errors = new HashMap<String, String>();

	public ComputerController(ComputerService computerService, FieldsValidator fieldsValidator) {
		this.instanceService = computerService;
		this.instanceValidator = fieldsValidator;
	}

	@GetMapping
	public List<ComputerDTO> get() {
		return instanceService.get();
	}

	@GetMapping("/{id}")
	public ComputerDTO get(@PathVariable Integer id) throws ComputerNotFoundException {
		return instanceService.get(id);
	}

	@GetMapping("/search/{pattern}")
	public List<ComputerDTO> search(@PathVariable String pattern) {
		return instanceService.search(pattern);
	}

	@PostMapping
	public void create(@RequestBody ComputerDTO computer) throws InvalidDataException {
		ComputerDTO newComputer = instanceValidator.validate(errors, false, computer);

		if (newComputer != null) {
			instanceService.create(computer);
		} else {
			LOG.error("create errors");
			// TODO : deal with errors
		}
	}

	@PutMapping
	public void update(@RequestBody ComputerDTO computer) throws InvalidDataException {
		ComputerDTO newComputer = instanceValidator.validate(errors, true, computer);

		if (newComputer != null) {
			instanceService.update(computer);
		} else {
			LOG.error("update errors");
			// TODO : deal with errors
		}
	}

	@DeleteMapping
	public void delete(Integer id) {
		instanceService.delete(id);
	}
}
