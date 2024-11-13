package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.InputEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controlador.Controlador;
import excepciones.ChoferNoDisponibleException;
import excepciones.ChoferRepetidoException;
import excepciones.ClienteConViajePendienteException;
import excepciones.PedidoInexistenteException;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoNoDisponibleException;
import excepciones.VehiculoNoValidoException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Auto;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloDatos.Pedido;
import modeloNegocio.Empresa;
import util.Constantes;
import vista.Ventana;

public class testGUI_panelCliente_Esc5 {
	private Robot robot;
	private Controlador controlador;
	private Empresa empresa= Empresa.getInstance();
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() { //ESCENARIO SITUACION 3, LUEGO DE CLICKEAR CALIFICAR_PAGAR
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			empresa.agregarCliente("kevin123","12345", "Kevin");
			
			Auto autViaje = new Auto("ABC123", 4, true); //auto  con 4 plazas y mascota
			empresa.agregarVehiculo(autViaje);
			//empresa.getVehiculosDesocupados().add(empresa.getVehiculos().get("ABC123"));
			Auto autViaje2 = new Auto("ABC1232", 4, true); //auto  con 4 plazas y mascota
			empresa.agregarVehiculo(autViaje2);
			
			this.loguea("kevin123","12345");
			
			this.pide();
			
			this.desloguea();
			
			Chofer chofViaje = new ChoferTemporario("32636774", "Pepe");
			empresa.agregarChofer(chofViaje);
			//empresa.getChoferesDesocupados().add(chofViaje);
			Chofer chofViaje2 = new ChoferTemporario("32646774", "2Pepe");
			empresa.agregarChofer(chofViaje2);
			
			this.loguea("admin","admin");
			this.adminConfirmaYDesloguea();
			
			this.loguea("kevin123","12345");
			robot.delay(3000);
			empresa.agregarPedido(new Pedido(empresa.getClientes().get("kevin123"), 3, false, false, 5, Constantes.ZONA_PELIGROSA));
			robot.delay(3000);
			
			this.paga();
			
			
		} catch (Exception e) {
			Assert.fail("No debio haber lanzado excepcion");
		}
	}

	private void loguea(String user, String pass) {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto(user, robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto(pass, robot);
		TestUtils.clickComponent(login, robot);
	}
	
	private void pide() {
		JButton hacerPedido = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("3", robot);
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		TestUtils.clickComponent(hacerPedido, robot);
	}

	private void desloguea() {
		JButton cerrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CERRAR_SESION_CLIENTE);
		TestUtils.clickComponent(cerrar, robot);
	}

	private void adminConfirmaYDesloguea() {
		JList<?> listaPedidos = (JList<?>) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		listaPedidos.setSelectedIndex(0);
		TestUtils.clickComponent(listaPedidos, robot);
		
		JList<?> listaChoferesLib = (JList<?>) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaChoferesLib.setSelectedIndex(0);
		TestUtils.clickComponent(listaChoferesLib, robot);
		
		JList<?> listaVehiculosDisp = (JList<?>) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		listaVehiculosDisp.setSelectedIndex(0);
		TestUtils.clickComponent(listaVehiculosDisp, robot);
		
		JButton nuevoViaje = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		TestUtils.clickComponent(nuevoViaje, robot);
		
		JButton cerrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CERRAR_SESION_ADMIN);
		TestUtils.clickComponent(cerrar, robot);
	}

	private void paga() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		TestUtils.clickComponent(calificacion, robot);
		TestUtils.tipeaTexto("4", robot);
		JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		TestUtils.clickComponent(pagar, robot);
	}
	
	@Test
	public void testPedidoOViajeActVacio() {
	    JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
	
	    Assert.assertTrue("El TextArea pedViajAct deberia estar vacio", pedViajAct.getText().trim().isEmpty() );
	}
	
	@Test
	public void testListaViajesNoVacia() {
		JList<?> listaViajes = (JList<?>) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VIAJES_CLIENTE);
	
	    Assert.assertFalse("El TextArea listaViajes deberia tener el pedido", listaViajes.getComponentCount()==0 );
	}
	
	@Test
	public void testCalificacionVacio() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		Assert.assertTrue("El TextField valorViaje deberia estar vacio", calificacion.getText().trim().isEmpty() );
	}
	
	
	//ahora testeamos que se pueda generar un nuevo pedido, pero que no este habilitado el boton de antemano
	
	@Test
	public void testBotonPagarDeshabilitado() {
    	JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		Assert.assertFalse("El JButton pagar no esta deshabilitado", pagar.isEnabled());
	}

	@Test
	public void testBotonAceptarDeshabilitado() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO);
		Assert.assertFalse("El JButton aceptar no esta deshabilitado", aceptar.isEnabled());
	}
	

	@Test
	public void testCantPaxHabilitado() {
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX);
		Assert.assertTrue("El TextField CantPax deberia estar habilitado", (cantPax.isEnabled() && cantPax.isEditable() && cantPax.isVisible()) );
	}

	@Test
	public void testCantKMHabilitado() {
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM);
		Assert.assertTrue("El TextField CantKM deberia estar habilitado", (cantKM.isEnabled() && cantKM.isEditable() && cantKM.isVisible()) );
	}

	@Test
	public void testRadioBtnStandardHabilitado() {
		JRadioButton radioBtnStandard = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_STANDARD);
		Assert.assertTrue("El JButton standard no esta habilitado", radioBtnStandard.isEnabled());
	}

	@Test
	public void testRadioBtnSinAsfaltarHabilitado() {
		JRadioButton radioBtnSinAsfaltar = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_SIN_ASFALTAR);
		Assert.assertTrue("El JButton sin asfaltar no esta habilitado", radioBtnSinAsfaltar.isEnabled());
	}

	@Test
	public void testRadioBtnPeligrosaHabilitado() {
		JRadioButton radioBtnPeligrosa = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_PELIGROSA);
		Assert.assertTrue("El JButton peligrosa no esta habilitado", radioBtnPeligrosa.isEnabled());
	}

	@Test
	public void testCheckBoxMascotaHabilitado() {
		JCheckBox checkMascota = (JCheckBox) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CHECK_MASCOTA);
		Assert.assertTrue("La check box mascota no esta habilitada", checkMascota.isEnabled());
	}

	@Test
	public void testCheckBoxBaulHabilitado() {
		JCheckBox checkBaul = (JCheckBox) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CHECK_BAUL);
		Assert.assertTrue("La check box baul no esta habilitada", checkBaul.isEnabled());
	}
	
	
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getClientes().clear();
		Empresa.getInstance().getChoferes().clear();
		Empresa.getInstance().getChoferesDesocupados().clear();
		Empresa.getInstance().getVehiculos().clear();
		Empresa.getInstance().getVehiculosDesocupados().clear();
		Empresa.getInstance().getPedidos().clear();
		Empresa.getInstance().getViajesIniciados().clear();
		Empresa.getInstance().getViajesTerminados().clear();
		}
	}
