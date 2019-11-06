package controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
public class AddComputer {
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";

    @Autowired
    private CompanyService instanceCompany;
    @Autowired
    private ComputerService instanceService;
    @Autowired
    private ComputerMapper  instanceMapper;
    @Autowired
    private FieldsValidator instanceValidator;

    private boolean success;
    private Map<String, String> errors;

    @Autowired public AddComputer() {
        super();
		errors = new HashMap<String, String>();
    }
    
    @RequestMapping(value = "/addComputer", method = RequestMethod.GET)
    public ModelAndView getAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		return new ModelAndView("addComputer", "companies", instanceCompany.get());
	}
    
    @RequestMapping(value = "/addComputer", method = RequestMethod.POST)
    public ModelAndView postAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ComputerDTO computer = instanceValidator.createFromRequest(request, errors, false);

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

        request.setAttribute("success", success);
        request.setAttribute("errors", errors);
        
		return new ModelAndView("addComputer", "companies", instanceCompany.get());
	}
}
