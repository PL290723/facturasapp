// Archivo: FacturacionApp.java
// Clase principal que contiene el método main para ejecutar la aplicación
package FacturasApp;

import javax.swing.*;


public class FacturacionApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FacturaFrame().setVisible(true);
        });
    }
}

// Archivo: FacturaFrame.java
// Clase que define la ventana principal de la aplicación


// Archivo: Cliente.java
// Clase que representa a un cliente


// Archivo: Producto.java
// Clase abstracta que representa un producto genérico (abstracción)

// Archivo: FacturasApp.ProductoNormal.java
// Clase que representa un producto normal (herencia)


// Archivo: FacturasApp.ProductoConDescuento.java
// Clase que representa un producto con descuento (herencia y polimorfismo)


// Archivo: FacturasApp.Factura.java
// Clase que representa una factura

