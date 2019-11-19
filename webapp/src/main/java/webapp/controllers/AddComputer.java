package webapp.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import core.exceptions.InvalidDataException;
import binding.ComputerMapper;
import core.model.Company;
import core.model.Computer;
import core.model.ComputerDTO;
import service.CompanyService;
import service.ComputerService;
import webapp.controllers.utils.FieldsValidator;

@Controller
public class AddComputer {
	private static final String CHAMP_INTRODUCED_DATE = "introducedDate";

	private CompanyService instanceCompany;
	private ComputerService instanceService;
	private ComputerMapper instanceMapper;
	private FieldsValidator instanceValidator;

	private boolean success;
	private Map<String, String> errors = new HashMap<String, String>();

	@Autowired
	public AddComputer(CompanyService companyService, ComputerService computerService, ComputerMapper computerMapper,
			FieldsValidator fieldsValidator) {
		instanceCompany = companyService;
		instanceService = computerService;
		instanceMapper = computerMapper;
		instanceValidator = fieldsValidator;
	}

	@GetMapping("/addComputer")
	public ModelAndView getAdd() {
		return new ModelAndView("addComputer", "companies",
				instanceCompany.get().stream().collect(Collectors.toMap(Company::getId, Company::getName)))
						.addObject("computer", new ComputerDTO.ComputerDTOBuilder().build());
	}

	@PostMapping("/addComputer")
	public ModelAndView postAdd(@ModelAttribute("computer") ComputerDTO computer) {

		ComputerDTO newComputer = instanceValidator.validate(errors, false, computer);

		if (newComputer != null) {
			try {
				Computer myComputer = instanceMapper.fromComputerDTO(newComputer);
				instanceService.create(myComputer);
				return getAdd().addObject("success", true);
			} catch (InvalidDataException e) {
				errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
			}
		} else {
			success = false;
		}

		return new ModelAndView("addComputer").addObject("companies", instanceCompany.get())
				.addObject("success", success).addObject("errors", errors);
	}
}
