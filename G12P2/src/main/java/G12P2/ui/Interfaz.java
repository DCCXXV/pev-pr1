package G12P2.ui;

import G12P2.Main;
import G12P2.Mapas;
import G12P2.Scene;
import G12P2.cromosomas.CromosomaDrones;
import G12P2.evaluacion.EvaluacionDrones;
import G12P2.evaluacion.resEvaluacion;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class Interfaz extends JFrame {

    public Interfaz() {
        setTitle("Configuración Algoritmo Genético");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        Tablero tablero = new Tablero();

        //panel de la grafica
        Grafica grafica = new Grafica(
            new int[]{1,2,3},
            new int[]{1,2,3},
            new double[]{1,2,3}
        );

        Configuracion configuracion = new Configuracion(tablero, grafica);

        //se meten los paneles en el panel principal
        mainPanel.add(configuracion);
        mainPanel.add(tablero);
        mainPanel.add(grafica);

        //se mete el panel princiapl
        add(mainPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Interfaz().setVisible(true));
    }
}