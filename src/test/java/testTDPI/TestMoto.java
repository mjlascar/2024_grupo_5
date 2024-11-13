package testTDPI;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Cliente;
import modeloDatos.Moto;
import modeloDatos.Pedido;
import util.Constantes;

public class TestMoto {

	private Moto moto;
	
	@Before
	public void setUp() throws Exception {
	
		moto = new Moto("ASD123");
	
	}

	@Test
	public void testConstructor() {
		Assert.assertEquals("ASD123", moto.getPatente());
		Assert.assertEquals(1 , moto.getCantidadPlazas());
		Assert.assertFalse(moto.isMascota());
	}

  	 //Este metodo testea la obtencion del puntaje de la moto para un pedido para 1 persona, sin mascotas ni baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje1() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 1, false, false, 3, Constantes.ZONA_PELIGROSA);
			Integer x = 1000;
			Assert.assertEquals(x,moto.getPuntajePedido(pedido));

		}
		
		//Este metodo testea la obtencion del puntaje de la moto para un pedido para dos personas, con  sin mascotas ni baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje2() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 2, false, false, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,moto.getPuntajePedido(pedido));

		}

		//Este metodo testea la obtencion del puntaje de la combi para un pedido para 1 persona, sin mascotas y con baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje3() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 1, false, true, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,moto.getPuntajePedido(pedido));

		}

		//Este metodo testea la obtencion del puntaje de la combi para un pedido para 1 persona, con mascotas y sin baul, de 3 km y a zona peligrosa
		@Test
		public void testGetPuntaje4() {
			
			Pedido pedido = new Pedido(new Cliente("jorge", "1234", "Jorge"), 1, true, false, 3, Constantes.ZONA_PELIGROSA);
			Assert.assertEquals(null,moto.getPuntajePedido(pedido));

		}
}

