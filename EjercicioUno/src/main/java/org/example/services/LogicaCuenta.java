package org.example.services;

import org.example.dto.Cuenta;
import org.example.dto.IGestionSaldo;

import java.util.ArrayList;
import java.util.List;

public class LogicaCuenta {
    private static volatile LogicaCuenta instance;
    private final List<Cuenta> cuentas = new ArrayList<>();

    private LogicaCuenta() {}

    public static LogicaCuenta getInstance() {
        if (instance == null) {
            synchronized (LogicaCuenta.class) {
                if (instance == null) {
                    instance = new LogicaCuenta();
                }
            }
        }
        return instance;
    }

    public synchronized void setCuentas(List<Cuenta> cuentasIniciales) {
        cuentas.clear();
        cuentas.addAll(cuentasIniciales);
    }

    public boolean agregarSaldo(int index, double monto) {
        Cuenta c = cuentas.get(index);
        return ((IGestionSaldo) c).agregarSaldo(monto);
    }

    public boolean quitarSaldo(int index, double monto) {
        Cuenta c = cuentas.get(index);
        return ((IGestionSaldo) c).quitarSaldo(monto);
    }

    public double consultarSaldo(int index) {
        return cuentas.get(index).getSaldo();
    }

    public int consultarOperaciones(int index) {
        return cuentas.get(index).getOperaciones();
    }
}
