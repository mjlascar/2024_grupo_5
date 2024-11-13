package testTDPI;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import excepciones.ChoferNoDisponibleException;
import excepciones.ChoferRepetidoException;
import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.ClienteSinViajePendienteException;
import excepciones.PasswordErroneaException;
import excepciones.PedidoInexistenteException;
import excepciones.SinVehiculoParaPedidoException;
import excepciones.SinViajesException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoNoDisponibleException;
import excepciones.VehiculoNoValidoException;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloDatos.Cliente;
import modeloDatos.Moto;
import modeloDatos.Auto;
import modeloDatos.Combi;
import modeloDatos.Pedido;
import modeloDatos.Usuario;
import modeloDatos.Vehiculo;
import modeloDatos.Viaje;
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;

public class TestEmpresa_Esc2 {

	private Empresa empresa= Empresa.getInstance();

	@Before
	public void setUp() throws Exception {
		
		HashMap<String,Chofer> choferes = new HashMap<String,Chofer>();
		HashMap<String, Cliente> clientes = new HashMap<String, Cliente>();
		HashMap<Cliente, Pedido> pedidos = new HashMap<Cliente, Pedido>();
		HashMap<String, Vehiculo> vehiculos = new HashMap<String, Vehiculo>();
		HashMap<Cliente, Viaje> viajes = new HashMap<Cliente, Viaje>();
		ArrayList<Chofer> chofDesocupados = new ArrayList<Chofer>();
		ArrayList<Vehiculo> vhicDesocupados = new ArrayList<Vehiculo>();
		
		choferes.put("34576832", new ChoferTemporario("34576832", "Juan"));
		chofDesocupados.add(choferes.get("34576832"));
		
		
		Chofer chofViaje = new ChoferTemporario("32636774", "Pepe");
		choferes.put("32636774", chofViaje);
		
		empresa.setChoferes(choferes);
		
		clientes.put("miUsuario", new Cliente("miUsuario", "12345", "Joel"));
		clientes.put("clPedidoPend", new Cliente("clPedidoPend", "12345", "Guille"));
		clientes.put("clViajePend", new Cliente("clViajePend", "12345", "BradPitt"));
		
		empresa.setClientes(clientes);
		
		Auto autViaje = new Auto("ABC123", 4, true);
		vehiculos.put("ABC123", autViaje);
		Combi combi = new Combi("RED234", 8, false);
		vehiculos.put("RED234", combi);
		vehiculos.put("DEF456", new Auto("DEF456", 4, true));
		vehiculos.put("ASD555", new Moto("ASD555"));
		
		empresa.setVehiculos(vehiculos);
		
		vhicDesocupados.add(vehiculos.get("DEF456"));
		vhicDesocupados.add(vehiculos.get("ASD555"));
		
		//se crea un pedido que quede en pendiente 
		Pedido pedido = new Pedido(empresa.getClientes().get("clPedidoPend"), 4, true, false, 20, Constantes.ZONA_STANDARD);
		pedidos.put(empresa.getClientes().get("clPedidoPend"), pedido);
		pedidos.put(empresa.getClientes().get("clViajePend"), new Pedido(empresa.getClientes().get("clViajePend"), 4, true, false, 10, Constantes.ZONA_PELIGROSA));
		
		empresa.setPedidos(pedidos);
		
		viajes.put(empresa.getClientes().get("clViajePend"), new Viaje(empresa.getPedidos().get(empresa.getClientes().get("clViajePend")), chofViaje, autViaje));
		//asi ya se inicia el viaje, y se lo asocia con el cliente
		
		empresa.setViajesIniciados(viajes);
		
		empresa.setChoferesDesocupados(chofDesocupados);
		empresa.setVehiculosDesocupados(vhicDesocupados);
		empresa.setViajesTerminados(new ArrayList<Viaje>());
	}

	@Test
	public void testPagarYFinalizar1(){
		Cliente cliente = empresa.getClientes().get("clViajePend");
		empresa.setUsuarioLogeado(cliente);
		try { 
			empresa.pagarYFinalizarViaje(3);
			
		} catch (ClienteSinViajePendienteException e) {
			Assert.fail("No deberia lanzar excepcion");
		}
		Viaje viaje = empresa.getViajesTerminados().get(0);
		Assert.assertEquals(3, viaje.getCalificacion());
		Assert.assertTrue(viaje.isFinalizado());
	}

