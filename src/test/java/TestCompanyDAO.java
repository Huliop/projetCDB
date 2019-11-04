import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.persistence.CompanyDAO;

import spring.SpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class TestCompanyDAO {
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Autowired
	private CompanyDAO instanceDAO;

	@Test
	public void testGetById() {
		Optional<Company> companyToTest = Optional.of(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build());
		Optional<Company> companyToTestFalse = Optional.of(new Company.CompanyBuilder().withId(1).withName("Thinking Machines").build());
		Optional<Company> company = instanceDAO.get(1);

		assertTrue("La méthode get(int) de CompanyDAO ne retourne pas de compagnie", company != null);

		assertEquals("La méthode get(int) de CompanyDAO ne renvoie pas la compagnie souhaitée", company, companyToTest);
		assertNotEquals("La méthode get(int) de CompanyDAO ne renvoie pas la compagnie souhaitée", company, companyToTestFalse);
	}

	@Test
	public void testGet() {
		final int nbRowsTest =	5;
		Optional<Company> companyToTest = Optional.of(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build());

		List<Company> lCompany = instanceDAO.get();

		assertTrue("La méthode get de CompanyDAO ne retourne pas de compagnies", lCompany != null);
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (longueur erronnée)", nbRowsTest, lCompany.size());
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (compagnie erronnée)", companyToTest, Optional.of(lCompany.get(0)));		
	}
}
