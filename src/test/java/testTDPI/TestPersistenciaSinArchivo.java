package testTDPI;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import modeloDatos.Auto;
import modeloDatos.ChoferTemporario;
import modeloNegocio.Empresa;
import persistencia.EmpresaDTO;
import persistencia.IPersistencia;
import persistencia.PersistenciaBIN;
import persistencia.UtilPersistencia;

public class TestPersistenciaSinArchivo {
	
	private Empresa empresa;

	@Before
	public void setUp() throws Exception {
	
		this.empresa = Empresa.getInstance();
		empresa.agregarChofer(new ChoferTemporario("34576832", "Juan"));
		empresa.agregarCliente("ximena_destroyer", "12345", "Kevin");
		empresa.agregarVehiculo(new Auto("FIA 449", 2, true));
        File arch = new File("Empresa.bin");
		if (arch.exists()) {
			arch.delete();
		}
	}

	@Test
	public void testPersistir() {
		EmpresaDTO empresaDTO;
		IPersistencia persistencia = new PersistenciaBIN();
		try{    
			persistencia.abrirOutput("Empresa.bin");
			persistencia.escribir( UtilPersistencia.EmpresaDtoFromEmpresa());
			persistencia.cerrarOutput();
            File arch = new File("Empresa.bin");
            Assert.assertTrue("Debería existir el archivo Empresa.bin", arch.exists());
			persistencia.abrirInput("Empresa.bin");
			empresaDTO = (EmpresaDTO) persistencia.leer();
			UtilPersistencia.empresaFromEmpresaDTO(empresaDTO);
			Assert.assertNotEquals(null, empresa); //no deberia de dar null pq se supone que cargamos la empresa con datos?????
			Assert.assertEquals(empresa.getChoferes().get("34576832").getNombre(), "Juan");
			Assert.assertEquals(empresa.getClientes().get("ximena_destroyer").getNombreReal(), "Kevin");
			Assert.assertEquals(empresa.getVehiculos().get("FIA 449").getPatente(),"FIA 449" );
		}
		catch (Exception e){
			
			Assert.fail("no deberia lanzar excepcion !!!!!!");

		}



	}
	
	@After
	public void tearDown() {
        empresa.getChoferes().clear();
		empresa.getClientes().clear();
		empresa.getVehiculos().clear();
	}
	
}
