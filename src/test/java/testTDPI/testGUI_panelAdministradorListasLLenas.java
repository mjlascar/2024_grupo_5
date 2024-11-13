package testTDPI;

import static org.junit.Assert.*;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Robot;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import controlador.Controlador;
import excepciones.ChoferRepetidoException;
import excepciones.ClienteConPedidoPendienteException;
import excepciones.ClienteConViajePendienteException;
import excepciones.ClienteNoExisteException;
import excepciones.SinVehiculoParaPedidoException;
import excepciones.UsuarioYaExisteException;
import excepciones.VehiculoRepetidoException;
import modeloDatos.Auto;
import modeloDatos.Chofer;
import modeloDatos.ChoferPermanente;
import modeloDatos.ChoferTemporario;
import modeloDatos.Cliente;
import modeloDatos.Combi;
import modeloDatos.Moto;
import modeloDatos.Pedido;
import modeloDatos.Vehiculo;
import modeloDatos.Viaje;
import modeloNegocio.Empresa;
import util.Constantes;
import util.Mensajes;
import vista.Ventana;

public class testGUI_panelAdministradorListasLLenas {
	private Robot robot;
	private Controlador controlador;
	private FalsoOptionPane op = new FalsoOptionPane();
	private Cliente cliente = new Cliente("kevin123","12345", "Kevin");
	private Cliente cliente2 = new Cliente("mafalda67","1290", "Mafalda");
	private Vehiculo auto = new Auto("ASD 123",4, true);
	private Vehiculo moto = new Moto("FIA 223");
	private Chofer chofer = new ChoferTemporario("12345678","Brey");
	private Chofer chofer2 = new ChoferTemporario("1234578","Valentini");
	
