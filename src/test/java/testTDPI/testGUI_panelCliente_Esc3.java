package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Auto;
import modeloNegocio.Empresa;
import util.Constantes;
import vista.Ventana;

public class testGUI_panelCliente_Esc3 {
	private Robot robot;
	private Controlador controlador;
	private Empresa empresa= Empresa.getInstance();
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() { //ESCENARIO SITUACION 2
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			empresa.agregarCliente("kevin123","12345", "Kevin");
			
			Auto autViaje = new Auto("ABC123", 4, true); //auto  con 4 plazas y mascota
			empresa.agregarVehiculo(autViaje);
			empresa.getVehiculosDesocupados().add(empresa.getVehiculos().get("ABC123"));
			
			JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
			JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
			JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
			TestUtils.clickComponent(nombre, robot);
			TestUtils.tipeaTexto("kevin123", robot);
			TestUtils.clickComponent(contra, robot);
			TestUtils.tipeaTexto("12345", robot);
			TestUtils.clickComponent(login, robot);
			
			JButton hacerPedido = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
			JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
			TestUtils.clickComponent(cantPax, robot);
			TestUtils.tipeaTexto("3", robot);
			JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
			TestUtils.clickComponent(cantKM, robot);
			TestUtils.tipeaTexto("10", robot);
			TestUtils.clickComponent(hacerPedido, robot);
		} catch (AWTException e) {
			
		}
	      catch (UsuarioYaExisteException e) {
	    	  
		} catch (VehiculoRepetidoException e) {
			
		}
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
	public void testPedidoOViajeActConPedido() {
	    JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
	    if (pedViajAct.getText().trim().contains("Viaje"))
	    	Assert.assertTrue("El TextArea pedViajAct no deberia tener un Viaje", false);
	    else
	    	Assert.assertTrue("El TextArea pedViajAct deberia tener un Pedido", pedViajAct.getText().trim().contains("Pedido") );
	}
	
	@Test
	public void testCalificacionDeshabilitado() {
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CALIFICACION_DE_VIAJE);
		Assert.assertFalse("El TextField calificacion deberia estar deshabilitado", (calificacion.isEnabled() && calificacion.isEditable() && calificacion.isVisible()) );
	}

	@Test 
	public void testValorViajeVacio() {
    	JTextField valorViaje = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.VALOR_VIAJE);
		Assert.assertTrue("El TextField valorViaje deberia estar vacio", valorViaje.getText().trim().isEmpty() );
	}

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

	
	
	
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getClientes().clear();
		Empresa.getInstance().getPedidos().clear();
		Empresa.getInstance().getVehiculos().clear();
		Empresa.getInstance().getVehiculosDesocupados().clear();
		Empresa.getInstance().getViajesIniciados().clear();
		Empresa.getInstance().getViajesTerminados().clear();
		}
	}
