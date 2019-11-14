package controle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
public class EditComputer {
	private static final String CHAMP_INTRODUCED_DATE = "introducedDate";
	private static final Logger LOG = LoggerFactory.getLogger(EditComputer.class);

	@Autowired
	private ComputerService instanceService;
	@Autowired
	private ComputerMapper instanceMapper;
	@Autowired
	private CompanyService instanceCompany;
	@Autowired
	private FieldsValidator instanceValidator;

	private Map<String, String> errors = new HashMap<String, String>();

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