	@Test
	public void testPagarYFinalizar2(){
		Cliente cliente = empresa.getClientes().get("miUsuario");
		empresa.setUsuarioLogeado(null);
		try { 
			empresa.pagarYFinalizarViaje(3);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (ClienteSinViajePendienteException  e) {
			Assert.assertEquals(Mensajes.CLIENTE_SIN_VIAJE_PENDIENTE.getValor(), e.getMessage());
		}
		
	}

	@Test 
	public void testVehiculosOrdenadosPorPedido(){

		Pedido pedido = new Pedido(empresa.getClientes().get("miUsuario"), 4, true, false, 20, Constantes.ZONA_STANDARD);
		ArrayList<Vehiculo> ordenados = new ArrayList<Vehiculo>();
		ordenados.add(empresa.getVehiculos().get("ABC123"));
		ordenados.add(empresa.getVehiculos().get("DEF456"));
		ordenados.add(empresa.getVehiculos().get("ASD555"));
	}

	@Test
	public void testValidarPedido(){
		//creo un pedido para motos, la cual existe en la coleccion
		Pedido pedido = new Pedido(empresa.getClientes().get("miUsuario"), 1, false, false, 20, Constantes.ZONA_STANDARD);
		Assert.assertTrue(empresa.validarPedido(pedido));
	}

	@Test
	public void testAgregarChofer() {

		Chofer chofer = new ChoferTemporario("34576832", "Laura"); //Mismo DNI, distinto nombre

		try { 
			empresa.agregarChofer(chofer);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (ChoferRepetidoException e) {
			String mensaje = e.getMessage();
			Assert.assertEquals(Mensajes.CHOFER_YA_REGISTRADO.getValor(),mensaje); 
			Assert.assertEquals(e.getChoferExistente(), empresa.getChoferes().get("34576832"));
			Assert.assertEquals(e.getDniPrentendido(), "34576832");
		}


	}
	
	@Test
	public void testAgregarChofer2() {

		Chofer chofer = new ChoferTemporario("2358453", "Juan"); //Mismo nombre, distinto DNI

		try { 
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			Assert.fail("No deberia lanzar excepcion");
		}
		Assert.assertNotEquals(null, empresa.getChoferes().get("2358453"));
	}

	@Test
	public void testAgregarCliente() {
		
		try {
			empresa.agregarCliente("miUsuario", "taxis", "Franco");
			Assert.fail("Deberia lanzar excepcion");			
		}
		catch(UsuarioYaExisteException e) {
			String mensaje = e.getMessage();
			Assert.assertEquals(Mensajes.USUARIO_REPETIDO.getValor(),mensaje);
			Assert.assertEquals(e.getUsuarioPretendido(), "miUsuario");
		}

	}
	
	@Test
	public void testAgregarCliente2() {
		
		try {
			//intento agregar un usuario con distinto nombre de usuario y contraseña pero igual nombre
			empresa.agregarCliente("Usuario123", "taxis", "Joel");
						
		}
		catch(UsuarioYaExisteException e) {
			Assert.fail("No deberia lanzar excepcion");
					
		}
		Cliente cliente = empresa.getClientes().get("Usuario123");
		Assert.assertNotEquals(null, cliente);
	}

	@Test
	public void testAgregarCliente3() {
		
		try {
			//misma contraseña, distinto nombre de usuario y nombre real
			empresa.agregarCliente("Usuario123", "12345", "Franco");
			
		}
		catch(UsuarioYaExisteException e) { 
			Assert.fail("No deberia lanzar excepcion");
		}
		Cliente cliente = empresa.getClientes().get("Usuario123");
		Assert.assertNotEquals(null, cliente);//lo agrego correctamentE
	}
	
	
	@Test
	public void testAgregaPedido1() {
		Pedido pedido = new Pedido (empresa.getClientes().get("miUsuario"),  4, true, true, 10, Constantes.ZONA_STANDARD );
		try {
	
		//Se intenta agrgar un pedido valido que deberia de poder asignarse un chofer y auto de un cliente existente sin nada pendiente	
			empresa.agregarPedido(pedido);
			
		} catch (SinVehiculoParaPedidoException e) {
			
			Assert.fail("No deberia haber lanzado excepcion");
			
		} catch (ClienteNoExisteException e) {
			
			Assert.fail("No deberia haber lanzado excepcion");
		
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("No deberia haber lanzado excepcion");
			
		} catch (ClienteConPedidoPendienteException e) {
			
			Assert.fail("No deberia haber lanzado excepcion");
		}
		
		Assert.assertEquals(pedido, empresa.getPedidos().get(empresa.getClientes().get("miUsuario")));
	}
	
	//Se intenta agregar un viaje de un cliente que ya tiene un pedido pendiente
	@Test
	public void testAgregaPedido4() {
		Pedido pedido = new Pedido (empresa.getClientes().get("clPedidoPend"),  4, true, true, 10, Constantes.ZONA_STANDARD );
		try {
	
			empresa.agregarPedido(pedido);
			Assert.fail("Deberia lanzar excepcion");
			
		} catch (SinVehiculoParaPedidoException e) {
			Assert.fail("No deberia haber lanzado esta excepcion");
			
		} catch (ClienteNoExisteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
		
		} catch (ClienteConViajePendienteException e) {
			
			Assert.fail("No deberia haber lanzado esta excepcion");
			

		} catch (ClienteConPedidoPendienteException e) {
			
			String mensaje = e.getMessage();
			Assert.assertEquals(Mensajes.CLIENTE_CON_PEDIDO_PENDIENTE.getValor(), mensaje);
		}
		
	}
	
	//Se intenta agregar un pedido de un cliente que ya tiene un viaje pendiente
		@Test
		public void testAgregaPedido5() {
			Pedido pedido = new Pedido (empresa.getClientes().get("clViajePend"),  4, true, true, 10, Constantes.ZONA_STANDARD );
			
			try {
		
				empresa.agregarPedido(pedido);
				Assert.fail("Deberia lanzar excepcion");
				
			} catch (SinVehiculoParaPedidoException e) {
				Assert.fail("No deberia haber lanzado esta excepcion");
				
			} catch (ClienteNoExisteException e) {
				
				Assert.fail("No deberia haber lanzado esta excepcion");
			
			} catch (ClienteConViajePendienteException e) {
				
				String mensaje = e.getMessage();
				Assert.assertEquals(Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor(), mensaje);

			} catch (ClienteConPedidoPendienteException e) {
				
				Assert.fail("No deberia haber lanzado esta excepcion");
				
			}
			
		}
		
		
	//Caso en el que se quiere crear un viaje exitosamente
	@Test 
	public void testCreaViaje1() {
		
		 
		Cliente cliente = empresa.getClientes().get("clPedidoPend");
		Pedido pedido = empresa.getPedidos().get(cliente);
		Chofer chofer = empresa.getChoferesDesocupados().get(0);
		Vehiculo Vehiculo = empresa.getVehiculos().get("DEF456");
		
		if (chofer != null)
			try {
			
				empresa.crearViaje(pedido, chofer, Vehiculo);
			
			} catch (PedidoInexistenteException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ChoferNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoValidoException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ClienteConViajePendienteException e) {
				
				Assert.fail("No deberia de lanzar excepcion");
				
			}
			
		Assert.assertNotEquals(null, empresa.getViajesIniciados().get(cliente));
	}
	
	//Caso en el que no esta el chofer disponible
	@Test 
	public void testCreaViaje2() {
		
		Cliente cliente = empresa.getClientes().get("clPedidoPend");
		Pedido pedido = empresa.getPedidos().get(cliente);
		Chofer chofer = empresa.getChoferes().get("32636774");
		Vehiculo Vehiculo = empresa.getVehiculos().get("DEF456");
	
			try {
			
				empresa.crearViaje(pedido, chofer, Vehiculo);
				Assert.fail("Deberia de lanzar excepcion");
				
			} catch (PedidoInexistenteException e) {

				Assert.fail("No deberia de lanzar esta excepcion");
				
			} catch (ChoferNoDisponibleException e) {

				Assert.assertEquals(Mensajes.CHOFER_NO_DISPONIBLE.getValor(), e.getMessage());
				Assert.assertEquals(chofer, e.getChofer());
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.fail("No deberia de lanzar esta excepcion");
				
			} catch (VehiculoNoValidoException e) {

				Assert.fail("No deberia de lanzar esta excepcion");
				
			} catch (ClienteConViajePendienteException e) {
				
				
			}
	}
	
	//Caso en el que el cliente tiene un viaje pendiente
	@Test 
	public void testCreaViaje9() {
		
		Cliente cliente = empresa.getClientes().get("clViajePend");
		Pedido pedido = new Pedido(cliente, 4, false, false, 8, Constantes.ZONA_STANDARD );
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException
				| ClienteConPedidoPendienteException e) {
		}
		Chofer chofer = empresa.getChoferesDesocupados().get(0);
		Vehiculo Vehiculo = empresa.getVehiculos().get("DEF456");
		
		if (chofer != null)
			try {
			
				empresa.crearViaje(pedido, chofer, Vehiculo);
				Assert.fail("Deberia de lanzar excepcion");
				
			} catch (PedidoInexistenteException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ChoferNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoValidoException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ClienteConViajePendienteException e) {
				
				Assert.assertEquals(Mensajes.CLIENTE_CON_VIAJE_PENDIENTE.getValor(), e.getMessage());
				
			}
			
		
	}
	
   //el pedido no esta en el hashmap
	@Test 
	public void testCreaViaje7() {
		
		Pedido pedido = new Pedido(empresa.getClientes().get("miUsuario"), 5, true, false, 14, Constantes.ZONA_STANDARD);
		Chofer chofer = empresa.getChoferesDesocupados().get(0);
		Vehiculo Vehiculo = empresa.getVehiculos().get("DEF456");
		
		if (chofer != null)
			try {
			
				empresa.crearViaje(pedido, chofer, Vehiculo);
				Assert.fail("Deberia de lanzar excepcion");
				
			} catch (PedidoInexistenteException e) {

				Assert.assertEquals(Mensajes.PEDIDO_INEXISTENTE.getValor(), e.getMessage());
				Assert.assertEquals(pedido, e.getPedido());
				
			} catch (ChoferNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoValidoException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ClienteConViajePendienteException e) {
				
				Assert.fail("No deberia de lanzar excepcion");
				
			}
			
	}	

	@Test 
	public void testCreaViaje3() {
		
		Cliente cliente = empresa.getClientes().get("clPedidoPend");
		Pedido pedido = empresa.getPedidos().get(cliente);
		Chofer chofer = empresa.getChoferesDesocupados().get(0);
		Vehiculo combi = new Combi("ASD123", 7, false); ;
		
		if (chofer != null)
			try {
			
				empresa.crearViaje(pedido, chofer, combi);
				Assert.fail("Deberia de lanzar excepcion");
				
			} catch (PedidoInexistenteException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ChoferNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.assertEquals(Mensajes.VEHICULO_NO_DISPONIBLE.getValor(), e.getMessage());
				Assert.assertEquals(combi, e.getVehiculo());
				
			} catch (VehiculoNoValidoException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ClienteConViajePendienteException e) {
				
				Assert.fail("No deberia de lanzar excepcion");
				
			}
			
	}	


	@Test 
	public void testCreaViaje8() {
		
		
		Pedido pedido = new Pedido(empresa.getClientes().get("miUsuario"), 8, false, false, 14, Constantes.ZONA_STANDARD);
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException
				| ClienteConPedidoPendienteException e) {
			System.out.println(e.getMessage());
		}
		Chofer chofer = empresa.getChoferesDesocupados().get(0);
		Vehiculo Vehiculo = empresa.getVehiculos().get("DEF456");
		
		if (chofer != null)
			try {
			
				empresa.crearViaje(pedido, chofer, Vehiculo);
				Assert.fail("Deberia de lanzar excepcion");
				
			} catch (PedidoInexistenteException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (ChoferNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoDisponibleException e) {

				Assert.fail("No deberia de lanzar excepcion");
				
			} catch (VehiculoNoValidoException e) {

				Assert.assertEquals(Mensajes.VEHICULO_NO_VALIDO.getValor(), e.getMessage());
				Assert.assertEquals(pedido, e.getPedido());
				Assert.assertEquals(Vehiculo, e.getVehiculo());
				
			} catch (ClienteConViajePendienteException e) {
				
				Assert.fail("No deberia de lanzar excepcion");
				
			}
			
	}	

	@Test
	public void testCalificaChofer(){
		//tomo el viaje ya creado del sets up
		//se finaliza
		//setear e array de viajes finalizado testear la calificacion

		Viaje viaje = empresa.getViajesIniciados().get(empresa.getClientes().get("clViajePend"));
		viaje.finalizarViaje(4);
		empresa.getViajesIniciados().remove(empresa.getClientes().get("clViajePend"));
		empresa.getViajesTerminados().add(viaje);
		double calificacion = 0;

		try {
			calificacion = empresa.calificacionDeChofer(viaje.getChofer());
		}
		catch(SinViajesException e){

			Assert.fail("No deberia lanzar excepcion");
		}
		Assert.assertEquals(4.0, calificacion, 0.0001);
	}

@Test
	public void testCalificaChofer2(){

		Chofer chofer = empresa.getChoferes().get("34576832");
		double calificacion = 0;

		try {
			
			calificacion = empresa.calificacionDeChofer(chofer);
			Assert.fail("Deberia lanzar excepcion");
		}
		catch(SinViajesException e){
			Assert.assertEquals(Mensajes.CHOFER_SIN_VIAJES.getValor(), e.getMessage());
		}
	}

	@Test
	public void testSetLoging(){
		Cliente cliente = new Cliente("miUuario","12345","Joel");
		empresa.setUsuarioLogeado(cliente);
		Assert.assertEquals(cliente, empresa.getUsuarioLogeado());
	}
	
    @Test
	public void testLogin1(){
		Cliente cliente = new Cliente("miUsuario","12345","Joel");
		empresa.getClientes().put("miUsuario", cliente);
		Usuario userLog = null;

		try {
			userLog = empresa.login("miUsuario","12345");
		}
		catch(UsuarioNoExisteException e){

			Assert.fail("No deberia lanzar excepcion");
		}
		catch(PasswordErroneaException e){

			Assert.fail("No deberia lanzar excepcion");
		}
		Assert.assertEquals(cliente, userLog);
		
	}

    //intenta loguear un usuario no registrado
	@Test
	public void testLogin4(){
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Usuario userLog = null;

		try {
			userLog = empresa.login("miUsuarionuevo","12345");
			Assert.fail("Deberia de lanzar excepcion");
		}
		catch(UsuarioNoExisteException e){

           Assert.assertEquals(Mensajes.USUARIO_DESCONOCIDO.getValor(), e.getMessage());
           Assert.assertEquals("miUsuarionuevo", e.getUsuarioPretendido());
           			
		}
		catch(PasswordErroneaException e){

			Assert.fail("No deberia lanzar excepcion");
		}
	}

	public void testLogin5(){
		Cliente cliente = new Cliente("miUsuario","12345","Joel");
		empresa.getClientes().put("miUsuario", cliente);
		Usuario userLog = null;

		try {
			userLog = empresa.login("miUsuario","12345678");
		}
		catch(UsuarioNoExisteException e){
			
			Assert.fail("No deberia lanzar excepcion");
           			
		}
		catch(PasswordErroneaException e){

			Assert.assertEquals(Mensajes.PASS_ERRONEO.getValor(),e.getMessage());
			
		}
		Assert.assertEquals(cliente, userLog);
		
	}

	@Test
	public void testLogout(){
		Cliente cliente = new Cliente("miUsuario","12345","Joel");
		empresa.setUsuarioLogeado(cliente);
		empresa.logout();
		Assert.assertEquals(null, empresa.getUsuarioLogeado());
	}

	@After
	public void tearDown() {
		empresa.getChoferes().clear();//LIMPIA TODO Y CC CV
		empresa.getClientes().clear();
		empresa.getVehiculos().clear();
		empresa.getViajesIniciados().clear();
		empresa.getViajesTerminados().clear();
		empresa.getPedidos().clear();
		empresa.getChoferesDesocupados().clear();
		empresa.getVehiculosDesocupados().clear();
	}
}
