package webapp.controllers.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import core.exceptions.InvalidDataException;
import core.exceptions.InvalidResourceException;
import core.model.Computer;
import core.model.ComputerDTO;
import core.model.Page;
import service.ComputerService;

@Component
public class IndexUpdator {

	private ComputerService instanceService;
	private Integer nbComputers;
	private Integer nbPages = 0;
	private Map<String, String> errors = new HashMap<String, String>();
	Page<Computer> myPage = new Page<Computer>();
	
	private static final Logger LOG = LoggerFactory.getLogger(IndexUpdator.class);

	@Autowired
	public IndexUpdator(ComputerService instanceService) {
		this.instanceService = instanceService;
	}

	private void updateNumPage(String numPage) throws InvalidDataException, InvalidResourceException {
		if (numPage != null) {
			try {
				Integer i = Integer.parseInt(numPage);
				if (i >= 1 && i <= nbPages) {
					try {
						myPage.setCurrentPage(i - 1);
					} catch (Exception e) {
						throw new InvalidResourceException("You tried to reach a page that doesn't exist");
					}
				} else if (i < 1 || i > nbPages) {
					throw new InvalidResourceException("You tried to reach a page that doesn't exist");
				}
			} catch (NumberFormatException e) {
				throw new InvalidDataException("You gave an unvalid index value");
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

	private void updateOffset(String offset) throws InvalidDataException {
		if (offset != null) {
			try {
				Integer i = Integer.parseInt(offset);
				myPage.setOffset(i);

				double newPage = (myPage.getCurrentPage() + 1)
						* ((double) (nbComputers / i + (nbComputers % i == 0 ? 0 : 1)) / (double) nbPages);
				myPage.setCurrentPage((int) newPage - 1);

			} catch (NumberFormatException e) {
				throw new InvalidDataException("You gave an unvalid index value");
			}
		}
	}

	public ModelAndView updatePages(String search, boolean isSearch, String numPage, String offset)
			throws InvalidDataException, InvalidResourceException {

		if (isSearch) {
			nbComputers = search(search).size();
		} else {
			nbComputers = instanceService.get().size();
		}

		updateNumPage(numPage);
		updateOffset(offset);

		nbPages = nbComputers / myPage.getOffset() + (nbComputers % myPage.getOffset() == 0 ? 0 : 1);

		if (isSearch) {
			instanceService.get(myPage, search);
		} else {
			instanceService.get(myPage, "");
		}

		List<Computer> computers = myPage.getElements();
		int numPages = myPage.getCurrentPage() + 1;
		List<String> navFooter = this.getNavFooter(myPage.getCurrentPage());

		return new ModelAndView("index").addObject("computers", computers).addObject("nbComputers", nbComputers)
				.addObject("navFooter", navFooter).addObject("numPages", numPages).addObject("nbPages", nbPages);
	}

	public ModelAndView delete(String param, String search, String numPage, String offset)
			throws InvalidDataException, InvalidResourceException {
		if (param.length() > 0) {
			LOG.error("here");
			String[] lCleanCompSelected = param.split(",");
			int length = lCleanCompSelected.length;
			for (String s : lCleanCompSelected) {
				try {
					LOG.error("sex");
					instanceService.delete(Integer.valueOf(s));
				} catch (Exception e) {
					errors.put("errorDeleting", "A problem has occured while deleting the computers.. Try Again");
					return updatePages(search, ((search != null && search.trim().length() > 0) ? true : false), numPage,
							offset).addObject("errors", errors);
				}
			}

			// int nPage = (myPage.getElements().size() != length ? myPage.getCurrentPage()
			// + 1 : myPage.getCurrentPage());

			if (search != null && search.trim().length() > 0) {
				return updatePages(search, true, numPage, offset).addObject("length", length)
						.addObject("successDelete", true).addObject("search", search);
			}
			return updatePages(search, false, numPage, offset).addObject("length", length).addObject("successDelete",
					true);
		} else {
			errors.put("errorDeleting", "You've selected no computer(s) to delete");
			return updatePages(search, ((search != null && search.trim().length() > 0) ? true : false), numPage, offset)
					.addObject("errors", errors);
		}
	}

	public List<ComputerDTO> search(String param) {
		if (param.length() > 0) {
			try {
				List<ComputerDTO> lComputer = instanceService.search(param);
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
