package org.example.dto;

public abstract class Cuenta {
    protected double saldo;
    protected int operaciones;

    public abstract boolean agregarSaldo(double monto);
    public abstract boolean quitarSaldo(double monto);

    public synchronized double getSaldo() {
        return saldo;
    }

    public synchronized int getOperaciones() {
        return operaciones;
    }
}
