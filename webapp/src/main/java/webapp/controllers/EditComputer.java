package webapp.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import core.exceptions.ComputerNotFoundException;
import core.exceptions.InvalidDataException;
import binding.ComputerMapper;
import core.model.Company;
import core.model.Computer;
import core.model.ComputerDTO;
import service.CompanyService;
import service.ComputerService;
import webapp.controllers.utils.FieldsValidator;

@Controller
public class EditComputer {
	private static final String CHAMP_INTRODUCED_DATE = "introducedDate";

	private CompanyService instanceCompany;
	private ComputerService instanceService;
	private ComputerMapper instanceMapper;
	private FieldsValidator instanceValidator;

	private Map<String, String> errors = new HashMap<String, String>();

	@Autowired
	public EditComputer(CompanyService companyService, ComputerService computerService, ComputerMapper computerMapper,
			FieldsValidator fieldsValidator) {
		instanceCompany = companyService;
		instanceService = computerService;
		instanceMapper = computerMapper;
		instanceValidator = fieldsValidator;
	}

	@GetMapping("/editComputer")
	public ModelAndView getEdit(@RequestParam(value = "idComputer", required = false) String id) {
		try {
			Computer myComputer = instanceService.get(Integer.valueOf(id));
			return new ModelAndView("editComputer", "companies",
					instanceCompany.get().stream().collect(Collectors.toMap(Company::getId, Company::getName)))
							.addObject("computer", instanceMapper.toDTO(myComputer));
		} catch (NumberFormatException e) {
			return new ModelAndView("500", "error", "Id non valide : " + e.getMessage());
		} catch (ComputerNotFoundException e) {
			return new ModelAndView("500", "error", "Id non valide : " + e.getMessage());
		}
	}

	@PostMapping("/editComputer")
	public ModelAndView postEdit(@ModelAttribute("computer") ComputerDTO computer) throws ComputerNotFoundException {
		ComputerDTO newComputer = instanceValidator.validate(errors, true, computer);
		if (newComputer != null) {
			try {
				Computer myComputer = instanceMapper.fromComputerDTO(newComputer);
				instanceService.update(myComputer);
				return new ModelAndView("redirect:/index").addObject("companies", instanceCompany.get())
						.addObject("success", true).addObject("numPage", 1);
			} catch (InvalidDataException e) {
				errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
			}
		}

		return new ModelAndView("editComputer").addObject("companies", instanceCompany.get())
				.addObject("computer", computer).addObject("errors", errors);
	}
}
