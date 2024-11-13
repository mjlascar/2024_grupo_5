package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;

import javax.swing.JButton;
import javax.swing.JTextField;

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

public class TestGUI_panelRegistrar {
	private Robot robot;
	private Controlador controlador;
	FalsoOptionPane op = new FalsoOptionPane();
	//TEST PANEL REGISTRAR
	
	@Before
	public void setup() {
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			Empresa.getInstance().agregarCliente("kevin123","12345", "Kevin");
			JButton registrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REGISTRAR);
			TestUtils.clickComponent(registrar, robot);
		} catch (AWTException e) {
			
		}
	      catch (UsuarioYaExisteException e) {
	    	  
		}
		
	}


	@Test
	public void testRegistrarCancelar() {
		JButton cancelar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_CANCELAR);
		Assert.assertTrue("El boton cancelar deberia estar habilitado", cancelar.isEnabled());
	}
	@Test
	public void testRegistrarSoloNombre() {
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Romina", robot);
		Assert.assertFalse("El boton registrar no deberia deberia estar habilitado", registrar2.isEnabled());
	}
	@Test
	public void testRegistrarSoloPass() {
		JTextField pass = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(pass, robot);
		TestUtils.tipeaTexto("1234", robot);
		Assert.assertFalse("El boton registrar no deberia deberia estar habilitado", registrar2.isEnabled());
	}
	@Test
	public void testRegistrarSoloPassRepe() {
		JTextField repe = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(repe, robot);
		TestUtils.tipeaTexto("1234", robot);
		Assert.assertFalse("El boton registrar no deberia deberia estar habilitado", registrar2.isEnabled());
	}
	@Test
	public void testRegistrarSoloNombreReal() {
		JTextField nomReal = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(nomReal, robot);
		TestUtils.tipeaTexto("romina", robot);
		Assert.assertFalse("El boton registrar no deberia deberia estar habilitado", registrar2.isEnabled());
	}
	@Test
	public void testRegistrarCancelarInhabilitado() {
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		Assert.assertFalse("El boton registrar no deberia deberia estar habilitado", registrar2.isEnabled());

	}
	@Test
	public void testRegistrarUsuarioOK() {
		JTextField repe = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
		JTextField pass = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
		JTextField nomReal = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("ximena123", robot);
		TestUtils.clickComponent(pass, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(repe, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(nomReal, robot);
		TestUtils.tipeaTexto("Ximena", robot);
		TestUtils.clickComponent(registrar2, robot);
		JButton registrar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REGISTRAR);
		Assert.assertTrue("El boton registrar deberia deberia estar habilitado, porque ya volvi al login inicial", registrar.isEnabled());


	}
	@Test
	public void testRegistrarUsuarioRepetido() {
		JTextField repe = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
		JTextField pass = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
		JTextField nomReal = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("kevin123", robot);
		TestUtils.clickComponent(pass, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(repe, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(nomReal, robot);
		TestUtils.tipeaTexto("Kevin", robot);
		TestUtils.clickComponent(registrar2, robot);
		Assert.assertEquals("Mensaje incorrecto, deberia decir: " , Mensajes.USUARIO_REPETIDO.getValor(), op.getMensaje());
	}
	@Test
	public void testRegistrarPassNoCoincide() {
		JTextField repe = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_CONFIRM_PASSWORD);
		JTextField pass = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_PASSWORD);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_USSER_NAME);
		JTextField nomReal = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_REAL_NAME);
		JButton registrar2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.REG_BUTTON_REGISTRAR);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("kevin123", robot);
		TestUtils.clickComponent(pass, robot);
		TestUtils.tipeaTexto("12345", robot);
		TestUtils.clickComponent(repe, robot);
		TestUtils.tipeaTexto("123456", robot);
		TestUtils.clickComponent(nomReal, robot);
		TestUtils.tipeaTexto("Kevin", robot);
		TestUtils.clickComponent(registrar2, robot);
		Assert.assertEquals("Mensaje incorrecto, deberia decir: " , Mensajes.PASS_NO_COINCIDE.getValor(), op.getMensaje());
	}
	

	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getClientes().clear();	
		}
}
