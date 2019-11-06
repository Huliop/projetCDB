package controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.persistence.CompanyDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
public class EditComputer {
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";
    private static final Logger log = LoggerFactory.getLogger(CompanyDAO.class);

    @Autowired
    private ComputerService instanceService;
    @Autowired
    private ComputerMapper  instanceMapper;
    @Autowired
    private FieldsValidator instanceValidator;
    @Autowired
    private CompanyService instanceCompany;

    private Map<String, String> errors = new HashMap<String, String>();
    
    @RequestMapping(value = "/editComputer", method = RequestMethod.GET)
    public ModelAndView getEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	try {
			Computer myComputer = instanceService.get(Integer.valueOf(request.getParameter("idComputer")));
			request.setAttribute("computer", myComputer);
		} catch (NumberFormatException e) {
			log.error("Id non valide : " + e.getMessage());
		} catch (ComputerNotFoundException e) {
			log.error("Id non valide : " + e.getMessage());
		}
		return new ModelAndView("editComputer", "companies", instanceCompany.get());
	}
    
    @RequestMapping(value = "/editComputer", method = RequestMethod.POST)
    public ModelAndView postEdit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	ComputerDTO computer = instanceValidator.createFromRequest(request, errors, true);

        if (computer != null) {
        	try {
        		Computer newComputer = instanceMapper.fromComputerDTO(computer);
        		instanceService.update(newComputer);
        		response.sendRedirect("index?success=true&numPage=1");
        	} catch (InvalidDataException e) {
        		errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
        	}
        }
        
        request.setAttribute("errors", errors);
        
		return new ModelAndView("editComputer", "companies", instanceCompany.get());
	}
}