	@Before
	public void setup() {
		
		controlador = new Controlador();
		controlador.getVista().setOptionPane(op);
		
		try {
			robot = new Robot();
			JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);
			JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
			JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);
			Viaje viaje = new Viaje(new Pedido(cliente2,1,false,false,3,Constantes.ZONA_STANDARD),chofer2,moto);
			Empresa.getInstance().agregarChofer(chofer);
			Empresa.getInstance().agregarChofer(chofer2);
			Empresa.getInstance().agregarVehiculo(auto);
			Empresa.getInstance().agregarVehiculo(moto);
			Empresa.getInstance().agregarCliente("kevin123","12345", "Kevin");
			Empresa.getInstance().agregarCliente("mafalda67","1290", "Mafalda");
			Empresa.getInstance().getViajesTerminados().add(new Viaje(new Pedido(cliente,1,false,false,6,Constantes.ZONA_STANDARD),chofer,auto));
			Empresa.getInstance().getViajesIniciados().put(cliente2, viaje);
			TestUtils.clickComponent(nombre, robot);
			TestUtils.tipeaTexto("admin", robot);
			TestUtils.clickComponent(contra, robot);
			TestUtils.tipeaTexto("admin", robot);
			TestUtils.clickComponent(login, robot);
		} catch (AWTException e) {
			
		} catch (ChoferRepetidoException e) {

		} catch (VehiculoRepetidoException e) {

		} catch (UsuarioYaExisteException e) {
			
		}
	}
	public void simuloPedidoyReLogueo(Robot robot, Controlador controlador,Pedido pedido) {
		JButton cerrarsesion = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CERRAR_SESION_ADMIN);
		TestUtils.clickComponent(cerrarsesion, robot);
		robot.delay(1000);
		JTextField contra = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PASSWORD);
		JButton login = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LOGIN);		
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_USUARIO);		
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto(pedido.getCliente().getNombreUsuario(), robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto(pedido.getCliente().getPass(), robot);
		TestUtils.clickComponent(login, robot);
		JButton hacerPedido = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_PEDIDO );
		JTextField cantPax = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_PAX );
		JButton cerrarsesion_2 = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CERRAR_SESION_CLIENTE);
		TestUtils.clickComponent(cantPax, robot);
		TestUtils.tipeaTexto(String.valueOf(pedido.getCantidadPasajeros()), robot);
		JTextField cantKM = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CANT_KM );
		TestUtils.clickComponent(cantKM, robot);
		TestUtils.tipeaTexto(String.valueOf(pedido.getKm()), robot);
		TestUtils.clickComponent(hacerPedido, robot);
		TestUtils.clickComponent(cerrarsesion_2, robot);
		//vuelvo login
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("admin", robot);
		TestUtils.clickComponent(contra, robot);
		TestUtils.tipeaTexto("admin", robot);
		TestUtils.clickComponent(login, robot);
	}
	//TEST LISTAS APARECEN BIEN 
	@Test
	public void testListaChoferesTotalesVacia() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		Assert.assertTrue("La lista choferes totales no deberia estar vacia: ", listaChoferes.getModel().getSize() > 0);
	}
	@Test
	public void testListaChoferesLibres() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		Assert.assertTrue("La lista choferes totales no deberia estar vacia: ", listaChoferes.getModel().getSize() > 0);
	}
	@Test
	public void testListaClientes() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTADO_DE_CLIENTES);
		Assert.assertTrue("La lista de clientes no deberia estar vacia: ", lista.getModel().getSize() > 0);
	}
	
	@Test
	public void testListaVehiculos() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		Assert.assertTrue("La lista vehiculos totales no deberia estar vacia: ", lista.getModel().getSize() > 0);
	}
	@Test
	public void testListaVehiculosDisp() {
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		Assert.assertFalse("La lista vehiculos disponibles  deberia estar vacia: ", lista.getModel().getSize() > 0);
	}
	//REGISTRO DE UN NUEVO CHOFER TEMPORARIO
	@Test
	public void testBotonNuevoTempNombre() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	
	@Test
	public void testBotonNuevoTempDNI() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("44460321", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	
	@Test
	public void testAgregoTempNombreVaciado() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("44460321", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo nombre deberia estar vacio: ",nuevo.getText().equals(""));
	}
	@Test
	public void testAgregoTempDNIVaciado() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("44460321", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo dni deberia estar vacio: ",dni.getText().equals(""));
	}
	@Test
	public void testAgregoTemp() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("44460321", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("La lista choferes totales deberia incluir a Jorge: ",lista.getModel().getSize() > 1);
	}
	@Test
	public void testAgregoTempRepetido() {
		JRadioButton temp = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.TEMPORARIO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		TestUtils.clickComponent(temp, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345678", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertEquals("Mensaje incorrecto" , Mensajes.CHOFER_YA_REGISTRADO.getValor(), op.getMensaje());
	}
	//REGISTRO DE UN NUEVO CHOFER PERMANENTE
	
	public void testBotonNuevoPermNombre() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Jorge", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	
	public void testBotonNuevoPermDNI() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("44460321", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	
	public void testBotonNuevoPermCantHijos() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	
	public void testBotonNuevoPermAnios() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(anios, robot);
		TestUtils.tipeaTexto("2018", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermAnioLimiteBajo() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("1900", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(perm, robot);//lo hago para que haga tiempo, delay no sirve
		Assert.assertTrue("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermAnioBajo() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("0", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermAnioLimiteAlto() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("3000", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(perm, robot);//lo hago para que haga tiempo, delay no sirve
		Assert.assertTrue("El boton nuevo chofer deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermAnioAlto() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("5000", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermAnioNoNumerico() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("holabuendia", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermHijosBajo() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("-1", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermHijosLimite() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("0", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(anios , robot);//pongo esto para que el programa espere un poquito antes de pasar al assert
		Assert.assertTrue("El boton nuevo chofer deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermHijosNoNumerico() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("holabuendia", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoPermOK() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo chofer no deberia estar habilitado: ",nuevo.isEnabled());
	}
	@Test
	public void testAgregoPermNombreVaciado() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo nombre deberia estar vacio: ",nombre.getText().equals(""));
	}
	@Test
	public void testAgregoPermDniVaciado() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo dni deberia estar vacio: ",dni.getText().equals(""));
	}
	@Test
	public void testAgregoPermAnioVaciado() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo anio deberia estar vacio: ",anios.getText().equals(""));
	}
	@Test
	public void testAgregoPermCantHijosVaciado() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);

		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El campo cantidad hijos deberia estar vacio: ",cant_hijos.getText().equals(""));
	}
	
	@Test
	public void testAgregoPermOK() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345679", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		System.out.println(lista.getModel().getSize());
		Assert.assertTrue(lista.getModel().getSize() > 1);

	}
	@Test
	public void testAgregoPermRepetido() {
		JRadioButton perm = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.PERMANENTE);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_CHOFER);
		JTextField nombre = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NOMBRE_CHOFER);
		JTextField dni = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.DNI_CHOFER);
		JTextField anios = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_ANIO);
		JTextField cant_hijos = (JTextField) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.CH_CANT_HIJOS);
		JList lista = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		TestUtils.clickComponent(perm, robot);
		TestUtils.clickComponent(dni, robot);
		TestUtils.tipeaTexto("12345678", robot);
		TestUtils.clickComponent(nombre, robot);
		TestUtils.tipeaTexto("Zenon", robot);
		TestUtils.clickComponent(anios , robot);
		TestUtils.tipeaTexto("2018", robot);
		TestUtils.clickComponent(cant_hijos, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertEquals("Mensaje incorrecto" , Mensajes.CHOFER_YA_REGISTRADO.getValor(), op.getMensaje());
	}
	
	// VISUALIZACION AL SELECCIONAR CHOFER
	@Test
	public void testSeleccionoChoferHistoricos() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		JList historicosChofer = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_VIAJES_DE_CHOFER);
		listaChoferes.setSelectedIndex(0);
		Assert.assertTrue(historicosChofer.getModel().getSize() > 0);
	}
	@Test
	public void testSeleccionoChoferSueldo() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		JTextField sueldo = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.SUELDO_DE_CHOFER);
		listaChoferes.setSelectedIndex(0);
		Assert.assertFalse(sueldo.getText().equals(""));
	}
	@Test
	public void testSeleccionoChoferCalificacion() {
		JList listaChoferes = (JList) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.LISTA_CHOFERES_TOTALES);
		JTextField calificacion = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CALIFICACION_CHOFER);
		listaChoferes.setSelectedIndex(0);
		Assert.assertFalse(calificacion.getText().equals(""));
	}


