package controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.persistence.CompanyDAO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
@WebServlet("/editComputer")
public class EditComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String VUE = "/WEB-INF/views/editComputer.jsp";
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

    private List<Company> companies;
    private Map<String, String> errors;

    public EditComputer() {
        super();
		errors = new HashMap<String, String>();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		companies = instanceCompany.get();
		try {
			Computer myComputer = instanceService.get(Integer.valueOf(request.getParameter("idComputer")));
			request.setAttribute("computer", myComputer);
			request.setAttribute("companies", companies);
		} catch (NumberFormatException e) {
			log.error("Id non valide : " + e.getMessage());
		} catch (ComputerNotFoundException e) {
			log.error("Id non valide : " + e.getMessage());
		}

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		companies = instanceCompany.get();
		ComputerDTO computer = instanceValidator.createFromRequest(request, errors, true);

        if (computer != null) {
        	try {
        		Computer newComputer = instanceMapper.fromComputerDTO(computer);
        		instanceService.update(newComputer);
        		response.sendRedirect("index?success=true&numPage=1");
        	} catch (InvalidDataException e) {
        		errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
        		request.setAttribute("companies", companies);
    	        request.setAttribute("errors", errors);
        	}
        }
	}
}
