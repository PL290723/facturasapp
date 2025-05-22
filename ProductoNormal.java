package FacturasApp;

public class ProductoNormal extends Producto {

    public ProductoNormal(String nombre, double precio, int cantidad) {
        super(nombre, precio, cantidad);
    }

    @Override
    public double calcularSubtotal() {
        return precio * cantidad;
    }
}
