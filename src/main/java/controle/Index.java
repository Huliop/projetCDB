package controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.exceptions.InvalidResourceException;

@Controller
public class Index {

	@Autowired
	private IndexUpdator instanceUpdator;

	@GetMapping(value = { "/index", "/" })
	public ModelAndView getIndex(@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "numPage", required = false) String numPage,
			@RequestParam(value = "offset", required = false) String offset) {
		try {
			if (search != null && search != "") {
				return instanceUpdator.updatePages(search, true, numPage, offset);

			} else {
				return instanceUpdator.updatePages(null, false, numPage, offset);
			}
		} catch (InvalidDataException e) {
			return error500(e.getMessage());
		} catch (InvalidResourceException e) {
			return error404(e.getMessage());
		}
	}

	@PostMapping("/index")
	public ModelAndView postIndex(@RequestParam(value = "selection", required = false) String selection,
			@RequestParam(value = "search", required = false) String search,
			@RequestParam(value = "numPage", required = false) String numPage,
			@RequestParam(value = "offset", required = false) String offset)
			throws InvalidDataException, InvalidResourceException {
		return instanceUpdator.delete(selection, search, numPage, offset);
	}

	public ModelAndView error500(String message) {
		return new ModelAndView("500", "error", message);
	}

	@RequestMapping("/*")
	public ModelAndView error404(String message) {
		return new ModelAndView("404", "error", ( message != null && message.trim().length() > 0 ? message : "Don't try me.. Qui fait le malin tombe dans la ravin!"));
	}
}
