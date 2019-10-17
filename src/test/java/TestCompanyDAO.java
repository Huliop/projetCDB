import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.connection.DBConnection;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;


public class TestCompanyDAO {

	@BeforeClass
	public static void beforeAll() {
		DBConnection dbC = DBConnection.getInstance();
		dbC.changeURLToTest();
	}

	@Test
	public void testGetInstance() {
		assertTrue("getInstance devrait toujours renvoyer une instance", CompanyDAO.getInstance() != null);
	}

	@Test
	public void testGetById() {
		Optional<Company> companyToTest = Optional.of(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build());
		Optional<Company> companyToTestFalse = Optional.of(new Company.CompanyBuilder().withId(1).withName("Thinking Machines").build());
		Optional<Company> company = CompanyDAO.getInstance().get(1);

		assertTrue("La méthode get(int) de CompanyDAO ne retourne pas de compagnie", company != null);

		assertEquals("La méthode get(int) de CompanyDAO ne renvoie pas la compagnie souhaitée", company, companyToTest);
		assertNotEquals("La méthode get(int) de CompanyDAO ne renvoie pas la compagnie souhaitée", company, companyToTestFalse);
	}

	@Test
	public void testGet() {
		final int nbRowsTest =	5;
		Optional<Company> companyToTest = Optional.of(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build());

		List<Company> lCompany = CompanyDAO.getInstance().get();

		assertTrue("La méthode get de CompanyDAO ne retourne pas de compagnies", lCompany != null);
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (longueur erronnée)", nbRowsTest, lCompany.size());
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (compagnie erronnée)", companyToTest, Optional.of(lCompany.get(0)));		
	}
}
