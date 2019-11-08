package controle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
public class AddComputer {
	private static final String CHAMP_INTRODUCED_DATE = "introducedDate";

	@Autowired
	private CompanyService instanceCompany;
	@Autowired
	private ComputerService instanceService;
	@Autowired
	private ComputerMapper instanceMapper;
	@Autowired
	private FieldsValidator instanceValidator;

	private boolean success;
	private Map<String, String> errors = new HashMap<String, String>();

	@RequestMapping(value = "/addComputer", method = RequestMethod.GET)
	public ModelAndView getAdd() {
		return new ModelAndView("addComputer", "companies",
				instanceCompany.get().stream().collect(Collectors.toMap(Company::getId, Company::getName)))
						.addObject("computer", new ComputerDTO.ComputerDTOBuilder().build());
	}

	@RequestMapping(value = "/addComputer", method = RequestMethod.POST)
	public ModelAndView postAdd(@ModelAttribute("computer") ComputerDTO computer) {
		ComputerDTO newComputer = instanceValidator.validate(errors, false, computer);
		if (newComputer != null) {
			try {
				Computer myComputer = instanceMapper.fromComputerDTO(newComputer);
				instanceService.create(myComputer);
				success = true;
				return getAdd();
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
