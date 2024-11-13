package testTDPI;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import modeloDatos.Administrador;

public class TestAdministrador_Esc2 {

	private Administrador admin;
	
	@Before
	public void setUp() throws Exception {
		admin = Administrador.getInstance();
	}

	@Test
	public void testGetInstance() {
		Administrador adminTest = Administrador.getInstance();		
		Assert.assertEquals(adminTest, admin);
	}
	
	@Test
	public void testGetNombreUsuario() {

		Assert.assertEquals("admin", admin.getNombreUsuario());
		
	}
	
	@Test
	public void testGetPass() {
		
		Assert.assertEquals("admin", admin.getPass());
		
	}
}
