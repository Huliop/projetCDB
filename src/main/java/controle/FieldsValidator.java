package controle;

import java.time.LocalDate;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.excilys.cdb.model.ComputerDTO;

@Component
public class FieldsValidator {
	private static final String CHAMP_COMPUTER_ID = "id";
	private static final String CHAMP_COMPUTER_NAME = "computerName";
	private static final String CHAMP_DISCONTINUED_DATE = "discontinuedDate";
	private static final Logger LOG = LoggerFactory.getLogger(FieldsValidator.class);

	public ComputerDTO validate(Map<String, String> errors, boolean isEdit, ComputerDTO computer) {
		String name          = verifNull(computer.getName());
		String introduced    = verifNull(computer.getIntroduced());
		String discontinued  = verifNull(computer.getDiscontinued());
		Integer computerId   = computer.getId();
		Integer companyId    = computer.getCompanyId();

		assertIdComputerValid(computerId, errors, isEdit);
		assertNameNotEmpty(name, errors);
		verifDates(introduced, discontinued, errors);

		if (errors.isEmpty()) {
			ComputerDTO comp = new ComputerDTO.ComputerDTOBuilder().withName(name)
					.withCompanyId(companyId != null ? companyId : null)
					.withIntroduced(introduced != null ? introduced : null)
					.withDiscontinued(discontinued != null ? discontinued : null).build();
			if (!isEdit) {
				return comp;
			} else {
				comp.setId(computerId);
				return comp;
			}
		} else {
			return null;
		}
	}

	public void assertNameNotEmpty(String name, Map<String, String> errors) {
		if (name == null) {
			errors.put(CHAMP_COMPUTER_NAME, "You must give a computer name");
		}
	}

	public void assertIdComputerValid(Integer id, Map<String, String> errors, boolean isEdit) {
		if (isEdit && id == 0) {
			errors.put(CHAMP_COMPUTER_ID, "You must give a valid id");
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
