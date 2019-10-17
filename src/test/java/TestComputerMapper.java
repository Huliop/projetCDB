import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;

@RunWith(MockitoJUnitRunner.class)
public class TestComputerMapper {

	@Mock
	private ResultSet resultSet;

	@Test
	public void testGetInstance() {
		assertTrue("getInstance devrait toujours renvoyer une instance", ComputerMapper.getInstance() != null);
	}

	@Test
	public void testFromResultSet() throws SQLException {
		Integer myInt = 0;
		Date introduced = new Date(0);
		Date discontinued = new Date(0);
		Mockito.when(resultSet.getInt(1)).thenReturn(myInt);
		Mockito.when(resultSet.getString(2)).thenReturn("myString");
		Mockito.when(resultSet.getDate(3)).thenReturn(introduced);
		Mockito.when(resultSet.getDate(4)).thenReturn(discontinued);

		Computer computer = ComputerMapper.getInstance().fromResultSet(resultSet);

		assertNotNull("La compagnie n'a pas été créée..", computer);
		assertEquals("Le numéro de l'oridnateur créé n'est pas compatible avec celui souhaité", myInt, computer.getId());
		assertEquals("Le nom de la compagnie créee n'est pas compatible avec celui souhaité", "myString", computer.getName());
		assertEquals("La date de début de la compagnie créee n'est pas compatible avec celle souhaitée", introduced.toLocalDate(), computer.getIntroduced());
		assertEquals("La date de fin de la compagnie créee n'est pas compatible avec celle souhaitée", discontinued.toLocalDate(), computer.getDiscontinued());
	}
}
