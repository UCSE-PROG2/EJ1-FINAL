package org.example.dto;

// File: CajaDeAhorro.java
public class CajaDeAhorro extends Cuenta implements IGestionSaldo {
    private CajaDeAhorro(Builder builder) {
        this.saldo = builder.initialSaldo;
        this.operaciones = 0;
    }

    public static class Builder {
        private double initialSaldo = 0;

        public Builder withInitialSaldo(double initialSaldo) {
            this.initialSaldo = initialSaldo;
            return this;
        }

        public CajaDeAhorro build() {
            return new CajaDeAhorro(this);
        }
    }

    @Override
    public synchronized boolean agregarSaldo(double monto) {
        if (monto <= 0) return false;
        saldo += monto;
        operaciones++;
        return true;
    }

    @Override
    public synchronized boolean quitarSaldo(double monto) {
        if (monto <= 0 || monto > saldo) return false;
        saldo -= monto;
        operaciones++;
        return true;
    }
}
