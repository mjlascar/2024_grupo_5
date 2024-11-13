package testTDPI;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import modeloDatos.Administrador;

public class TestAdministrador_Esc1 {

	private Administrador admin;
	
	@Before
	public void setUp() throws Exception {
		admin = null;
	}

	@Test
	public void testGetInstance() {
		
		admin = Administrador.getInstance();
		Assert.assertNotEquals(null, admin);
		
	}
}
