package org.pr1.ui;

import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;

public class Grafica extends JPanel {
    public Grafica(int[] mejoresPorGeneracion, int[] mejoresAbsolutos, double[] mediaPorGeneracion) {

        int generaciones = mejoresPorGeneracion.length;

        // Eje X: generaciones
        double[] x = new double[generaciones];
        for (int i = 0; i < generaciones; i++) {
            x[i] = i;
        }

        // Convertir int[] a double[]
        double[] mejoresGen = toDouble(mejoresPorGeneracion);
        double[] mejoresAbs = toDouble(mejoresAbsolutos);

        // Crear el panel de gráfica
        Plot2DPanel plot = new Plot2DPanel();
        plot.addLegend("SOUTH");

        // Añadir curvas
        plot.addLinePlot("Mejor por generación", x, mejoresGen);
        plot.addLinePlot("Mejor absoluto", x, mejoresAbs);
        plot.addLinePlot("Media por generación", x, mediaPorGeneracion);

        setLayout(new BorderLayout());
        add(plot, BorderLayout.CENTER);
        plot.setAxisLabels("Generacion", "Fittness");

        add(plot);
    }

    private double[] toDouble(int[] array) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }
}
