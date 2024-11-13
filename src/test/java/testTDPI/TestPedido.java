/**
 * 
 */
package testTDPI;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;
import modeloDatos.Pedido;
import util.Constantes;

/**
 * 
 */
public class TestPedido {

	private Pedido pedido;
	private Cliente cliente;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	
		cliente = new Cliente("miUsuario", "12345", "Joel");
		
	}

	

	@Test
	public void testConstructor1() {
		
		pedido = new Pedido(cliente, 4, true, true, 10, Constantes.ZONA_STANDARD);
		Assert.assertNotEquals(null, pedido.getCliente());
		Assert.assertEquals(4, pedido.getCantidadPasajeros());
		Assert.assertTrue(pedido.isMascota());
		Assert.assertTrue(pedido.isBaul());
		Assert.assertEquals(10, pedido.getKm());
		Assert.assertEquals(Constantes.ZONA_STANDARD, pedido.getZona());

	}
	
	@Test
	public void testConstructor2() {
		
		pedido = new Pedido(cliente, 4, false, true, 10, Constantes.ZONA_PELIGROSA);
		Assert.assertNotEquals(null, pedido.getCliente());
		Assert.assertEquals(4, pedido.getCantidadPasajeros());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertTrue(pedido.isBaul());
		Assert.assertEquals(10, pedido.getKm());
		Assert.assertEquals(Constantes.ZONA_PELIGROSA, pedido.getZona());

	}
	
	@Test
	public void testConstructor() {
		
		pedido = new Pedido(cliente, 1, false, false, 12, Constantes.ZONA_SIN_ASFALTAR);
		Assert.assertNotEquals(null, pedido.getCliente());
		Assert.assertEquals(1, pedido.getCantidadPasajeros());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertFalse(pedido.isBaul());
		Assert.assertEquals(12, pedido.getKm());
		Assert.assertEquals(Constantes.ZONA_SIN_ASFALTAR, pedido.getZona());

	}

}
