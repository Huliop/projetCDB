package controle;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

/**
 * Servlet implementation class Index
 */
@WebServlet("/index")
public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L; 
	final ComputerService computerService;
	private Integer nbComputers;
	Page<Computer> myPage = new Page<Computer>();
	
    public Index() {
    	super();
    	computerService = ComputerService.getInstance();
    	nbComputers = computerService.get().size();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		if (request.getParameter("nbPage") != null) {
			try {
				Integer i = Integer.parseInt(request.getParameter("nbPage")) - 1;
				myPage.setCurrentPage(i);
			} catch (NumberFormatException e) {
				this.getServletContext().getRequestDispatcher( "/WEB-INF/views/main/500.jsp" ).forward( request, response );
			}
		}
		
		if (request.getParameter("offset") != null) {
			try {
				Integer i = Integer.parseInt(request.getParameter("offset"));
				myPage.setOffset(i);
			} catch (NumberFormatException e) {
				this.getServletContext().getRequestDispatcher( "/WEB-INF/views/main/500.jsp" ).forward( request, response );
			}
		}
		
		computerService.get(myPage);
		List<Computer> computers = myPage.getElements();
		
		request.setAttribute("computers", computers);
		request.setAttribute("nbComputers", nbComputers);
		
		this.getServletContext().getRequestDispatcher( "/WEB-INF/views/main/dashboard.jsp" ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
