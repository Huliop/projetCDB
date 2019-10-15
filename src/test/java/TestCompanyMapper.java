import static org.junit.Assert.*;

import org.junit.Test;

import com.excilys.cdb.mappers.CompanyMapper;

public class TestCompanyMapper {

	@Test
	public void testGetInstance() {
		assertTrue("getInstance devrait toujours renvoyer une instance", CompanyMapper.getInstance() != null);	}

	@Test
	public void testFromResultSet() {
		fail("Not yet implemented");
	}

}
