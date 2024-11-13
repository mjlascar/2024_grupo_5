package testTDPI;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;
import modeloDatos.Combi;
import modeloDatos.Pedido;
import util.Constantes;

public class TestCombi {

	private Combi combi;
	
	@Before
	public void setUp() throws Exception {
	
		combi = new Combi("ASD123", 7, false);
	
	}

	@Test
	public void testConstructor() {
		Assert.assertEquals("ASD123", combi.getPatente());
		Assert.assertEquals(7 ,combi.getCantidadPlazas());
		Assert.assertFalse(combi.isMascota());
	}

  	 //Este metodo testea la obtencion del puntaje de la combi para un pedido para 6 personas, sin mascotas y con baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje1() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 6, false, true, 3, Constantes.ZONA_PELIGROSA);
			Integer x = 160;
			Assert.assertEquals(x,combi.getPuntajePedido(pedido));

		}
		
		//Este metodo testea la obtencion del puntaje de la combi para un pedido para 6 personas, sin mascotas ni baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje2() {
					
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 6, false, false, 3, Constantes.ZONA_PELIGROSA);
			Integer x = 60;
			Assert.assertEquals(x,combi.getPuntajePedido(pedido));

		}
		
		//Este metodo testea la obtencion del puntaje de la combi para un pedido para dos personas, sin mascotas y con baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje3() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 2, false, true, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,combi.getPuntajePedido(pedido));

		}

		//Este metodo testea la obtencion del puntaje de la combi para un pedido para 7 personas, con mascotas y con baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje4() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 7, true, true, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,combi.getPuntajePedido(pedido));

		}

		//Este metodo testea la obtencion del puntaje de la combi para un pedido para 9 personas, sin mascotas y con baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje5() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 9, false, true, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,combi.getPuntajePedido(pedido));

		}
}