// AGREGADO VEHICULOS
	//MOTO
	@Test
	public void testBotonMotoDeshabilitado() {
		JRadioButton moto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.MOTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		TestUtils.borraJTextField(patente, robot);
		TestUtils.clickComponent(moto, robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonMotoHabilitado() {
		JRadioButton moto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.MOTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		TestUtils.clickComponent(moto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 449", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	
	//COMBI
	
	@Test
	public void testBotonCombiOK() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("7", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 447", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiVacioPatente() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("7", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiVacioPlazas() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 447", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiPlazasLimiteBajo() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("5", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 447", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiPlazasLimiteAlto() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("10", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiPlazasBajo() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("4", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonCombiPlazasAlto() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("11", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	//AUTO
	@Test
	public void testBotonAutoOK() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoVacioPatente() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("2", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoVacioPlazas() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoPlazasLimiteBajo() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("1", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoPlazasLimiteAlto() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("4", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		Assert.assertTrue("El boton nuevo vehiculo deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoPlazasBajo() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("0", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
	@Test
	public void testBotonAutoPlazasAlto() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("5", robot);
		Assert.assertFalse("El boton nuevo vehiculo no deberia estar habilitado: ", nuevo.isEnabled());
	}
//AGREGO CADA UNO
	@Test
	public void testAgregoAuto() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 448", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El nuevo vehiculo deberia haber sido agregado: ", vehiculos.getModel().getSize() > 1);
	}
	@Test
	public void testAgregoMoto() {
		JRadioButton moto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.MOTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(moto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 446", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El nuevo vehiculo deberia haber sido agregado: ", vehiculos.getModel().getSize() > 1);
	}
	@Test
	public void testAgregoCombi() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("7", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("FIA 445", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("El nuevo vehiculo deberia haber sido agregado: ", vehiculos.getModel().getSize() > 1);
	}
//AGREGO REPETIDOS
	@Test
	public void testAgregoAutoRepetido() {
		JRadioButton auto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.AUTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(auto, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("2", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("ASD 123", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertEquals("Se muestra el mensaje equivocado: ", Mensajes.VEHICULO_YA_REGISTRADO.getValor(), op.getMensaje());
	}
	@Test
	public void testAgregoMotoRepetida() {
		JRadioButton moto = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.MOTO);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(moto, robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("ASD 123", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertEquals("Se muestra el mensaje equivocado: ", Mensajes.VEHICULO_YA_REGISTRADO.getValor(), op.getMensaje());
	}
	@Test
	public void testAgregoCombiRepetida() {
		JRadioButton combi = (JRadioButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.COMBI);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VEHICULO);
		JTextField patente = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.PATENTE);
		JTextField cant_plazas = (JTextField) TestUtils.getComponentForName((Component)controlador.getVista(), Constantes.CANTIDAD_PLAZAS);
		JList vehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_TOTALES);
		TestUtils.clickComponent(combi, robot);
		TestUtils.clickComponent(cant_plazas, robot);
		TestUtils.tipeaTexto("7", robot);
		TestUtils.clickComponent(patente, robot);
		TestUtils.tipeaTexto("ASD 123", robot);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertEquals("Se muestra el mensaje equivocado: ", Mensajes.VEHICULO_YA_REGISTRADO.getValor(), op.getMensaje());
	}
// GESTION DE PEDIDOS 
	@Test
	public void testListaPedidosPendientes(){
		this.simuloPedidoyReLogueo(robot, controlador, new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD));
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		Assert.assertTrue("La lista de pedidos pendientes deberia tener el pedido: ", listaPendientes.getModel().getSize() > 0);
	}
	
	@Test
	public void testListaPedidosPendientesSelectValido(){
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD));
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JList listaVehiculosDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		listaPendientes.setSelectedIndex(0);
		Assert.assertTrue("La lista de vehiculos disponibles no deberia estar vacia: ", listaVehiculosDisp.getModel().getSize() > 0);
	}
	
	@Test
	public void testListaPedidosPendientesSelectNoCumple(){
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,8,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JList listaVehiculosDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		listaPendientes.setSelectedIndex(0);
		Assert.assertFalse("La lista de vehiculos disponibles deberia estar vacia: ", listaVehiculosDisp.getModel().getSize() > 0);
	}
	
	@Test
	public void testBotonNuevoViaje() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		listaPendientes.setSelectedIndex(0);
		Assert.assertFalse("El boton nuevo viaje no deberia estar habilitado",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoViajePedidoMasChofer() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaChoferesDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaPendientes.setSelectedIndex(0);
		listaChoferesDisp.setSelectedIndex(0);
		Assert.assertFalse("El boton nuevo viaje no deberia estar habilitado",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoViajePedidoMasVehiculo() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaVehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		listaPendientes.setSelectedIndex(0);
		listaVehiculos.setSelectedIndex(0);
		Assert.assertFalse("El boton nuevo viaje no deberia estar habilitado",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoViajeHabilitado() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaVehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		JList listaChoferesDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaPendientes.setSelectedIndex(0);
		listaVehiculos.setSelectedIndex(0);
		listaChoferesDisp.setSelectedIndex(0);
		Assert.assertTrue("El boton nuevo viaje deberia estar habilitado",nuevo.isEnabled());
	}
	@Test
	public void testBotonNuevoViajePresionado_VehiculosVacio() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaVehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		JList listaChoferesDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaPendientes.setSelectedIndex(0);
		listaVehiculos.setSelectedIndex(0);
		listaChoferesDisp.setSelectedIndex(0);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("La lista de vehiculos disponibles deberia estar vacia: ", listaVehiculos.getModel().getSize() == 0);
	}
	@Test
	public void testBotonNuevoViajePresionado_PedidosActualizados() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaVehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		JList listaChoferesDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaPendientes.setSelectedIndex(0);
		listaVehiculos.setSelectedIndex(0);
		listaChoferesDisp.setSelectedIndex(0);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("La lista de pedidos pendientes deberia estar sin el pedido: ", listaPendientes.getModel().getSize() == 0);
	}
	@Test
	public void testBotonNuevoViajePresionado_ChoferesActualizados() {
		this.simuloPedidoyReLogueo(robot, controlador,new Pedido(cliente,2,false,false,4,Constantes.ZONA_STANDARD)); //ningun vehiculo cumple
		JList listaPendientes = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_PEDIDOS_PENDIENTES);
		JButton nuevo = (JButton) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.NUEVO_VIAJE);
		JList listaVehiculos = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_VEHICULOS_DISPONIBLES);
		JList listaChoferesDisp = (JList) TestUtils.getComponentForName((Component) controlador.getVista(), Constantes.LISTA_CHOFERES_LIBRES);
		listaPendientes.setSelectedIndex(0);
		listaVehiculos.setSelectedIndex(0);
		listaChoferesDisp.setSelectedIndex(0);
		TestUtils.clickComponent(nuevo, robot);
		Assert.assertTrue("La lista de choferes deberia estar sin el chofer: ", listaChoferesDisp.getModel().getSize() == 1);
	}
	@After
	public void tearDown() {
		Ventana ventana = (Ventana) controlador.getVista();
		ventana.setVisible(false);
		Empresa.getInstance().getChoferesDesocupados().clear();
		Empresa.getInstance().getClientes().clear();
		Empresa.getInstance().getChoferes().clear();
		Empresa.getInstance().getVehiculos().clear();
		Empresa.getInstance().getPedidos().clear();
		Empresa.getInstance().getViajesTerminados().clear();
		Empresa.getInstance().getHistorialViajeChofer(chofer).clear();
		Empresa.getInstance().getHistorialViajeChofer(chofer2).clear();
		}
	}
