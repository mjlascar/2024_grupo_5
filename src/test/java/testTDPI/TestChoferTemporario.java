package testTDPI;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.junit.Assert;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;

public class TestChoferTemporario {

	private ChoferTemporario chofer;
	private double sueldobasico = 500000.0;
	
	@Before
	public void setUp() throws Exception {
		chofer = new ChoferTemporario("12412512", "pepe");
	}

	@Test
	public void testSueldoBruto() {
		Assert.assertEquals(sueldobasico, chofer.getSueldoBruto(), 0.000001);
	}

	
}
