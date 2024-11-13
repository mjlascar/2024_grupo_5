package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controlador.Controlador;
import excepciones.UsuarioYaExisteException;
import vista.Ventana;
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;


public class TestGUI_panelLogin {
	private Robot robot;
	private Controlador controlador;
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() {
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			Empresa.getInstance().agregarCliente("kevin123","12345", "Kevin");
		} catch (AWTException e) {
			
		}
	      catch (UsuarioYaExisteException e) {
	    	  
		}
		
	}
	
	
	//TEST PANEL LOGIN INICIAL
	@Test
	public void testSoloNombre() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Ximena", robot);
		Assert.assertFalse("El boton login deberia estar deshabilitado", login.isEnabled());
	}
	@Test
	public void testSoloContra() {
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("12345", robot);
		Assert.assertFalse("El boton login deberia estar deshabilitado", login.isEnabled());
	}
	@Test
	public void testContraNombreCorta() {
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("1", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("X", robot);
		Assert.assertFalse("El boton login deberia estar deshabilitado", login.isEnabled());
	}
	@Test
	public void testNombreContraOK() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Ximena", robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("12345", robot);
		Assert.assertTrue("El boton login deberia estar habilitado", login.isEnabled());
	}
	@Test
	public void testRegistrar() {
		JButton registrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REGISTRAR);
		Assert.assertTrue("El boton registrar deberia estar habilitado", registrar.isEnabled());
	}
	@Test
	public void testClienteCorrecto() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("kevin123", robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(login, robot);
		JPanel cliente = (JPanel) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PANEL_CLIENTE);
		Assert.assertTrue("Deberia haber pasado al panel de cliente", cliente.isVisible());
	}	
	@Test
	public void testContraIncorrecta() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("kevin123", robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("contraincorrecta", robot);
		TestUtils.clickComponent(login, robot);
		Assert.assertEquals("Mensaje incorrecto, deberia decir: " , Mensajes.PASS_ERRONEO.getValor(), op.getMensaje());
	}
	public void testNombreInexistente() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Ximena", robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(login, robot);
		Assert.assertEquals("Mensaje incorrecto, deberia decir: " , Mensajes.USUARIO_DESCONOCIDO.getValor(), op.getMensaje());
	}
	
	
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);	
	}
}