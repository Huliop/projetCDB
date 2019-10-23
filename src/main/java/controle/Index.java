package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	private ComputerService computerService;
	private Integer nbComputers;
	private Integer nbPages;
	Page<Computer> myPage = new Page<Computer>();

    public Index() {
    	super();
    	computerService = ComputerService.getInstance();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		nbComputers = computerService.get().size();

		if (request.getParameter("numPage") != null) {
			try {
				Integer i = Integer.parseInt(request.getParameter("numPage")) - 1;
				if (i >= 0 && i <= (nbPages - 1)) {
					myPage.setCurrentPage(i);
				} else if (i == -1 && myPage.getCurrentPage() > 0) {
					myPage.setCurrentPage(myPage.getCurrentPage() - 1);
				} else if (i == nbPages && myPage.getCurrentPage() < (nbPages - 1)) {
					myPage.setCurrentPage(myPage.getCurrentPage() + 1);
				} else {
					myPage.setCurrentPage(0);
				}
			} catch (NumberFormatException e) {
				this.getServletContext().getRequestDispatcher(ERROR500).forward(request, response);
			}
		}

		if (request.getParameter("offset") != null) {
			try {
				Integer i = Integer.parseInt(request.getParameter("offset"));
				myPage.setOffset(i);

				double newPage = (myPage.getCurrentPage() + 1) * ((double) (nbComputers / i + (nbComputers % i == 0 ? 0 : 1)) / (double) nbPages);
				myPage.setCurrentPage((int) newPage - 1);

			} catch (NumberFormatException e) {
				this.getServletContext().getRequestDispatcher(ERROR500).forward(request, response);
			}
		}

		computerService.get(myPage);
		List<Computer> computers = myPage.getElements();

		nbPages = nbComputers / myPage.getOffset() + (nbComputers % myPage.getOffset() == 0 ? 0 : 1);
		List<String> navFooter = this.getNavFooter(myPage.getCurrentPage());

		request.setAttribute("computers", computers);
		request.setAttribute("nbComputers", nbComputers);
		request.setAttribute("navFooter", navFooter);
		request.setAttribute("nbPages", nbPages);

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
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
