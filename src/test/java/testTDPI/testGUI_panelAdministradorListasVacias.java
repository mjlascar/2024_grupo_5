package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.awt.TextField;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import excepciones.UsuarioYaExisteException;
import modeloDatos.Chofer;
import modeloDatos.ChoferTemporario;
import modeloNegocio.Empresa;
import util.Constantes;
import vista.Ventana;

public class testGUI_panelAdministradorListasVacias {
	private Robot robot;
	private Controlador controlador;
	FalsoOptionPane op = new FalsoOptionPane();
	
	@Before
	public void setup() {
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		try {
			robot = new Robot();
			JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
			JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
			JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
			TestUtils.clickComponent(nombre, robot);
			TestUtils.tipeaTexto("admin", robot);
			TestUtils.clickComponent(contra, robot);
			TestUtils.tipeaTexto("admin", robot);
			TestUtils.clickComponent(login, robot);
		} catch (AWTException e) {
			
		}
	}
	
	
	//VISUALIZACION INFORMACION
	@Test
	public void testPanel() {
		JPanel panelAdmin = (JPanel) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PANEL_ADMINISTRADOR);
		Assert.assertTrue("El panel Admin deberia estar habilitado", panelAdmin.isEnabled());
	}
	@Test
	public void testListaChoferesTotalesVacia() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		Assert.assertTrue("La lista choferes totales deberia estar vacia: ", listaChoferes.getModel().getSize() == 0);
	}

	@Test
	public void testListaViajesChoferesVacia() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VIAJES_DE_CHOFER);
		Assert.assertTrue("La lista choferes totales deberia estar vacia: ", lista.getModel().getSize() == 0);
	}
	
	@Test
	public void testCalificacionChofer() {
		JTextField campo = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CALIFICACION_CHOFER);
		Assert.assertTrue("El campo de calificacion de chofer deberia estar vacio: ", campo.getText().equals(""));
	}
	@Test
	public void testSueldoChofer() {
		JTextField campo = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.SUELDO_DE_CHOFER);
		Assert.assertTrue("El campo de sueldo de chofer deberia estar vacio: ", campo.getText().equals(""));
	}
	@Test
	public void testTotalSueldosPagar() {
		JTextField campo = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.TOTAL_SUELDOS_A_PAGAR);
		Assert.assertTrue("El campo de sueldos a pagar deberia estar vacio: ", campo.getText().equals("0.00"));
	}
	
	@Test
	public void testListaClientes() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTADO_DE_CLIENTES);
		Assert.assertTrue("La lista de clientes deberia estar vacia: ", lista.getModel().getSize() == 0);
	}
	
	@Test
	public void testListaVehiculos() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		Assert.assertTrue("La lista vehiculos totales deberia estar vacia: ", lista.getModel().getSize() == 0);
	}
	
	@Test
	public void testListaViajesHistoricos() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VIAJES_HISTORICOS);
		Assert.assertTrue("La lista viajes historicos deberia estar vacia: ", lista.getModel().getSize() == 0);
	}
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getClientes().clear();
		Empresa.getInstance().getChoferes().clear();
	}
}
