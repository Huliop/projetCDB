package controle;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.ComputerDTO;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class AddComputer
 */
@WebServlet("/addComputer")
public class AddComputer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final  String VUE       = "/WEB-INF/views/addComputer.jsp";
    private static final String CHAMP_COMPUTER_NAME      = "computerName";
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";
    private static final String CHAMP_DISCONTINUED_DATE  = "discontinuedDate";
    private static final String CHAMP_COMPANY_ID         = "company";


    private List<Company> companies;
    private ComputerService instanceService;
    private ComputerMapper  instanceMapper;
    private boolean success;
    private Map<String, String> errors;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComputer() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		companies = CompanyService.getInstance().get();
		request.setAttribute("companies", companies);
		errors = new HashMap<String, String>();

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		instanceService = ComputerService.getInstance();
		instanceMapper = ComputerMapper.getInstance();
		companies = CompanyService.getInstance().get();
		errors = new HashMap<String, String>();

        ComputerDTO computer = createFromRequest(request);

        if (computer != null) {
        	Computer newComputer = instanceMapper.fromComputerDTO(computer);
        	instanceService.create(newComputer);
        	success = true;
        } else {
        	success = false;
        }

        request.setAttribute("companies", companies);
        request.setAttribute("success", success);
        request.setAttribute("errors", errors);

        this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	private ComputerDTO createFromRequest(HttpServletRequest request) {
		String computerName = getValeurChamp(request, CHAMP_COMPUTER_NAME);
		String introduced   = getValeurChamp(request, CHAMP_INTRODUCED_DATE);
		String discontinued = getValeurChamp(request, CHAMP_DISCONTINUED_DATE);
		String companyId    = getValeurChamp(request, CHAMP_COMPANY_ID);

		try {
			verifDate(introduced);
			try {
				verifDate(discontinued);
				try {
					verifOrdreDate(introduced, discontinued);
					if (computerName != null) {
						return new ComputerDTO.ComputerDTOBuilder()
								.withName(computerName)
								.withCompanyId(Integer.valueOf(companyId) != 0 ? Integer.valueOf(companyId) : null)
								.withIntroduced(introduced != null ? introduced : null)
								.withDiscontinued(discontinued != null ? discontinued : null)
								.build();
					}
				} catch (Exception e) {
					errors.put(CHAMP_DISCONTINUED_DATE, e.getMessage());
				}
			} catch (Exception e) {
				errors.put(CHAMP_DISCONTINUED_DATE, e.getMessage());
			}
		} catch (Exception e) {
			errors.put(CHAMP_INTRODUCED_DATE, e.getMessage());
			try {
				verifDate(discontinued);
			} catch (Exception ex) {
				errors.put(CHAMP_DISCONTINUED_DATE, ex.getMessage());
			}
		}
		return null;
	}

	private void verifDate(String date) throws Exception {
		if (date != null) {
			try {
				LocalDate.parse(date);
			} catch (Exception e) {
				throw new Exception("You must give a valid date");
			}
		}
	}

	private void verifOrdreDate(String introduced, String discontinued) throws Exception {
		if (introduced != null && discontinued != null) {
			if (!LocalDate.parse(discontinued).isAfter(LocalDate.parse(introduced))) {
				throw new Exception("You must give a discontinued date which is later than introduced's");
			}
		}
	}

	private String getValeurChamp(HttpServletRequest request, String nomChamp) {
	    String valeur = request.getParameter(nomChamp);
	    if (valeur == null || valeur.trim().length() == 0) {
	    	if (nomChamp.equals(CHAMP_COMPUTER_NAME)) {
	    		errors.put(CHAMP_COMPUTER_NAME, "You must give a computer name");
	    	}
	        return null;
	    } else {
	        return valeur.trim();
	    }
	}

}
