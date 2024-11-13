package testTDPI;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;

public class TestCliente {
	
	private Cliente cliente;
	
	@Before
	public void setUp() throws Exception {
	
		cliente = new Cliente("miUsuario", "12345", "Joel");
	}

	@Test
	public void testConstructor() {
		Assert.assertEquals("miUsuario", cliente.getNombreUsuario());
		Assert.assertEquals("12345", cliente.getPass());
		Assert.assertEquals("Joel", cliente.getNombreReal());
	}

}
