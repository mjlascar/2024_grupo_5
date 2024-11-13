package testTDPI;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Auto;
import modeloDatos.Chofer;
import modeloDatos.ChoferPermanente;
import modeloDatos.Cliente;
import modeloDatos.Pedido;
import modeloDatos.Viaje;
import util.Constantes;

public class TestViaje {
	private Auto auto;
	private Pedido pedido;
	private Cliente cliente;
	private Chofer chofer;
	private Viaje viaje;
	double base = 1000.0;

	@Before
	public void setUp() throws Exception {
		auto = new Auto("azw 638", 3, true);
		chofer = new ChoferPermanente("12412512", "Marcus", 2020, 2);
        cliente = new Cliente("miUsuario", "12345", "Joel");
		pedido = new Pedido(cliente, 4, true, true, 10, Constantes.ZONA_STANDARD);
		viaje = new Viaje(pedido,chofer,auto);
		Viaje.setValorBase(base);
	}

	
	@Test
	public void testConstructor() {
		Assert.assertEquals(pedido, viaje.getPedido());
		Assert.assertEquals(chofer, viaje.getChofer());
		Assert.assertEquals(auto, viaje.getVehiculo());
	}

	
	@Test
	public void testGetValor(){
		double esperado = base+ base*(1+(0.1*4))+ base*(1+(0.1*10))+ base*(1+(0.1*4))+ base*(1+(0.2*10))+ base*(1+(0.1*4))+ base*(1+(0.05*10)); 
		Assert.assertEquals(esperado, viaje.getValor(), 0.000001);
	}

	
	@Test
	public void testFinalizaViajes(){
		viaje.finalizarViaje(4);
		Assert.assertEquals(4, viaje.getCalificacion());
		Assert.assertTrue(viaje.isFinalizado());
	}

	
	@Test
	public void testSetValorBase(){
		Viaje.setValorBase(100);
		Assert.assertEquals(100, Viaje.getValorBase(), 0.000001);
	}

}
