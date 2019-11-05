import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.excilys.cdb.mappers.ComputerMapper;
import com.excilys.cdb.model.Computer;

import configuration.SpringConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfiguration.class})
public class TestComputerMapper {
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Autowired
	private ComputerMapper instanceMapper;
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testFromResultSet() throws SQLException {
		Integer myInt = 0;
		Date introduced = new Date(0);
		Date discontinued = new Date(0);
		Mockito.when(resultSet.getInt(1)).thenReturn(myInt);
		Mockito.when(resultSet.getString(2)).thenReturn("myString");
		Mockito.when(resultSet.getDate(3)).thenReturn(introduced);
		Mockito.when(resultSet.getDate(4)).thenReturn(discontinued);

		Computer computer = instanceMapper.mapRow(resultSet, 0);

		assertNotNull("La compagnie n'a pas été créée..", computer);
		assertEquals("Le numéro de l'oridnateur créé n'est pas compatible avec celui souhaité", myInt, computer.getId());
		assertEquals("Le nom de la compagnie créee n'est pas compatible avec celui souhaité", "myString", computer.getName());
		assertEquals("La date de début de la compagnie créee n'est pas compatible avec celle souhaitée", introduced.toLocalDate(), computer.getIntroduced());
		assertEquals("La date de fin de la compagnie créee n'est pas compatible avec celle souhaitée", discontinued.toLocalDate(), computer.getDiscontinued());
	}
}
