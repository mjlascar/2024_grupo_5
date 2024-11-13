package testTDPI;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import excepciones.ChoferRepetidoException;
import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.SinVehiculoParaPedidoException;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloDatos.Cliente;
import modeloDatos.Moto;
import modeloDatos.Pedido;
import modeloDatos.Vehiculo;
import modeloDatos.Viaje;
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;

public class TestEmpresa_Esc1 {
	
	private Empresa empresa;
	
	@Before
	public void setUp() throws Exception {
	
		empresa = Empresa.getInstance();
		empresa.setChoferes(new HashMap<String,Chofer>());
		empresa.setClientes(new HashMap<String, Cliente>());
		empresa.setVehiculos(new HashMap<String, Vehiculo>());
		empresa.setChoferesDesocupados(new ArrayList<Chofer>());
		empresa.setPedidos(new HashMap<Cliente, Pedido>());
		empresa.setVehiculosDesocupados(new ArrayList<Vehiculo>());
		empresa.setViajesIniciados(new HashMap<Cliente, Viaje>());
		empresa.setViajesTerminados(new ArrayList<Viaje>());
	}

  	
	@Test
	public void testAgregarChofer() {
			
		Chofer chofer = new ChoferTemporario("34576832","Juan");
		try {
			empresa.agregarChofer(chofer);			
		}
		catch(Exception e) {
			Assert.fail("No debio haber lanzado excepcion");
		}
		Assert.assertEquals(chofer, empresa.getChoferes().get("34576832"));	

	}

	@Test
	public void testAgregarCliente() {	
		try {
			empresa.agregarCliente("miUsuario", "12345", "Joel");			
		}
		catch(Exception e) {
			Assert.fail("No debio haber lanzado excepcion");
		}
		Assert.assertNotEquals(null, empresa.getClientes().get("miUsuario"));	

	}
	
	@Test
	public void testAgregaPedido3() {
		Pedido pedido = new Pedido (new Cliente("miUsuario", "12345", "Joel"),  4, true, true, 10, Constantes.ZONA_STANDARD );
		try {
			empresa.agregarPedido(pedido);
			Assert.fail("Deberia haber lanzado excepcion");
			
		} catch (SinVehiculoParaPedidoException e) {
			
			Assert.fail("Deberia haber lanzado esta excepcion");
			
		} catch (ClienteNoExisteException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(mensaje, Mensajes.CLIENTE_NO_EXISTE.getValor());
			
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("Deberia haber lanzado esta excepcion");
			
		} catch (ClienteConPedidoPendienteException e) {
			
			Assert.fail("Deberia haber lanzado esta excepcion");
		}
	}
	
	@After
	public void tearDown() {
		empresa.getChoferes().clear();//LIMPIA todo
		empresa.getClientes().clear();
		
	}
}
