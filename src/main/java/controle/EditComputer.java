package controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

@WebServlet("/editComputer")
public class EditComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String VUE = "/WEB-INF/views/editComputer.jsp";
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";

    private ComputerService instanceService;
    private ComputerMapper  instanceMapper;
    private FieldsValidator instanceValidator;
    private List<Company> companies;
    private Map<String, String> errors;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputer() {
        super();

        instanceService = ComputerService.getInstance();
		instanceMapper = ComputerMapper.getInstance();
		instanceValidator = FieldsValidator.getInstance();
		companies = CompanyService.getInstance().get();
		errors = new HashMap<String, String>();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try {
			Computer myComputer = ComputerService.getInstance().get(Integer.valueOf(request.getParameter("idComputer")));
			request.setAttribute("computer", myComputer);
			request.setAttribute("companies", companies);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ComputerNotFoundException e) {
			e.printStackTrace();
		}

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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
