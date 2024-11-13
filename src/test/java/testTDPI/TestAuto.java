package testTDPI;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Auto;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import util.Constantes;

public class TestAuto {

	private Auto auto;
	private Pedido pedido;
	
	@Before
	public void setUp() throws Exception {
		
		auto = new Auto("azw 638", 3, false);
		
	}

	@Test
	public void testConstructor() {
		
		Auto auto2 = new Auto("ola 420", 2, true);
		
		Assert.assertEquals("ola 420", auto2.getPatente());
		Assert.assertEquals(2,auto2.getCantidadPlazas());
		Assert.assertTrue(auto2.isMascota());

	}
	
	//Este metodo testea la obtencion del puntaje del auto para un pedido para dos personas, sin mascotas y con baul, de 3 km y a zona peligrosa
	@Test
	public void testGetPuntaje1() {
		
		pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 2, false, true, 3, Constantes.ZONA_PELIGROSA);
		Integer x = 80;
		Assert.assertEquals(x,auto.getPuntajePedido(pedido));

	}
	
	//Este metodo testea la obtencion del puntaje del auto para un pedido para dos personas, sin mascotas ni baul, de 3 km y a zona peligrosa
	@Test
	public void testGetPuntaje2() {
		
		pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 2, false, false, 3, Constantes.ZONA_PELIGROSA);
		Integer x = 60;
		Assert.assertEquals(x,auto.getPuntajePedido(pedido));

	}

	//Este metodo testea la obtencion del puntaje del auto para un pedido para 5 personas, con baul y sin mascotas, de 3 km y a zona peligrosa
	@Test
	public void testGetPuntaje3() {
		
		pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 5, false, true, 3, Constantes.ZONA_PELIGROSA);
		Assert.assertEquals(null,auto.getPuntajePedido(pedido));

	}
	
	//Este metodo testea la obtencion del puntaje del auto para un pedido para 2 personas, con baul y mascotas, de 3 km y a zona peligrosa
	@Test
	public void testGetPuntaje4() {
			
		pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 2, true, true, 3, Constantes.ZONA_PELIGROSA);
		Assert.assertEquals(null,auto.getPuntajePedido(pedido));

	}
}
