package FacturasApp;

import javax.swing.*;
import java.awt.*;

class FacturaFrame extends JFrame {
    private JTextField txtCliente, txtNit, txtProducto, txtPrecio, txtCantidad, txtDescuento;
    private JButton btnAgregar, btnGenerar, btnLimpiar, btnSalir;
    private JTextArea txtAreaDetalle, txtAreaFactura;
    private Producto[] productos;
    private int cantidadProductos;
    private static final int MAX_PRODUCTOS = 100;
    private Factura factura;

    public FacturaFrame() {
        super("Sistema de Facturación");
        inicializarComponentes();
        productos = new Producto[MAX_PRODUCTOS];
        cantidadProductos = 0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        // Panel de datos del cliente
        JPanel panelCliente = new JPanel(new GridLayout(2, 4, 10, 10));
        panelCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        panelCliente.add(new JLabel("Nombre Cliente:"));
        txtCliente = new JTextField();
        panelCliente.add(txtCliente);

        panelCliente.add(new JLabel("NIT/CI:"));
        txtNit = new JTextField();
        panelCliente.add(txtNit);

        // Panel de productos
        JPanel panelProductos = new JPanel(new GridLayout(4, 6, 10, 10));
        panelProductos.setBorder(BorderFactory.createTitledBorder("Agregar Productos"));

        panelProductos.add(new JLabel("Producto:"));
        txtProducto = new JTextField();
        panelProductos.add(txtProducto);

        panelProductos.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelProductos.add(txtPrecio);

        panelProductos.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField();
        panelProductos.add(txtCantidad);

        panelProductos.add(new JLabel("Descuento (%):"));
        txtDescuento = new JTextField();
        panelProductos.add(txtDescuento);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnAgregar = new JButton("Agregar Producto");
        btnGenerar = new JButton("Generar Factura");
        btnLimpiar = new JButton("Limpiar");
        btnSalir = new JButton("Salir");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnGenerar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnSalir);

        // Panel de detalle de productos
        JPanel panelDetalle = new JPanel(new BorderLayout());
        panelDetalle.setBorder(BorderFactory.createTitledBorder("Detalle de Productos"));
        txtAreaDetalle = new JTextArea();
        txtAreaDetalle.setEditable(false);
        JScrollPane scrollDetalle = new JScrollPane(txtAreaDetalle);
        scrollDetalle.setPreferredSize(new Dimension(350, 150));
        panelDetalle.add(scrollDetalle, BorderLayout.CENTER);

        // Panel de factura
        JPanel panelFactura = new JPanel(new BorderLayout());
        panelFactura.setBorder(BorderFactory.createTitledBorder("Factura Generada"));
        txtAreaFactura = new JTextArea();
        txtAreaFactura.setEditable(false);
        JScrollPane scrollFactura = new JScrollPane(txtAreaFactura);
        scrollFactura.setPreferredSize(new Dimension(350, 150));
        panelFactura.add(scrollFactura, BorderLayout.CENTER);

        // Configurar el layout principal
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelCliente, BorderLayout.NORTH);
        panelSuperior.add(panelProductos, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 10, 10));
        panelInferior.add(panelDetalle);
        panelInferior.add(panelFactura);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.CENTER);

        // Configurar los eventos de los botones
        configurarEventos();
    }

    private void configurarEventos() {
        btnAgregar.addActionListener(e -> agregarProducto());
        btnGenerar.addActionListener(e -> generarFactura());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void agregarProducto() {
        try {
            if (cantidadProductos >= MAX_PRODUCTOS) {
                JOptionPane.showMessageDialog(this, "No se pueden agregar más productos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nombre = txtProducto.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            String descuentoStr = txtDescuento.getText().trim();
            double porcentajeDescuento = descuentoStr.isEmpty() ? 0 : Double.parseDouble(descuentoStr);

            if (nombre.isEmpty() || precio <= 0 || cantidad <= 0) {
                JOptionPane.showMessageDialog(this, "Datos de producto inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto producto;
            if (porcentajeDescuento > 0) {
                producto = new ProductoConDescuento(nombre, precio, cantidad, porcentajeDescuento);
            } else {
                producto = new ProductoNormal(nombre, precio, cantidad);
            }
            productos[cantidadProductos] = producto;
            cantidadProductos++;

            actualizarDetalleProductos();

            // Limpiar campos de producto
            txtProducto.setText("");
            txtPrecio.setText("");
            txtCantidad.setText("");
            txtDescuento.setText("");
            txtProducto.requestFocus();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio, cantidad o descuento inválidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDetalleProductos() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %-10s %-8s %-12s %-10s %-12s\n",
                "Producto", "Precio", "Cantidad", "Subtotal", "Descuento", "Total"));
        sb.append("-------------------------------------------------------------------------\n");

        for (int i = 0; i < cantidadProductos; i++) {
            Producto p = productos[i];
            String descuentoInfo = p instanceof ProductoConDescuento ?
                    String.format("%.2f%%", ((ProductoConDescuento) p).getPorcentajeDescuento()) : "0%";
            double montoDescuento = p instanceof ProductoConDescuento ?
                    ((ProductoConDescuento) p).calcularDescuento() : 0;
            sb.append(String.format("%-20s $%-9.2f %-8d $%-12.2f %-10s $%-12.2f\n",
                    p.getNombre(), p.getPrecio(), p.getCantidad(),
                    p.calcularSubtotal() + montoDescuento, descuentoInfo, p.calcularSubtotal()));
        }

        txtAreaDetalle.setText(sb.toString());
    }

    private void generarFactura() {
        if (cantidadProductos == 0) {
            JOptionPane.showMessageDialog(this, "Debe agregar al menos un producto", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String nombreCliente = txtCliente.getText().trim();
        String nit = txtNit.getText().trim();

        if (nombreCliente.isEmpty() || nit.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar los datos del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear cliente y factura
        Cliente cliente = new Cliente(nombreCliente, nit);

        // Crear una copia de los productos para la factura
        Producto[] productosFactura = new Producto[cantidadProductos];
        for (int i = 0; i < cantidadProductos; i++) {
            productosFactura[i] = productos[i];
        }

        factura = new Factura(cliente, productosFactura, cantidadProductos);

        // Mostrar la factura generada
        txtAreaFactura.setText(factura.generarFactura());
    }

    private void limpiarFormulario() {
        txtCliente.setText("");
        txtNit.setText("");
        txtProducto.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtDescuento.setText("");
        txtAreaDetalle.setText("");
        txtAreaFactura.setText("");
        cantidadProductos = 0;
    }
}