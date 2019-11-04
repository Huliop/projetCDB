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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@Controller
@WebServlet("/addComputer")
public class AddComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final  String VUE       = "/WEB-INF/views/addComputer.jsp";
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";

    @Autowired
    private CompanyService instanceCompany;
    @Autowired
    private ComputerService instanceService;
    @Autowired
    private ComputerMapper  instanceMapper;
    @Autowired
    private FieldsValidator instanceValidator;

    private List<Company> companies;
    private boolean success;
    private Map<String, String> errors;

    @Autowired public AddComputer() {
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
		request.setAttribute("companies", companies);
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

        request.setAttribute("companies", companies);
        request.setAttribute("success", success);
        request.setAttribute("errors", errors);

        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}
}
