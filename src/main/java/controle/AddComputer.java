package controle;

import java.io.IOException;
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
    private boolean success = false;
    private String resultat;
    private Map<String,String> errors;
    
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
		
		this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		instanceService = ComputerService.getInstance();
		instanceMapper = ComputerMapper.getInstance();
		companies = CompanyService.getInstance().get();
		
		request.setAttribute("companies", companies);
		
        ComputerDTO computer = createFromRequest(request);
        
        Computer newComputer = instanceMapper.fromComputerDTO(computer);
        
        instanceService.create(newComputer);
		
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}
	
	private ComputerDTO createFromRequest(HttpServletRequest request) {
		String computerName = getValeurChamp(request, CHAMP_COMPUTER_NAME);
		String introduced   = getValeurChamp(request, CHAMP_INTRODUCED_DATE);
		String discontinued = getValeurChamp(request, CHAMP_DISCONTINUED_DATE);
		String companyId    = getValeurChamp(request, CHAMP_COMPANY_ID);
		
		return new ComputerDTO.ComputerDTOBuilder()
			.withName(computerName)
			.withCompanyId(Integer.valueOf(companyId))
			.withIntroduced(introduced != null ? introduced : null)
			.withDiscontinued(discontinued != null ? discontinued : null)
			.build();
	}
	
	private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
	    String valeur = request.getParameter( nomChamp );
	    if ( valeur == null || valeur.trim().length() == 0 ) {
	        return null;
	    } else {
	        return valeur.trim();
	    }
	}

}
