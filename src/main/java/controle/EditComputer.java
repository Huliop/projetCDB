package controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
public class EditComputer {
	private static final String CHAMP_INTRODUCED_DATE = "introducedDate";

	@Autowired
	private ComputerService instanceService;
	@Autowired
	private ComputerMapper instanceMapper;
	@Autowired
	private FieldsValidator instanceValidator;
	@Autowired
	private CompanyService instanceCompany;

	private Map<String, String> errors = new HashMap<String, String>();

	@RequestMapping(value = "/editComputer", method = RequestMethod.GET)
	public ModelAndView getEdit(HttpServletRequest request, @RequestParam("idComputer") String id)
			throws ServletException, IOException {
		ModelAndView mv = new ModelAndView("editComputer", "companies", instanceCompany.get());
		try {
			Computer myComputer = instanceService.get(Integer.valueOf(id));
			mv.addObject("computer", myComputer);
		} catch (NumberFormatException e) {
			return new ModelAndView("500", "error", "Id non valide : " + e.getMessage());
		} catch (ComputerNotFoundException e) {
			return new ModelAndView("500", "error", "Id non valide : " + e.getMessage());
		}
		return mv;
	}

	@RequestMapping(value = "/editComputer", method = RequestMethod.POST)
	public ModelAndView postEdit(@RequestParam(value = "idComputer", required = false) String id,
			@RequestParam(value = "computerName", required = false) String name,
			@RequestParam(value = "introducedDate", required = false) String intDate,
			@RequestParam(value = "discontinuedDate", required = false) String disDate,
			@RequestParam(value = "company", required = false) String cId) throws ServletException, IOException {
		ComputerDTO computer = instanceValidator.createFromRequest(errors, true, id, name, intDate, disDate, cId);

		if (computer != null) {
			try {
				Computer newComputer = instanceMapper.fromComputerDTO(computer);
				instanceService.update(newComputer);
				return new ModelAndView("redirect:/index").addObject("companies", instanceCompany.get())
						.addObject("success", true).addObject("numPage", 1);
			} catch (InvalidDataException e) {
				errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
			}
		}

		return new ModelAndView("editComputer").addObject("companies", instanceCompany.get()).addObject("errors",
				errors);
	}
}
