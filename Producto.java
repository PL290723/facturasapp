package FacturasApp;

public abstract class Producto {
    protected String nombre;
    protected double precio;
    protected int cantidad;

    public Producto(String nombre, double precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    // Método abstracto que las subclases deben implementar (polimorfismo)
    public abstract double calcularSubtotal();

    // Método para calcular impuestos
    public double calcularImpuesto() {
        return calcularSubtotal() * 0.16; // IVA del 13%
    }
}
