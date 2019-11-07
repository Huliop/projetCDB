package controle;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
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
		return new ModelAndView("addComputer", "companies", instanceCompany.get());
	}

	@RequestMapping(value = "/addComputer", method = RequestMethod.POST)
	public ModelAndView postAdd(@RequestParam(value = "idComputer", required = false) String id,
			@RequestParam(value = "computerName", required = false) String name,
			@RequestParam(value = "introducedDate", required = false) String intDate,
			@RequestParam(value = "discontinuedDate", required = false) String disDate,
			@RequestParam(value = "company", required = false) String cId) {
		ComputerDTO computer = instanceValidator.createFromRequest(errors, false, id, name, intDate, disDate, cId);

		if (computer != null) {
			try {
				Computer newComputer = instanceMapper.fromComputerDTO(computer);
				instanceService.create(newComputer);
				success = true;
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
