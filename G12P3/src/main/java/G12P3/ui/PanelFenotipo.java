package G12P3.ui;

import G12P3.ag.Cromosoma;
import java.awt.*;
import javax.swing.*;

public class PanelFenotipo extends JPanel {

    private final JTextArea texto;
    private final JLabel resumen;

    public PanelFenotipo() {
        setLayout(new BorderLayout());
        setBorder(
            BorderFactory.createTitledBorder(
                "Estrategia del mejor individuo global"
            )
        );

        resumen = new JLabel(" ");
        resumen.setFont(new Font("Arial", Font.BOLD, 13));
        resumen.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(resumen, BorderLayout.NORTH);

        texto = new JTextArea(10, 80);
        texto.setEditable(false);
        texto.setFont(new Font("Monospaced", Font.PLAIN, 13));
        texto.setBackground(new Color(250, 250, 245));

        JScrollPane scroll = new JScrollPane(texto);
        scroll.setPreferredSize(new Dimension(1000, 200));
        add(scroll, BorderLayout.CENTER);
    }

    public void setMejor(Cromosoma c) {
        SwingUtilities.invokeLater(() -> {
            resumen.setText(
                String.format(
                    "Fitness: %.2f   |   Fitness base: %.2f   |   Nodos AST: %d",
                    c.fitness,
                    c.fitnessBase,
                    c.nodos
                )
            );
            texto.setText(c.arbol.toString());
            texto.setCaretPosition(0);
        });
    }

    public void limpiar() {
        SwingUtilities.invokeLater(() -> {
            resumen.setText(" ");
            texto.setText("");
        });
    }
}
