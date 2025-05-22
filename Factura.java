package FacturasApp;

import java.text.SimpleDateFormat;
import java.util.Date;

class Factura {
    private static int numeroFactura = 1000;
    private int numero;
    private Cliente cliente;
    private Producto[] productos;
    private int cantidadProductos;
    private Date fecha;

    public Factura(Cliente cliente, Producto[] productos, int cantidadProductos) {
        this.numero = ++numeroFactura;
        this.cliente = cliente;
        this.productos = productos;
        this.cantidadProductos = cantidadProductos;
        this.fecha = new Date();
    }

    public double calcularTotalSinImpuestos() {
        double total = 0;
        for (int i = 0; i < cantidadProductos; i++) {
            total += productos[i].calcularSubtotal();
        }
        return total;
    }

    public double calcularTotalImpuestos() {
        double totalImpuestos = 0;
        for (int i = 0; i < cantidadProductos; i++) {
            totalImpuestos += productos[i].calcularImpuesto();
        }
        return totalImpuestos;
    }

    public double calcularTotal() {
        return calcularTotalSinImpuestos() + calcularTotalImpuestos();
    }

    public String generarFactura() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();

        sb.append("=============================================\n");
        sb.append("              FACTURA DE VENTA              \n");
        sb.append("=============================================\n");
        sb.append("Factura N°: ").append(numero).append("\n");
        sb.append("Fecha: ").append(sdf.format(fecha)).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append("Cliente: ").append(cliente.getNombre()).append("\n");
        sb.append("NIT/CI: ").append(cliente.getNit()).append("\n");
        sb.append("---------------------------------------------\n");
        sb.append(String.format("%-20s %-10s %-8s %-12s %-10s %-12s\n",
                "Producto", "Precio", "Cantidad", "Subtotal", "Descuento", "Total"));
        sb.append("---------------------------------------------\n");

        double totalDescuentos = 0;
        for (int i = 0; i < cantidadProductos; i++) {
            Producto p = productos[i];
            String descuentoInfo = p instanceof ProductoConDescuento ?
                    String.format("%.2f%%", ((ProductoConDescuento) p).getPorcentajeDescuento()) : "0%";
            double montoDescuento = p instanceof ProductoConDescuento ?
                    ((ProductoConDescuento) p).calcularDescuento() : 0;
            totalDescuentos += montoDescuento;
            sb.append(String.format("%-20s $%-9.2f %-8d $%-12.2f %-10s $%-12.2f\n",
                    p.getNombre(), p.getPrecio(), p.getCantidad(),
                    p.calcularSubtotal() + montoDescuento, descuentoInfo, p.calcularSubtotal()));
        }

        sb.append("---------------------------------------------\n");
        sb.append(String.format("%-41s $%-12.2f\n", "Subtotal:", calcularTotalSinImpuestos() + totalDescuentos));
        sb.append(String.format("%-41s $%-12.2f\n", "Descuentos:", totalDescuentos));
        sb.append(String.format("%-41s $%-12.2f\n", "Subtotal con Descuentos:", calcularTotalSinImpuestos()));
        sb.append(String.format("%-41s $%-12.2f\n", "IVA (16%):", calcularTotalImpuestos()));
        sb.append(String.format("%-41s $%-12.2f\n", "TOTAL:", calcularTotal()));
        sb.append("=============================================\n");
        sb.append("           GRACIAS POR SU COMPRA            \n");
        sb.append("=============================================\n");

        return sb.toString();
    }
}