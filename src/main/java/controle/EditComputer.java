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
	private static final String CHAMP_COMPUTER_NAME      = "computerName";
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";
    private static final String CHAMP_DISCONTINUED_DATE  = "discontinuedDate";
    private static final String CHAMP_COMPANY_ID         = "company";
    
    private ComputerService instanceService;
    private ComputerMapper  instanceMapper;
    private List<Company> companies;
	private boolean success;
    private Map<String, String> errors;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditComputer() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			Computer myComputer = ComputerService.getInstance().get(Integer.valueOf(request.getParameter("idComputer")));
			request.setAttribute("computer", myComputer);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ComputerNotFoundException e) {
			e.printStackTrace();
		}
		
		errors = new HashMap<String, String>();
		
		companies = CompanyService.getInstance().get();
		request.setAttribute("companies", companies);
		request.setAttribute("success", success);
        request.setAttribute("errors", errors);
		
		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		instanceService = ComputerService.getInstance();
		instanceMapper = ComputerMapper.getInstance();
		errors = new HashMap<String, String>();
		companies = CompanyService.getInstance().get();

		if (!updateFromRequest(request)) {
			request.setAttribute("companies", companies);
	        request.setAttribute("errors", errors);
		} else {
			//this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp" + "?success=true&numPage=3").forward(request, response);
			response.sendRedirect("index?success=true&numPage=1");
		}
	}
	
	private boolean updateFromRequest(HttpServletRequest request) {
		String computerName = getValeurChamp(request, CHAMP_COMPUTER_NAME);
		String introduced   = getValeurChamp(request, CHAMP_INTRODUCED_DATE);
		String discontinued = getValeurChamp(request, CHAMP_DISCONTINUED_DATE);
		String companyId    = getValeurChamp(request, CHAMP_COMPANY_ID);
		Integer id = Integer.valueOf(request.getParameter("idComputer"));
		
		try {
			verifDate(introduced);
			try {
				verifDate(discontinued);
				try {
					verifOrdreDate(introduced, discontinued);
					if (computerName != null) {
						ComputerDTO comp = new ComputerDTO.ComputerDTOBuilder()
								.withId(id)
								.withName(computerName)
								.withIntroduced(introduced != null ? introduced : null)
								.withDiscontinued(discontinued != null ? discontinued : null)
								.withCompanyId(Integer.valueOf(companyId) != 0 ? Integer.valueOf(companyId) : null)
								.build();
						instanceService.update(instanceMapper.fromComputerDTO(comp));
						return true;
					} else {
						errors.put(CHAMP_COMPUTER_NAME, "You must give a computer name");
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
		return false;
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
