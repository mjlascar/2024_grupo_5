package testTDPI;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import excepciones.ChoferNoDisponibleException;
import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.PedidoInexistenteException;
import excepciones.SinVehiculoParaPedidoException;
import excepciones.VehiculoNoDisponibleException;
import excepciones.VehiculoNoValidoException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloDatos.Cliente;
import modeloDatos.Combi;
import modeloDatos.Moto;
import modeloDatos.Pedido;
import modeloDatos.Vehiculo;
import modeloDatos.Viaje;
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;

public class TestEmpresa_Esc4 {

	private Empresa empresa= Empresa.getInstance();
	
	@Before
	public void setUp() throws Exception {
		
		//Escenario donde solo hay motos en la empresa disponibles
		
		

		HashMap<String,Chofer> choferes = new HashMap<String,Chofer>();
		HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
		HashMap<Cliente, Pedido> pedidos = new HashMap<Cliente, Pedido>();
		HashMap<String, Vehiculo> vehiculos = new HashMap<String, Vehiculo>();
		HashMap<Cliente, Viaje> viajes = new HashMap<Cliente, Viaje>();
		ArrayList<Chofer> chofDesocupados = new ArrayList<Chofer>();
		ArrayList<Vehiculo> vhicDesocupados = new ArrayList<Vehiculo>();
		
		clientes.put("miUsuario", new Cliente("miUsuario", "12345", "Joel"));
		
		empresa.setClientes(clientes);
		
		choferes.put("34576832", new ChoferTemporario("34576832", "Juan"));
		chofDesocupados.add(choferes.get("34576832"));
		
		empresa.setChoferes(choferes);
		
		vehiculos.put("ABC123", new Moto("ABC123"));
		vehiculos.put("LNG767", new Moto("LNG767"));
		
		empresa.setVehiculos(vehiculos);
		
		vhicDesocupados.add(vehiculos.get("ABC123"));
		vhicDesocupados.add(vehiculos.get("LNG767"));
		
		empresa.setViajesIniciados(viajes);
		empresa.setChoferesDesocupados(chofDesocupados);
		empresa.setVehiculosDesocupados(vhicDesocupados);
		empresa.setViajesTerminados(new ArrayList<Viaje>());
	}
	
	@Test
	public void testAgregaVehiculo1() {
		Moto moto = new Moto("AZW256");
		
		try {
			empresa.agregarVehiculo(moto);
			
		} catch (VehiculoRepetidoException e) {
			Assert.fail("No deberia lanzar excepcion");
		}
		Assert.assertNotEquals(null, empresa.getVehiculos().get("AZW256"));
	}
	
	@Test
	public void testAgregaVehiculo2() {
		Moto moto = new Moto("LNG767");
		
		try {
			empresa.agregarVehiculo(moto);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (VehiculoRepetidoException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(mensaje, Mensajes.VEHICULO_YA_REGISTRADO.getValor());
			Assert.assertEquals("LNG767", e.getPatentePrentendida());
			Assert.assertEquals(empresa.getVehiculos().get("LNG767"), e.getVehiculoExistente());
		}
	
	}
	
	
	@Test
	public void testAgregaPedido2_3() {

		//pedido para una persona pero con baul, se requiere un auto
		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  1, false, true, 10, Constantes.ZONA_STANDARD );
		try {
	
			empresa.agregarPedido(pedido);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (SinVehiculoParaPedidoException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(mensaje, Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
			Assert.assertEquals(pedido, e.getPedido());
			
		} catch (ClienteNoExisteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");

		} catch (ClienteConPedidoPendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		}
		
	}
	
	
	@Test
	public void testAgregaPedido2_4() {
		
		//pedido para una persona pero con mascota, se requiere un auto
		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  1, true, false, 10, Constantes.ZONA_STANDARD );
		try {
	
			empresa.agregarPedido(pedido);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (SinVehiculoParaPedidoException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(mensaje, Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
			Assert.assertEquals(pedido, e.getPedido());
			
		} catch (ClienteNoExisteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");

		} catch (ClienteConPedidoPendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		}
		
	}
	
	@Test
	public void testAgregaPedido2_5() {

		//pedido para una combi que no hay
		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  6, false, true, 10, Constantes.ZONA_STANDARD );
		try {
	
			empresa.agregarPedido(pedido);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (SinVehiculoParaPedidoException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(mensaje, Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor());
			Assert.assertEquals(pedido, e.getPedido());
			
		} catch (ClienteNoExisteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");

		} catch (ClienteConPedidoPendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		}
		
	}
	

	@After
	public void tearDown() {
		empresa.getChoferes().clear();
		empresa.getClientes().clear();
		empresa.getVehiculos().clear();
		empresa.getViajesIniciados().clear();
		empresa.getViajesTerminados().clear();
		empresa.getPedidos().clear();
	}
	
}