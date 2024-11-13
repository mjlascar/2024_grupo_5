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

public class testGUI_panelCliente_Esc4 {
	private Robot robot;
	private Controlador controlador;
	private Empresa empresa= Empresa.getInstance();
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() { //ESCENARIO SITUACION 3
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			empresa.agregarCliente("kevin123","12345", "Kevin");
			
			Auto autViaje = new Auto("ABC123", 4, true); //auto  con 4 plazas y mascota
			empresa.agregarVehiculo(autViaje);
			//empresa.getVehiculosDesocupados().add(empresa.getVehiculos().get("ABC123"));
			
			
			this.loguea("kevin123","12345");
			
			this.pide();
			
			this.desloguea();
			
			Chofer chofViaje = new ChoferTemporario("32636774", "Pepe");
			empresa.agregarChofer(chofViaje);
			//empresa.getChoferesDesocupados().add(chofViaje);
			
			this.loguea("admin","admin");
			this.adminConfirmaYDesloguea();
			
			this.loguea("kevin123","12345");
			
			
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
	
	@Test
	public void testMuestraNombreReal() {
		
		JPanel panel = (JPanel) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PANEL_CLIENTE);
		String title = ((TitledBorder) panel.getBorder()).getTitle();
		Assert.assertTrue("El panel deberia estar titulado con el nombre real: ", "Kevin".equals(title));
	}
	
	@Test
	public void testCerrarSesion() {
		JButton cerrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CERRAR_SESION_CLIENTE);
		TestUtils.clickComponent(cerrar, robot);
		JPanel panel = (JPanel) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PANEL_LOGIN);
		Assert.assertTrue(panel!=null);
	}
	
	@Test 
	public void testPedidoOViajeActConViaje() {
	    JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
	    
	    Assert.assertTrue("El TextArea pedViajAct deberia tener un Viaje", pedViajAct.getText().trim().contains("Viaje") );
	}
	
	@Test
	public void testCalificacionHabilitado() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		Assert.assertTrue("El TextField calificacion deberia estar habilitado", (calificacion.isEnabled() && calificacion.isEditable() && calificacion.isVisible()) );
	}

	@Test 
	public void testValorViajeNoVacio() {
    	JTextField valorViaje = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.VALOR_VIAJE);
		Assert.assertFalse("El TextField valorViaje no deberia estar vacio", valorViaje.getText().trim().isEmpty() );
	}

	@Test
	public void testBotonAceptarDeshabilitado() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO);
		Assert.assertFalse("El JButton aceptar no esta deshabilitado", aceptar.isEnabled());
	}

	@Test
	public void testCantPaxDeshabilitado() {
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX);
		Assert.assertFalse("El TextField CantPax no deberia estar habilitado", (cantPax.isEnabled() && cantPax.isEditable() && cantPax.isVisible()) );
	}

	@Test
	public void testCantKMDeshabilitado() {
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM);
		Assert.assertFalse("El TextField CantKM no deberia estar habilitado", (cantKM.isEnabled() && cantKM.isEditable() && cantKM.isVisible()) );
	}

	@Test
	public void testRadioBtnStandardDeshabilitado() {
		JRadioButton radioBtnStandard = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_STANDARD);
		Assert.assertFalse("El JButton standard esta habilitado", radioBtnStandard.isEnabled());
	}

	@Test
	public void testRadioBtnSinAsfaltarDeshabilitado() {
		JRadioButton radioBtnSinAsfaltar = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_SIN_ASFALTAR);
		Assert.assertFalse("El JButton sin asfaltar esta habilitado", radioBtnSinAsfaltar.isEnabled());
	}

	@Test
	public void testRadioBtnPeligrosaDeshabilitado() {
		JRadioButton radioBtnPeligrosa = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.ZONA_PELIGROSA);
		Assert.assertFalse("El JButton peligrosa esta habilitado", radioBtnPeligrosa.isEnabled());
	}

	@Test
	public void testCheckBoxMascotaDeshabilitado() {
		JCheckBox checkMascota = (JCheckBox) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CHECK_MASCOTA);
		Assert.assertFalse("La check box mascota esta habilitada", checkMascota.isEnabled());
	}

	@Test
	public void testCheckBoxBaulDeshabilitado() {
		JCheckBox checkBaul = (JCheckBox) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CHECK_BAUL);
		Assert.assertFalse("La check box baul esta habilitada", checkBaul.isEnabled());
	}
	
	@Test
	public void testBotonPagarSinCalificacion() {
    	JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		Assert.assertFalse("El JButton pagar no esta deshabilitado", pagar.isEnabled());
	}
	
	@Test
	public void testBotonPagarConCalificacionMenorA0() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		TestUtils.clickComponent(calificacion, robot);
		TestUtils.tipeaTexto("-2", robot);
		
		JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		Assert.assertFalse("El JButton pagar no esta deshabilitado", pagar.isEnabled());
	}

	@Test
	public void testBotonPagarConCalificacionMayorA5() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		TestUtils.clickComponent(calificacion, robot);
		TestUtils.tipeaTexto("6", robot);
		
		JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		Assert.assertFalse("El JButton pagar no esta deshabilitado", pagar.isEnabled());
	}
	
	@Test
	public void testBotonPagarConCalificacionCorrecta() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		TestUtils.clickComponent(calificacion, robot);
		TestUtils.tipeaTexto("4", robot);
		robot.delay(100);
		JButton pagar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICAR_PAGAR);
		Assert.assertTrue("El JButton pagar no esta habilitado", pagar.isEnabled());
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
