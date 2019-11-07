package controle;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.excilys.cdb.model.ComputerDTO;

@Component
public class FieldsValidator {
	private static final String CHAMP_COMPUTER_ID = "id";
	private static final String CHAMP_COMPUTER_NAME = "computerName";
	private static final String CHAMP_DISCONTINUED_DATE = "discontinuedDate";

	public ComputerDTO createFromRequest(Map<String, String> errors, boolean isEdit, String idComputer,
			String computerName, String introducedDate, String discontinuedDate, String companyId) {
		Integer id = 0;
		idComputer       = verifNull(idComputer);
		computerName     = verifNull(computerName);
		introducedDate   = verifNull(introducedDate);
		discontinuedDate = verifNull(discontinuedDate);
		companyId        = verifNull(companyId);

		verifDates(introducedDate, discontinuedDate, errors);
		assertNameNotEmpty(computerName, errors);
		id = assertIdComputerValid(idComputer, errors, isEdit);

		if (errors.isEmpty()) {
			ComputerDTO comp = new ComputerDTO.ComputerDTOBuilder().withName(computerName)
					.withCompanyId(Integer.valueOf(companyId) != 0 ? Integer.valueOf(companyId) : null)
					.withIntroduced(introducedDate != null ? introducedDate : null)
					.withDiscontinued(discontinuedDate != null ? discontinuedDate : null).build();
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

	public void assertNameNotEmpty(String name, Map<String, String> errors) {
		if (name == null || name == "") {
			errors.put(CHAMP_COMPUTER_NAME, "You must give a computer name");
		}
	}

	public Integer assertIdComputerValid(String id, Map<String, String> errors, boolean isEdit) {
		if (isEdit) {
			try {
				return Integer.valueOf(id);
			} catch (NumberFormatException e) {
				errors.put(CHAMP_COMPUTER_ID, "You must give a valid id");
			}
		}
		return Integer.valueOf(0);
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
				errors.put(CHAMP_DISCONTINUED_DATE,
						"You must give a discontinued date which is later than introduced's");
			}
		}
	}

	public void verifDates(String introducedDate, String discontinuedDate, Map<String, String> errors) {
		verifDate(introducedDate, errors);
		verifDate(discontinuedDate, errors);
		if (errors.isEmpty()) {
			verifOrdreDate(introducedDate, discontinuedDate, errors);
		}
	}

	public String verifNull(String champ) {
	    if (champ == null || champ.trim().length() == 0) {
	        return null;
	    } else {
	        return champ.trim();
	    }
	}
}
