package controle;

import java.time.LocalDate;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.excilys.cdb.model.ComputerDTO;

public class FieldsValidator {
	private static final String CHAMP_COMPUTER_NAME      = "computerName";
    private static final String CHAMP_INTRODUCED_DATE    = "introducedDate";
    private static final String CHAMP_DISCONTINUED_DATE  = "discontinuedDate";
    private static final String CHAMP_COMPANY_ID         = "company";

	public static FieldsValidator instance;

	public static FieldsValidator getInstance() {
		if (instance == null) {
			instance = new FieldsValidator();
		}

		return instance;
	}

	public ComputerDTO createFromRequest(HttpServletRequest request, Map<String, String> errors, boolean isEdit) {
		String computerName     = getValeurChamp(request, CHAMP_COMPUTER_NAME, errors);
		String introducedDate   = getValeurChamp(request, CHAMP_INTRODUCED_DATE, errors);
		String discontinuedDate = getValeurChamp(request, CHAMP_DISCONTINUED_DATE, errors);
		String companyId        = getValeurChamp(request, CHAMP_COMPANY_ID, errors);
		Integer id = 0;

		if (isEdit) {
			id = Integer.valueOf(request.getParameter("idComputer"));
		}

		verifDate(introducedDate, errors);
		verifDate(discontinuedDate, errors);

		if (errors.size() > 0) {
			verifOrdreDate(introducedDate, discontinuedDate, errors);
		}

		if (errors.size() == 0) {
			ComputerDTO  comp = new ComputerDTO.ComputerDTOBuilder()
					.withName(computerName)
					.withCompanyId(Integer.valueOf(companyId) != 0 ? Integer.valueOf(companyId) : null)
					.withIntroduced(introducedDate != null ? introducedDate : null)
					.withDiscontinued(discontinuedDate != null ? discontinuedDate : null)
					.build();
			if (!isEdit) {
				return comp;
			} else {
				comp.setId(id);
				return comp;
			}
		} else {
			return null;
		}
	}

	public void verifDate(String date, Map<String, String> errors) {
		if (date != null) {
			try {
				LocalDate.parse(date);
			} catch (Exception e) {
				errors.put(date, "You must give a valid date");
			}
		}
	}

	public void verifOrdreDate(String introduced, String discontinued, Map<String, String> errors) {
		if (introduced != null && discontinued != null) {
			if (!LocalDate.parse(discontinued).isAfter(LocalDate.parse(introduced))) {
				errors.put(CHAMP_DISCONTINUED_DATE, "You must give a discontinued date which is later than introduced's");
			}
		}
	}

	public String getValeurChamp(HttpServletRequest request, String nomChamp, Map<String, String> errors) {
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
