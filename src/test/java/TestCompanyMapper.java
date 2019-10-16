import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.excilys.cdb.mappers.CompanyMapper;
import com.excilys.cdb.model.Company;


@RunWith(MockitoJUnitRunner.class)
public class TestCompanyMapper {
	
	
	@Mock
	private ResultSet resultSet;
	
	@Test
	public void testGetInstance() {
		assertTrue("getInstance devrait toujours renvoyer une instance", CompanyMapper.getInstance() != null);
	}

	@Test
	public void testFromResultSet() throws SQLException {
		Integer myInt = 0;
		Mockito.when(resultSet.getInt(1)).thenReturn(myInt);
		Mockito.when(resultSet.getString(2)).thenReturn("myString");
		
		Company company = CompanyMapper.getInstance().fromResultSet(resultSet);
		
		assertNotNull("La compagnie n'a pas été créée..", company);
		assertEquals("Le numéro de la compagnie créée n'est pas compatible avec celui souhaité", myInt, company.getId());
		assertEquals("Le nom de la compagnie créee n'est pas compatible avec celui souhaité", "myString", company.getName());
	}

}
