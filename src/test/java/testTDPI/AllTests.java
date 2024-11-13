package testTDPI;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestAuto.class, TestCombi.class, TestMoto.class, TestChoferPermanente.class, TestChoferTemporario.class, TestViaje.class, 
	TestPedido.class, TestCliente.class, TestEmpresa_Esc1.class, TestEmpresa_Esc2.class, TestEmpresa_Esc3.class, TestEmpresa_Esc4.class,
	TestIntegración.class, TestPersistencia.class, TestPersistenciaSinArchivo.class, TestEmpresa_Esc1.class, TestEmpresa_Esc2.class, TestGUI_panelLogin.class,
	TestGUI_panelRegistrar.class, TestGUI_panelLogin.class, testGUI_panelCliente_Esc1.class, testGUI_panelCliente_Esc2.class, testGUI_panelCliente_Esc3.class, testGUI_panelCliente_Esc4.class,
	testGUI_panelCliente_Esc5.class, testGUI_panelAdministradorListasLLenas.class, testGUI_panelAdministradorListasVacias.class 
})
public class AllTests {

}
