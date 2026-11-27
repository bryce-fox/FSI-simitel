import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


public class FProcedimientos extends JFrame {

    private final Conexion2 cnx = new Conexion2();
    private final String consulta = "SELECT * FROM recetas";
    private final JTextField txtNumero = new JTextField();
    private final JTextField txtFecha = new JTextField();
    private final JTextField txtVigencia = new JTextField();
    private final JTextField txtEstado = new JTextField();
    private final JTextField txtFolio = new JTextField();
    private final JTable tabla = new JTable();

    public FProcedimientos() {
        setTitle("Recetas - Procedimiento almacenado");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel campos = new JPanel(new GridLayout(0, 2, 5, 5));
        campos.add(new JLabel("Número de receta:"));
        campos.add(txtNumero);
        campos.add(new JLabel("Fecha (yyyy-MM-dd HH:mm:ss):"));
        campos.add(txtFecha);
        campos.add(new JLabel("Vigencia (yyyy-MM-dd):"));
        campos.add(txtVigencia);
        campos.add(new JLabel("Estado:"));
        campos.add(txtEstado);
        campos.add(new JLabel("Folio:"));
        campos.add(txtFolio);

        JPanel botones = new JPanel();
        JButton insertar = new JButton("Insertar");
        insertar.addActionListener(e -> insertarReceta());
        botones.add(insertar);

        JPanel superior = new JPanel(new BorderLayout(5, 5));
        superior.add(campos, BorderLayout.CENTER);
        superior.add(botones, BorderLayout.SOUTH);
        add(superior, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        setSize(800, 500);
        setLocationRelativeTo(null);
        if (cnx.conectar("localhost", "simitel", "root", "") == 1) {
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos simitel.");
        }
    }

    private void insertarReceta() {
        try {
            Integer numero = Integer.valueOf(txtNumero.getText().trim());
            Integer folio = Integer.valueOf(txtFolio.getText().trim());
            Object[] resultado = cnx.ejecutarCRUDSinSalida("insertar_receta", new Object[]{
                numero, txtFecha.getText().trim(), txtVigencia.getText().trim(),
                txtEstado.getText().trim(), folio
            });
            JOptionPane.showMessageDialog(this,
                    "Estado: " + resultado[0] + "\nMensaje: " + resultado[1]);
            if (Integer.parseInt(resultado[0].toString()) == 1) {
                actualizarTabla();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El folio debe ser un número entero.");
        }
    }

    private void actualizarTabla() {
        cnx.entablar(consulta, tabla);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FProcedimientos().setVisible(true));
    }
}
