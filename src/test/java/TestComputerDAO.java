import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.excilys.cdb.connection.DBConnection;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.persistence.ComputerDAO;

public class TestComputerDAO {

	@Test
	public void testGetInstance() {
		assertTrue("getInstance devrait toujours renvoyer une instance", ComputerDAO.getInstance() != null);
	}

	@Test
	public void testGetById() {
		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());
		Optional<Computer> computerToTestFalse = Optional.of(new Computer.ComputerBuilder().withId(1).withName("CM-2a").build());
		Optional<Computer> computer = ComputerDAO.getInstance().get(1);

		assertTrue("La méthode get(int) de ComputerDAO ne retourne pas de compagnies", computer != null);

		assertEquals("La méthode get(int) de ComputerDAO ne renvoie pas l'ordinateur souhaité (différents au lieu de pareils)", computer, computerToTest);
		assertNotEquals("La méthode get(int) de ComputerDAO ne renvoie pas l'ordianteur souhaité (pareils au lieu de différents)", computer, computerToTestFalse);
	}

	@Test
	public void testGet() {
		final int nbRowsTest =	7;
		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());

		List<Computer> lComputer = ComputerDAO.getInstance().get();

		assertTrue("La méthode get de CompanyDAO ne retourne pas de computers", lComputer != null);
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (longueur erronnée)", nbRowsTest, lComputer.size());
		assertEquals("La méthode get de CompanyDAO ne renvoie pas la bonne liste (compagnie erronnée)", computerToTest, Optional.of(lComputer.get(0)));
	}

	@Test
	public void testGetPageOfComputer() {
		Page<Computer> pageToTest = new Page<Computer>();
		pageToTest.setOffset(5);
		int offset = pageToTest.getOffset();
		ComputerDAO.getInstance().get(pageToTest);

		Optional<Computer> computerToTest = Optional.of(new Computer.ComputerBuilder().withId(1).withName("MacBook Pro 15.4 inch").withCompany(new Company.CompanyBuilder().withId(1).withName("Apple Inc.").build()).build());

		List<Computer> lComputer = pageToTest.getElements();

		assertTrue("La méthode getPage de ComputerDAO ne retourne pas de computers", lComputer != null);
		assertEquals("La méthode getPage de CumputerDAO ne renvoie pas la bonne liste (longueur erronnée)", offset, lComputer.size());
		assertEquals("La méthode getPage de ComputerDAO ne renvoie pas la bonne liste (compagnie erronnée)", computerToTest, Optional.of(lComputer.get(0)));
	}

	@Test
	public void testCreate() {
		ComputerDAO instance = ComputerDAO.getInstance();

		List<Computer> lComputerAvant = instance.get();
		int nbAvant = lComputerAvant.size();

		instance.create(new Computer.ComputerBuilder().
				withName("PC-BG").
				withIntroduced(LocalDate.parse("2011-11-11")).
				withDiscontinued(LocalDate.parse("2012-11-11")).
				withCompany(new Company.CompanyBuilder().
						withId(1).
						withName("Apple Inc.").build())
				.build());

		List<Computer> lComputerApres = instance.get();
		
		instance.get().stream().forEach(System.out::println);
		
		int nbApres = lComputerApres.size();

		assertEquals("Le nombre d'ordinateur n'a pas augmenté, pas créé.. ?", nbAvant + 1, nbApres);
		assertEquals("L'ID de l'ordinateur créé n'a pas été correctement incrémenté de 1", lComputerApres.get(nbAvant - 1).getId(), Integer.valueOf(lComputerApres.get(nbApres - 1).getId() + 1));
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

}
