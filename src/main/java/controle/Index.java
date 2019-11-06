package controle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.ComputerService;

@Controller
public class Index {
	private final String SELECT_ALL = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";
	private final String SEARCH = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id WHERE computer.name LIKE ? OR company.name LIKE ?";

	@Autowired
	private ComputerService instanceService;
	private Integer nbComputers;
	private Integer nbPages = 0;
	private Map<String, String> errors = new HashMap<String, String>();
	Page<Computer> myPage = new Page<Computer>();

	@RequestMapping(value = { "/index", "/" }, method = RequestMethod.GET)
	public ModelAndView getIndex(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("search") != null && request.getParameter("search") != "") {
			updatePages(request, response, search(request, response, request.getParameter("search")), true);
		} else {
			updatePages(request, response, instanceService.get(), false);
		}
		return new ModelAndView("index");
	}
	
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public void postIndex(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		delete(request, response, request.getParameter("selection"));
	}
	
	@RequestMapping("/500")
	public ModelAndView error500() {
		return new ModelAndView("500");
	}

	private void updateNumPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("numPage") != null) {
			try {
				Integer i = Integer.parseInt(request.getParameter("numPage"));
				if (i > 0 && i < nbPages + 1) {
					try {
						myPage.setCurrentPage(i - 1);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else if (i < 0 || i > nbPages + 1) {
					error500();
				}
			} catch (NumberFormatException e) {
				error500();
			}
		} else {
			if (nbPages > 0) {
				double newPage = (myPage.getCurrentPage() + 1)
						* ((double) (nbComputers / myPage.getOffset() + (nbComputers % myPage.getOffset() == 0 ? 0 : 1))
								/ (double) nbPages);
				myPage.setCurrentPage((int) newPage - 1);
			} else {
				myPage.setCurrentPage(0);
			}
		}
	}

	private void updateOffset(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			Integer i = Integer.parseInt(request.getParameter("offset"));
			myPage.setOffset(i);

			double newPage = (myPage.getCurrentPage() + 1)
					* ((double) (nbComputers / i + (nbComputers % i == 0 ? 0 : 1)) / (double) nbPages);
			myPage.setCurrentPage((int) newPage - 1);

		} catch (NumberFormatException e) {
			error500();
		}
	}

	private void updatePages(HttpServletRequest request, HttpServletResponse response, List<Computer> list,
			boolean isSearch) throws ServletException, IOException {

		nbComputers = list.size();

		updateNumPage(request, response);

		if (request.getParameter("offset") != null) {
			updateOffset(request, response);
		}

		nbPages = nbComputers / myPage.getOffset() + (nbComputers % myPage.getOffset() == 0 ? 0 : 1);

		if (isSearch) {
			instanceService.get(myPage, SEARCH, request.getParameter("search"), true);
		} else {
			instanceService.get(myPage, SELECT_ALL, "", false);
		}

		List<Computer> computers = myPage.getElements();
		int numPages = myPage.getCurrentPage() + 1;
		List<String> navFooter = this.getNavFooter(myPage.getCurrentPage());

		request.setAttribute("computers", computers);
		request.setAttribute("nbComputers", nbComputers);
		request.setAttribute("navFooter", navFooter);
		request.setAttribute("numPages", numPages);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response, String param)
			throws IOException, ServletException {
		if (param.length() > 0) {
			String[] lCleanCompSelected = param.split(",");
			int length = lCleanCompSelected.length;

			for (String s : lCleanCompSelected) {
				try {
					instanceService.delete(Integer.valueOf(s));
				} catch (Exception e) {
					errors.put("errorDeleting", "A problem has occured while deleting the computers.. Try Again");
					request.setAttribute("errors", errors);
					getIndex(request, response);
				}
			}

			int nPage = (myPage.getElements().size() != length ? myPage.getCurrentPage() + 1 : myPage.getCurrentPage());
			response.sendRedirect("index?successDelete=true&numPage=" + nPage + "&length=" + length);
		} else {
			errors.put("errorDeleting", "You've selected no computer(s) to delete");
			request.setAttribute("errors", errors);
			getIndex(request, response);
		}
	}

	private List<Computer> search(HttpServletRequest request, HttpServletResponse response, String param) {
		if (param.length() > 0) {
			try {
				List<Computer> lComputer = instanceService.search(param);
				return lComputer;
			} catch (Exception e) {
				errors.put("errorDeleting",
						"A problem has occured while searching the matching computers with this pattern.. Try again");
			}
		}
		return null;
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
