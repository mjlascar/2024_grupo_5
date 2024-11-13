package testTDPI;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.SinVehiculoParaPedidoException;
import modeloDatos.Auto;
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

public class TestEmpresa_Esc3 {

	private Empresa empresa= Empresa.getInstance();
	
	@Before
	public void setUp() throws Exception {
		
		//Escenario donde solo hay combis en la empresa disponibles
		
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
		
		vehiculos.put("ABC123", new Combi("ABC123", 6, false));
		vehiculos.put("LNG767", new Combi("LNG767", 6, false));
		vehiculos.put("AZW256", new Combi("AZW256", 6, false));
		
		empresa.setVehiculos(vehiculos);
		
		vhicDesocupados.add(vehiculos.get("ABC123"));
		vhicDesocupados.add(vehiculos.get("LNG767"));
		vhicDesocupados.add(vehiculos.get("AZW256"));
		
		empresa.setViajesIniciados(viajes);
		empresa.setChoferesDesocupados(chofDesocupados);
		empresa.setVehiculosDesocupados(vhicDesocupados);
		empresa.setViajesTerminados(new ArrayList<Viaje>());
	}
	
	
	@Test
	public void testAgregaPedido2_1() {

		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  1, false, false, 10, Constantes.ZONA_STANDARD );
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
	public void testAgregaPedido2_2() {

		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  7, true, true, 10, Constantes.ZONA_STANDARD );
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
	}
	
}
