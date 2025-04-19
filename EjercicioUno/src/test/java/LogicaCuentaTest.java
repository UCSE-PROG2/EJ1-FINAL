import org.example.dto.CajaDeAhorro;
import org.example.dto.Cuenta;
import org.example.dto.CuentaCorriente;
import org.example.services.LogicaCuenta;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class LogicaCuentaTest {

    private LogicaCuenta logica;

    @BeforeEach
    void setUp() {
        logica = LogicaCuenta.getInstance();
        // Prepara dos cuentas: CajaDeAhorro y CuentaCorriente
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(new CajaDeAhorro.Builder()
                .withInitialSaldo(100.0)
                .build());
        cuentas.add(new CuentaCorriente.Builder()
                .withInitialSaldo(100.0)
                .withGiroDescubierto(50.0)
                .build());
        logica.setCuentas(cuentas);
    }

    @AfterEach
    void tearDown() {
        // Limpia las cuentas para evitar estados residuales
        logica.setCuentas(new ArrayList<>());
    }

    // Prueba que getInstance() devuelva siempre la misma instancia (Singleton)
    @Test
    void testSingletonInstance() {
        LogicaCuenta first = LogicaCuenta.getInstance();
        LogicaCuenta second = LogicaCuenta.getInstance();
        assertSame(first, second, "getInstance() should always return the same instance");
    }

    // Prueba que con índices inválidos lance IndexOutOfBoundsException
    @Test
    void testSetCuentasAndInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> logica.agregarSaldo(2, 10.0));
        assertThrows(IndexOutOfBoundsException.class, () -> logica.quitarSaldo(-1, 10.0));
        assertThrows(IndexOutOfBoundsException.class, () -> logica.consultarSaldo(5));
    }

    // Prueba agregarSaldo exitoso en CajaDeAhorro incrementa saldo y operaciones
    @Test
    void testAgregarSaldoSuccess() {
        boolean result = logica.agregarSaldo(0, 50.0);
        assertTrue(result, "Should be able to add to CajaDeAhorro");
        assertEquals(150.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(1, logica.consultarOperaciones(0));
    }

    // Prueba que agregarSaldo con monto inválido no modifique saldo ni operaciones
    @Test
    void testAgregarSaldoInvalidAmounts() {
        assertFalse(logica.agregarSaldo(0, -10.0), "Negative amount should not be added");
        assertEquals(100.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(0, logica.consultarOperaciones(0));
    }

    // Prueba retiro válido y bloqueo de retirada mayor al saldo en CajaDeAhorro
    @Test
    void testQuitarSaldoCajaDeAhorroValidAndInvalid() {
        boolean ok = logica.quitarSaldo(0, 40.0);
        assertTrue(ok);
        assertEquals(60.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(1, logica.consultarOperaciones(0));

        boolean no = logica.quitarSaldo(0, 100.0);
        assertFalse(no);
        assertEquals(60.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(1, logica.consultarOperaciones(0));
    }

    // Prueba retiros dentro y fuera del sobregiro en CuentaCorriente
    @Test
    void testQuitarSaldoCuentaCorrienteWithinAndBeyondOverdraft() {
        boolean ok = logica.quitarSaldo(1, 140.0);
        assertTrue(ok);
        assertEquals(-40.0, logica.consultarSaldo(1), 1e-6);
        assertEquals(1, logica.consultarOperaciones(1));

        boolean no = logica.quitarSaldo(1, 20.0);
        assertFalse(no);
        assertEquals(-40.0, logica.consultarSaldo(1), 1e-6);
        assertEquals(1, logica.consultarOperaciones(1));
    }

    // Prueba varias operaciones en ambas cuentas y verifica saldos y conteo de operaciones
    @Test
    void testMultipleOperationsTrackCorrectly() {
        assertTrue(logica.agregarSaldo(0, 10.0));
        assertTrue(logica.quitarSaldo(0, 5.0));
        assertTrue(logica.agregarSaldo(1, 20.0));
        assertTrue(logica.quitarSaldo(1, 140.0));

        assertEquals(105.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(-20.0, logica.consultarSaldo(1), 1e-6);
        assertEquals(2, logica.consultarOperaciones(0));
        assertEquals(2, logica.consultarOperaciones(1));
    }

    // Prueba valores iniciales de saldo y operaciones antes de cualquier operación
    @Test
    void testConsultarSaldoAndOperacionesInitial() {
        assertEquals(100.0, logica.consultarSaldo(0), 1e-6);
        assertEquals(100.0, logica.consultarSaldo(1), 1e-6);
        assertEquals(0, logica.consultarOperaciones(0));
        assertEquals(0, logica.consultarOperaciones(1));
    }
}
