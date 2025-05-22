package FacturasApp;

public class ProductoConDescuento extends Producto {
    private double porcentajeDescuento;

    public ProductoConDescuento(String nombre, double precio, int cantidad, double porcentajeDescuento) {
        super(nombre, precio, cantidad);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularSubtotal() {
        double subtotalSinDescuento = precio * cantidad;
        double descuento = subtotalSinDescuento * (porcentajeDescuento / 100);
        return subtotalSinDescuento - descuento;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public double calcularDescuento() {
        return precio * cantidad * (porcentajeDescuento / 100);
    }
}