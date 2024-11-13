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
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;
import vista.Ventana;

public class testGUI_panelCliente_Esc1 {
	private Robot robot;
	private Controlador controlador;
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() { //ESCENARIO, SITUACION 1, ARRAY VEHICULOS VACIO
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			Empresa.getInstance().agregarCliente("kevin123","12345", "Kevin");
			JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
			JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
			JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
			TestUtils.clickComponent(nombre, robot);
			TestUtils.tipeaTexto("kevin123", robot);
			TestUtils.clickComponent(contra, robot);
			TestUtils.tipeaTexto("12345", robot);
			TestUtils.clickComponent(login, robot);
		} catch (AWTException e) {
			
		}
	      catch (UsuarioYaExisteException e) {
	    	  
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
	public void testPedidoOViajeActVacio() {
	    JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
		Assert.assertTrue("El TextArea pedViajAct deberia estar vacio", pedViajAct.getText().trim().isEmpty() );
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
	
	
	
	
	@Test
	public void testBotonSoloCantPaxCorrecta() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("4", robot);
		Assert.assertFalse("El boton aceptar deberia estar deshabilitado", aceptar.isEnabled());
	}
	
	@Test
	public void testBotonSoloCantKMCorrecta() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM  );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		Assert.assertFalse("El boton aceptar deberia estar deshabilitado", aceptar.isEnabled());
	}
	
	@Test
	public void testBotonCantPaxIncorrecta() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("14", robot);
		
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		
		Assert.assertFalse("El boton aceptar deberia estar deshabilitado", aceptar.isEnabled());
	}
	
	@Test
	public void testBotonCantKMIncorrecta() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("4", robot);
		
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("-4", robot);
		
		Assert.assertFalse("El boton aceptar deberia estar deshabilitado", aceptar.isEnabled());
	}
	
	@Test
	public void testBotonHabilitadoPaxYKMCorrectas() {
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("4", robot);
		
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		
		Assert.assertTrue("El boton aceptar deberia estar habilitado", aceptar.isEnabled());
	}
	
	@Test
	public void testBotonPaxYKMCorrectasSinAutoApto() {
		JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("4", robot);
		
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		
		TestUtils.clickComponent(aceptar, robot);
		Assert.assertEquals("Mensaje incorrecto " , Mensajes.SIN_VEHICULO_PARA_PEDIDO.getValor(), op.getMensaje());
	}
	
	
	
	
	
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getClientes().clear();
		Empresa.getInstance().getVehiculos().clear();
		Empresa.getInstance().getVehiculosDesocupados().clear();
		Empresa.getInstance().getViajesIniciados().clear();
		Empresa.getInstance().getViajesTerminados().clear();
		}
	}
