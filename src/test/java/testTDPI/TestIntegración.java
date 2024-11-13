package testTDPI;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.event.ActionEvent;

import javax.swing.JButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
 
import org.mockito.mock.*;


import controlador.Controlador;
import excepciones.ChoferNoDisponibleException;
import excepciones.ChoferRepetidoException;
import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.PasswordErroneaException;
import excepciones.PedidoInexistenteException;
import excepciones.SinVehiculoParaPedidoException;
import excepciones.UsuarioNoExisteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoNoDisponibleException;
import excepciones.VehiculoNoValidoException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Auto;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloDatos.Cliente;
import modeloDatos.Moto;
import modeloDatos.Pedido;
import modeloDatos.Vehiculo;
import modeloDatos.Viaje;
import modeloNegocio.Empresa;
import persistencia.EmpresaDTO;
import persistencia.IPersistencia;
import persistencia.PersistenciaBIN;
import persistencia.UtilPersistencia;
import util.Constantes;
import vista.Ventana;

public class TestIntegración {

	Controlador controlador;
	Empresa empresa;
	Ventana ventana;
	
	@Before
	public void setUp() {
	
		controlador = new Controlador();
		empresa = Empresa.getInstance();
		ventana = mock(Ventana.class);
		controlador.setVista(ventana);
		FalsoOptionPane op = new FalsoOptionPane();
		when(ventana.getOptionPane()).thenReturn(op);
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
	
	@Test 
	public void testGetFileName() {
		Assert.assertEquals("empresa.bin", controlador.getFileName());
	}
	
	@Test 
	public void testLeer() {
		
		try {
			empresa.agregarChofer(new ChoferTemporario("34576832", "Juan"));
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarCliente("ximena_destroyer", "12345", "Kevin");
		} catch (UsuarioYaExisteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarVehiculo(new Auto("FIA 449", 2, true));
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		EmpresaDTO empresaDTO;
		IPersistencia persistencia = new PersistenciaBIN();
		try{
			persistencia.abrirOutput("Empresa.bin");
			persistencia.escribir(UtilPersistencia.EmpresaDtoFromEmpresa());
			persistencia.cerrarOutput();
		}
		catch (Exception e){

			Assert.fail("no deberia lanzar excepcion !!");

		}
		
		controlador.leer();
		Assert.assertNotNull(empresa); //no deberia de dar null pq se supone que cargamos la empresa con datos?????
		Assert.assertEquals(empresa.getChoferes().get("34576832").getNombre(), "Juan");
		Assert.assertEquals(empresa.getClientes().get("ximena_destroyer").getNombreReal(), "Kevin");
		Assert.assertEquals(empresa.getVehiculos().get("FIA 449").getPatente(),"FIA 449" );
	}
	
	
	@Test
	public void testEscribir() {
		
		try {
			empresa.agregarChofer(new ChoferTemporario("34576832", "Juan"));
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarCliente("ximena_destroyer", "12345", "Kevin");
		} catch (UsuarioYaExisteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarVehiculo(new Auto("FIA 449", 2, true));
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controlador.escribir();
		
		EmpresaDTO empresaDTO;
		IPersistencia persistencia = new PersistenciaBIN();
		try {
			persistencia.abrirInput("Empresa.bin");
			empresaDTO = (EmpresaDTO)persistencia.leer();
			UtilPersistencia.empresaFromEmpresaDTO(empresaDTO);
			Assert.assertNotEquals(null, empresa); //no deberia de dar null pq se supone que cargamos la empresa con datos?????
			Assert.assertEquals(empresa.getChoferes().get("34576832").getNombre(), "Juan");
			Assert.assertEquals(empresa.getClientes().get("ximena_destroyer").getNombreReal(), "Kevin");
			Assert.assertEquals(empresa.getVehiculos().get("FIA 449").getPatente(),"FIA 449" );
		} catch(Exception e) {
			
			Assert.fail("no deberia lanzar excepcion !!");
		}
		
	}
	
	
	@Test
	public void testAPLogin() {
		
		when(ventana.getUsserName()).thenReturn("admin");
		when(ventana.getPassword()).thenReturn("admin");

		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.LOGIN));
		Assert.assertEquals("admin",empresa.getUsuarioLogeado().getNombreUsuario());
		Assert.assertEquals("admin", empresa.getUsuarioLogeado().getPass());
		
	}
	
	@Test
	public void testAPRegistro() {
		
		when(ventana.getRegUsserName()).thenReturn("miUsuario");
		when(ventana.getRegPassword()).thenReturn("12345");
		when(ventana.getRegConfirmPassword()).thenReturn("12345");
		when(ventana.getRegNombreReal()).thenReturn("Laura"); 

		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.REG_BUTTON_REGISTRAR));
		Cliente clienteReg = empresa.getClientes().get("miUsuario");
		Assert.assertNotNull(clienteReg);
		Assert.assertEquals("miUsuario",clienteReg.getNombreUsuario());
		Assert.assertEquals("Laura",clienteReg.getNombreReal());
		Assert.assertEquals("12345",clienteReg.getPass());
		
	}

	@Test
	public void testAPNuevoVehiculo() {
		
		when(ventana.getTipoVehiculo()).thenReturn(Constantes.MOTO);
		when(ventana.getPatente()).thenReturn("ABC123");
		when(ventana.getPlazas()).thenReturn(1);
		when(ventana.isVehiculoAptoMascota()).thenReturn(false);

		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.NUEVO_VEHICULO));
		Vehiculo moto = empresa.getVehiculos().get("ABC123");
		Assert.assertNotNull(moto);
		Assert.assertEquals("ABC123",moto.getPatente());
		Assert.assertEquals(1,moto.getCantidadPlazas());
		Assert.assertFalse(moto.isMascota());
		
	}
	
	@Test
	public void testAPNuevoChofer() {
		
		when(ventana.getTipoChofer()).thenReturn(Constantes.TEMPORARIO);
		when(ventana.getNombreChofer()).thenReturn("pepe");
		when(ventana.getDNIChofer()).thenReturn("12412512");
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.NUEVO_CHOFER));
		Chofer chofer = empresa.getChoferes().get("12412512");
		Assert.assertNotNull(chofer);
		Assert.assertEquals("12412512", chofer.getDni());
		Assert.assertEquals("pepe", chofer.getNombre());
	}

	@Test
	public void testAPNuevoPedido() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException | PasswordErroneaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo auto = new Auto("ABC123", 4, true);
		
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarVehiculo(auto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		when(ventana.getCantidadPax()).thenReturn(3);
		when(ventana.isPedidoConMascota()).thenReturn(false);
		when(ventana.isPedidoConBaul()).thenReturn(true);
		when(ventana.getCantKm()).thenReturn(5);
		when(ventana.getTipoZona()).thenReturn(Constantes.ZONA_STANDARD);
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.NUEVO_PEDIDO));
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = empresa.getPedidos().get(cliente);
		Assert.assertEquals(Constantes.ZONA_STANDARD, pedido.getZona()); //el contrato del metodo nuevoPedido no aclara que recupera la zona de la vista
		Assert.assertEquals(3, pedido.getCantidadPasajeros());
		Assert.assertEquals(5, pedido.getKm());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertTrue(pedido.isBaul());
		Assert.assertEquals(cliente.getNombreUsuario(), pedido.getCliente().getNombreUsuario());	
		
	}
	
	/*Se invoca al metodo crearViaje(...) de la clase Empresa con los parametros obtenidos del atributo vista:
	Pedido pedido = this.vista.getPedidoSeleccionado()
	Chofer chofer = this.vista.getChoferDisponibleSeleccionado()
	Vehiculo vehiculo = this.vista.getVehiculoDisponibleSeleccionado()
	Empresa.getInstance().crearViaje(pedido, chofer, vehiculo)*/
	
	@Test
	public void testAPNuevoViaje() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = new Pedido(cliente , 1, false, false, 10, Constantes.ZONA_STANDARD);
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo moto = new Moto("ASD555");
		
		try {
			empresa.agregarVehiculo(moto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		empresa.getVehiculosDesocupados().add(moto);
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException
				| ClienteConPedidoPendienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		
		when(ventana.getPedidoSeleccionado()).thenReturn(pedido);
		when(ventana.getChoferDisponibleSeleccionado()).thenReturn(chofer);
		when(ventana.getVehiculoDisponibleSeleccionado()).thenReturn(moto);
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.NUEVO_VIAJE));
		Viaje viaje = empresa.getViajesIniciados().get(cliente);
		Assert.assertNotNull(viaje);
		Assert.assertEquals(1, pedido.getCantidadPasajeros());
		Assert.assertEquals(10, pedido.getKm());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertFalse(pedido.isBaul());
		Assert.assertEquals(cliente.getNombreUsuario(), viaje.getPedido().getCliente().getNombreUsuario());
		Assert.assertEquals(chofer.getDni(), viaje.getChofer().getDni());
		Assert.assertEquals(moto.getPatente(), viaje.getVehiculo().getPatente());
		
	}

	@Test
	public void testAPCalificarYPagar() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException e) {
			
		} catch (PasswordErroneaException e) {
			
		}
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = new Pedido(cliente , 1, false, false, 10, Constantes.ZONA_STANDARD);
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo moto = new Moto("ASD555");			
		
		try {
			empresa.agregarVehiculo(moto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		empresa.getVehiculosDesocupados().add(moto);
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		empresa.getChoferesDesocupados().add(chofer);
		
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException
				| ClienteConPedidoPendienteException e) {
		}
	
		
		try {
			empresa.crearViaje(pedido, chofer, moto);
		} catch (PedidoInexistenteException | ChoferNoDisponibleException | VehiculoNoDisponibleException
				| VehiculoNoValidoException | ClienteConViajePendienteException e) {
		
		}
		
		Viaje viaje = empresa.getViajesIniciados().get(cliente);
		
		when(ventana.getCalificacion()).thenReturn(4);
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.CALIFICAR_PAGAR));
		Viaje viajefin = empresa.getViajesTerminados().get(0);
		
		Assert.assertTrue(viajefin.isFinalizado());
		Assert.assertEquals(4, viajefin.getCalificacion());
		
	}
	
	@Test
	public void testAPLogoutCliente() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException e) {
			
		} catch (PasswordErroneaException e) {
			
		}
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.CERRAR_SESION_CLIENTE));
		Assert.assertNull(empresa.getUsuarioLogeado());
	}
	
	@Test
	public void testAPLogoutAdmin() {
		
		try {
			empresa.login("admin", "admin");
		} catch (UsuarioNoExisteException | PasswordErroneaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controlador.actionPerformed(new ActionEvent(new JButton(), ActionEvent.ACTION_PERFORMED, Constantes.CERRAR_SESION_CLIENTE));
		Assert.assertNull(empresa.getUsuarioLogeado());
	}
	
	@Test
	public void testLogin() {
		
		when(ventana.getUsserName()).thenReturn("admin");
		when(ventana.getPassword()).thenReturn("admin");

		controlador.login();
		Assert.assertEquals("admin",empresa.getUsuarioLogeado().getNombreUsuario());
		Assert.assertEquals("admin", empresa.getUsuarioLogeado().getPass());
		
	}
	
	@Test
	public void testRegistro() {
		
		when(ventana.getRegUsserName()).thenReturn("miUsuario");
		when(ventana.getRegPassword()).thenReturn("12345");
		when(ventana.getRegConfirmPassword()).thenReturn("12345");
		when(ventana.getRegNombreReal()).thenReturn("Laura"); 

		controlador.registrar();
		Cliente clienteReg = empresa.getClientes().get("miUsuario");
		Assert.assertNotNull(clienteReg);
		Assert.assertEquals("miUsuario", clienteReg.getNombreUsuario());
		Assert.assertEquals("Laura", clienteReg.getNombreReal());
		Assert.assertEquals("12345",clienteReg.getPass());
		
	}

	@Test
	public void testNuevoVehiculo() {
		
		when(ventana.getTipoVehiculo()).thenReturn(Constantes.MOTO);
		when(ventana.getPatente()).thenReturn("ABC123");
		when(ventana.getPlazas()).thenReturn(1);
		when(ventana.isVehiculoAptoMascota()).thenReturn(false);

		controlador.nuevoVehiculo();
		Vehiculo moto = empresa.getVehiculos().get("ABC123");
		Assert.assertNotNull(moto);
		Assert.assertEquals("ABC123",moto.getPatente());
		Assert.assertEquals(1,moto.getCantidadPlazas());
		Assert.assertFalse(moto.isMascota());
		
	}
	
	@Test
	public void testNuevoChofer() {
		
		when(ventana.getTipoChofer()).thenReturn(Constantes.TEMPORARIO);
		when(ventana.getNombreChofer()).thenReturn("pepe");
		when(ventana.getDNIChofer()).thenReturn("12412512");
		
		controlador.nuevoChofer();
		Chofer chofer = empresa.getChoferes().get("12412512");
		Assert.assertNotNull(chofer);
		Assert.assertEquals("12412512", chofer.getDni());
		Assert.assertEquals("pepe", chofer.getNombre());
	}

	@Test
	public void testNuevoPedido() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException e) {
			
		} catch (PasswordErroneaException e) {
			
		}
		
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo auto = new Auto("ABC123", 4, true);
		
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			empresa.agregarVehiculo(auto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		when(ventana.getCantidadPax()).thenReturn(3);
		when(ventana.isPedidoConMascota()).thenReturn(false);
		when(ventana.isPedidoConBaul()).thenReturn(true);
		when(ventana.getCantKm()).thenReturn(3);
		when(ventana.getTipoZona()).thenReturn(Constantes.ZONA_STANDARD);
		
		controlador.nuevoPedido();
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = empresa.getPedidos().get(cliente);
		Assert.assertEquals(Constantes.ZONA_STANDARD, pedido.getZona()); //el contrato del metodo nuevoPedido no aclara que recupera la zona de la vista
		Assert.assertEquals(3, pedido.getCantidadPasajeros());
		Assert.assertEquals(3, pedido.getKm());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertTrue(pedido.isBaul());
		Assert.assertEquals(cliente.getNombreUsuario(), pedido.getCliente().getNombreUsuario());	
		
	}
	
	/*Se invoca al metodo crearViaje(...) de la clase Empresa con los parametros obtenidos del atributo vista:
	Pedido pedido = this.vista.getPedidoSeleccionado()
	Chofer chofer = this.vista.getChoferDisponibleSeleccionado()
	Vehiculo vehiculo = this.vista.getVehiculoDisponibleSeleccionado()
	Empresa.getInstance().crearViaje(pedido, chofer, vehiculo)*/
	
	@Test
	public void testNuevoViaje() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = new Pedido(cliente , 1, false, false, 10, Constantes.ZONA_STANDARD);
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo moto = new Moto("ASD555");
		
		try {
			empresa.agregarVehiculo(moto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		empresa.getVehiculosDesocupados().add(moto);
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClienteNoExisteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClienteConViajePendienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClienteConPedidoPendienteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		when(ventana.getPedidoSeleccionado()).thenReturn(pedido);
		when(ventana.getChoferDisponibleSeleccionado()).thenReturn(chofer);
		when(ventana.getVehiculoDisponibleSeleccionado()).thenReturn(moto);
		
		controlador.nuevoViaje();
		Viaje viaje = empresa.getViajesIniciados().get(cliente);
		Assert.assertNotNull(viaje);
		Assert.assertEquals(1, pedido.getCantidadPasajeros());
		Assert.assertEquals(10, pedido.getKm());
		Assert.assertFalse(pedido.isMascota());
		Assert.assertFalse(pedido.isBaul());
		Assert.assertEquals(cliente.getNombreUsuario(), viaje.getPedido().getCliente().getNombreUsuario());
		Assert.assertEquals(chofer.getDni(), viaje.getChofer().getDni());
		Assert.assertEquals(moto.getPatente(), viaje.getVehiculo().getPatente());
		
	}

	@Test
	public void testCalificarYPagar() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException e) {
			
		} catch (PasswordErroneaException e) {
			
		}
		
		Cliente cliente = empresa.getClientes().get("miUsuario");
		Pedido pedido = new Pedido(cliente , 1, false, false, 10, Constantes.ZONA_STANDARD);
		Chofer chofer = new ChoferTemporario("34576832", "Juan");
		Vehiculo moto = new Moto("ASD555");	
		
		try {
			empresa.agregarVehiculo(moto);
		} catch (VehiculoRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		empresa.getVehiculosDesocupados().add(moto);
		try {
			empresa.agregarChofer(chofer);
		} catch (ChoferRepetidoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		empresa.getChoferesDesocupados().add(chofer);
		
		try {
			empresa.agregarPedido(pedido);
		} catch (SinVehiculoParaPedidoException | ClienteNoExisteException | ClienteConViajePendienteException
				| ClienteConPedidoPendienteException e) {
		}		
		
		try {
			empresa.crearViaje(pedido, chofer, moto);
		} catch (PedidoInexistenteException | ChoferNoDisponibleException | VehiculoNoDisponibleException
				| VehiculoNoValidoException | ClienteConViajePendienteException e) {
		
		}
		
		Viaje viaje = empresa.getViajesIniciados().get(cliente);
		
		when(ventana.getCalificacion()).thenReturn(4);
		
		controlador.calificarPagar();
		Viaje viajefin = empresa.getViajesTerminados().get(0);
		
		Assert.assertTrue(viajefin.isFinalizado());
		Assert.assertEquals(4, viajefin.getCalificacion());
		
	}
	
	@Test
	public void testLogoutCliente() {
		
		try {
			empresa.agregarCliente("miUsuario", "12345", "Laura");
		} catch (UsuarioYaExisteException e) {
			
		}
		try {
			empresa.login("miUsuario", "12345");
		} catch (UsuarioNoExisteException e) {
			
		} catch (PasswordErroneaException e) {
			
		}
		
		controlador.logout();
		Assert.assertNull(empresa.getUsuarioLogeado());
	}
	
	@Test
	public void testLogoutAdmin() {
		
		try {
			empresa.login("admin", "admin");
		} catch (UsuarioNoExisteException | PasswordErroneaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		controlador.logout();
		Assert.assertNull(empresa.getUsuarioLogeado());
	}
}
