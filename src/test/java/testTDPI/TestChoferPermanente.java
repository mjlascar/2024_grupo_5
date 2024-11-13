package testTDPI;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;

import modeloDatos.Chofer;
import modeloDatos.ChoferPermanente;
import modeloDatos.ChoferTemporario;

public class TestChoferPermanente {

	private ChoferPermanente chofer;
	private double sueldobasico = 500000.0;
	
	@Before
	public void setUp() throws Exception {
		
		chofer = new ChoferPermanente("12412512", "pepe", 2020, 2);
		Chofer.setSueldoBasico(sueldobasico);
		
	}

	@Test
	public void testGetCantidadHijos() {
		Assert.assertEquals(2, chofer.getCantidadHijos());
	}

	@Test
	public void testSueldoBruto() {
		Assert.assertEquals(670000, chofer.getSueldoBruto(), 0.000001);
	}

	@Test
	public void testSueldoNeto() {
		Assert.assertEquals(576200, chofer.getSueldoNeto(), 0.000001);  //el assert double esta deprecated buu, se tiene que poner un delta
	}
	
	@Test
	public void testAntiguedad() {
	
		Assert.assertEquals(4, chofer.getAntiguedad());
	}
	
	@Test
	public void testGetAnioIngreso() {
	
		Assert.assertEquals(2020, chofer.getAnioIngreso());
	}
	
	@Test
	public void testSetCantidadHijos() {
		
		chofer.setCantidadHijos(0);
		Assert.assertEquals(0, chofer.getCantidadHijos());
	}
	
	@Test
	public void testGetDni() {
	
		Assert.assertEquals("12412512", chofer.getDni());
	}
	
	@Test
	public void testGetNombre() {
	
		Assert.assertEquals("pepe", chofer.getNombre());
	}
	
	@Test
	public void testSetSueldoBasico() {
		
		Chofer.setSueldoBasico(100);
		Assert.assertEquals(100, Chofer.getSueldoBasico(), 0.000001);
	}
	
	

}
