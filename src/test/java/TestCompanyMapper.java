import static org.junit.Assert.*;

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

import com.excilys.cdb.mappers.CompanyMapper;
import com.excilys.cdb.model.Company;

import configuration.WebConfig;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class})
public class TestCompanyMapper {
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@Autowired
	private CompanyMapper instanceMapper;
	@Mock
	private ResultSet resultSet;

	@Test
	public void testFromResultSet() throws SQLException {
		Integer myInt = 0;
		Mockito.when(resultSet.getInt(1)).thenReturn(myInt);
		Mockito.when(resultSet.getString(2)).thenReturn("myString");

		Company company = instanceMapper.mapRow(resultSet, 0);

		assertNotNull("La compagnie n'a pas été créée..", company);
		assertEquals("Le numéro de la compagnie créée n'est pas compatible avec celui souhaité", myInt, company.getId());
		assertEquals("Le nom de la compagnie créee n'est pas compatible avec celui souhaité", "myString", company.getName());
	}
}
