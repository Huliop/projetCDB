import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.ComputerDAO;

import configuration.WebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class TestComputerDAO {
	
	@Autowired
	private ComputerDAO instanceDAO;
	private final String SELECT_ALL = "SELECT computer.id, computer.name, introduced, discontinued, company.id, company.name FROM computer LEFT OUTER JOIN company ON computer.company_id = company.id";

	@Test
	public void testGetById() {
		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());
		Optional<Computer> computerToTestFalse = Optional.of(new Computer.ComputerBuilder().withId(1).withName("CM-2a").build());
		Optional<Computer> computer = instanceDAO.get(1);

		assertTrue("La méthode get(int) de ComputerDAO ne retourne pas de compagnies", computer != null);

		assertEquals("La méthode get(int) de ComputerDAO ne renvoie pas l'ordinateur souhaité (différents au lieu de pareils)", computer, computerToTest);
		assertNotEquals("La méthode get(int) de ComputerDAO ne renvoie pas l'ordianteur souhaité (pareils au lieu de différents)", computer, computerToTestFalse);
	}

	@Test
	public void testGet() {
		final int nbRowsTest =	7;
		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());

		List<Computer> lComputer = instanceDAO.get();

		assertTrue("La méthode get de CompanyDAO ne retourne pas de computers", lComputer != null);
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (longueur erronnée)", nbRowsTest, lComputer.size());
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (compagnie erronnée)", computerToTest, Optional.of(lComputer.get(0)));
	}

	@Test
	public void testGetPageOfComputer() {
		Page<Computer> pageToTest = new Page<Computer>();
		pageToTest.setOffset(5);
		int offset = pageToTest.getOffset();
		instanceDAO.get(pageToTest, SELECT_ALL, false);

		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());

		List<Computer> lComputer = pageToTest.getElements();

		assertTrue("La méthode getPage de ComputerDAO ne retourne pas de computers", lComputer != null);
		assertEquals("La méthode getPage de CumputerDAO ne renvoie pas la bonne liste (longueur erronnée)", offset, lComputer.size());
		assertEquals("La méthode getPage de ComputerDAO ne renvoie pas la bonne liste (compagnie erronnée)", computerToTest, Optional.of(lComputer.get(0)));
	}

	@Test
	public void testCreate() {
		List<Computer> lComputerAvant = instanceDAO.get();
		int nbAvant = lComputerAvant.size();

		try {
			instanceDAO.create(new Computer.ComputerBuilder().
					withName("PC-BG").
					withIntroduced(LocalDate.parse("2011-11-11")).
					withDiscontinued(LocalDate.parse("2012-11-11")).
					withCompany(new Company.CompanyBuilder().
							withId(1).
							withName("Apple Inc.").build())
					.build());
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Computer> lComputerApres = instanceDAO.get();
		
		int nbApres = lComputerApres.size();

		assertEquals("Le nombre d'ordinateur n'a pas augmenté, pas créé.. ?", nbAvant + 1, nbApres);
		assertEquals("L'ID de l'ordinateur créé n'a pas été correctement incrémenté de 1", lComputerApres.get(nbApres - 1).getId(), Integer.valueOf(lComputerApres.get(nbApres - 2).getId() + 1));
		assertEquals("Le nom de l'ordinateur créé n'est pas le même que celui souhaité", lComputerApres.get(nbApres - 1).getName(), "PC-BG");
		assertEquals("La date d'introduction de l'ordinateur créé n'est pas la même que celle souhaitée", lComputerApres.get(nbApres - 1).getIntroduced(), LocalDate.parse("2011-11-11"));
		assertEquals("La date d'arrêt de l'ordinateur créé n'est pas le même que celle souhaitée", lComputerApres.get(nbApres - 1).getDiscontinued(), LocalDate.parse("2012-11-11"));
		assertEquals("L'id de la compagnie créée n'est pas le même que celui souhaité", lComputerApres.get(nbApres - 1).getCompany().getId(), Integer.valueOf(1));
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
