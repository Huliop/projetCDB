package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

@WebServlet("/index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String VUE = "/WEB-INF/views/dashboard.jsp";
	public static final String ERROR500 = "/WEB-INF/views/500.jsp";
	private ComputerService instanceService;
	private Integer nbComputers;
	private Integer nbPages;
	private Map<String, String> errors;
	Page<Computer> myPage = new Page<Computer>();

    public Index() {
    	super();
    	instanceService = ComputerService.getInstance();
    	errors = new HashMap<String, String>();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		nbComputers = instanceService.get().size();
		
		if (request.getParameter("numPage") != null) {
			updateNumPage(request, response);
		}
		
		if (request.getParameter("offset") != null) {
			updateOffset(request, response);
		}

		instanceService.get(myPage);
		List<Computer> computers = myPage.getElements();
		int numPages = myPage.getCurrentPage() + 1;

		nbPages = nbComputers / myPage.getOffset() + (nbComputers % myPage.getOffset() == 0 ? 0 : 1);
		List<String> navFooter = this.getNavFooter(myPage.getCurrentPage());

		request.setAttribute("computers", computers);
		request.setAttribute("nbComputers", nbComputers);
		request.setAttribute("navFooter", navFooter);
		request.setAttribute("numPages", numPages);

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String lCompSelected = request.getParameter("selection");
		
		String[] lCleanCompSelected = lCompSelected.split(",");
		int length = lCleanCompSelected.length;
		
		for (String s : lCleanCompSelected) {
			try {
				instanceService.delete(Integer.valueOf(s));
			} catch (Exception e) {
				System.out.println("tthere");
				errors.put("errorDeleting","A problem has occured while deleting the computers.. Try Again");
			}
		}
		
		if (errors.size() == 0) {
			response.sendRedirect("index?successDelete=true&numPage=1&length=" + length);
		}
		else {
			request.setAttribute("errors", errors);
			response.sendRedirect("505");
		}
		
	}
	
	private void updateNumPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer i = Integer.parseInt(request.getParameter("numPage"));
			if (i > 0 || i < nbPages + 1) {
				try {
					myPage.setCurrentPage(i-1);
				} catch (Exception e){
					System.out.println(e.getMessage());
				}
			}
		} catch (NumberFormatException e) {
			this.getServletContext().getRequestDispatcher(ERROR500).forward(request, response);
		}
	}
	
	private void updateOffset(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Integer i = Integer.parseInt(request.getParameter("offset"));
			myPage.setOffset(i);

			double newPage = (myPage.getCurrentPage() + 1) * ((double) (nbComputers / i + (nbComputers % i == 0 ? 0 : 1)) / (double) nbPages);
			myPage.setCurrentPage((int) newPage - 1);

		} catch (NumberFormatException e) {
			this.getServletContext().getRequestDispatcher(ERROR500).forward(request, response);
		}
	}

	private List<String> getNavFooter(Integer page) {
		List<String> lNavFooter = new ArrayList<String>();
		if ((page + 1) == 1) {
			lNavFooter.add("1");
			lNavFooter.add("...");
			lNavFooter.add(nbPages.toString());
		} else if ((page + 1) == 2) {
			lNavFooter.add("1");
			lNavFooter.add("2");
			lNavFooter.add("...");
			lNavFooter.add(nbPages.toString());
		} else if ((page + 1) == (nbPages - 1)) {
			lNavFooter.add("1");
			lNavFooter.add("...");
			lNavFooter.add((Integer.valueOf(page + 1)).toString());
			lNavFooter.add(nbPages.toString());
		} else if ((page + 1) == nbPages) {
			lNavFooter.add("1");
			lNavFooter.add("...");
			lNavFooter.add(nbPages.toString());
		} else {
			lNavFooter.add("1");
			lNavFooter.add("...");
			lNavFooter.add((Integer.valueOf(page + 1)).toString());
			lNavFooter.add("...");
			lNavFooter.add(nbPages.toString());
		}
		return lNavFooter;
	}
}
