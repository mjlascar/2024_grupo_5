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
import util.Mensajes;
import vista.Ventana;

public class testGUI_panelCliente_Esc2 {
	private Robot robot;
	private Controlador controlador;
	private Empresa empresa= Empresa.getInstance();
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() { //ESCENARIO, SITUACION 1, ARRAY VEHICULOS CON AUTO
		
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
		} catch (AWTException e) {
			
		}
	      catch (UsuarioYaExisteException e) {
	    	  
		} catch (VehiculoRepetidoException e) {
			
		}
	}
	
	@Test
	public void testBotonPaxYKMCorrectasConAutoApto() {
		JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("3", robot);
		
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto("10", robot);
		
		TestUtils.clickComponent(aceptar, robot);
		Assert.assertFalse("El TextArea pedViajAct no deberia estar vacio", pedViajAct.getText().trim().isEmpty() );
	}
	
	@Test
	public void testBotonPaxYKMCorrectasSinAutoApto() {
		JTextArea pedViajAct = (JTextArea) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PEDIDO_O_VIAJE_ACTUAL);
		JButton aceptar = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto("5", robot);
		
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
