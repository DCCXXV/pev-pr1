package org.pr1.ui;

import org.math.plot.Plot2DPanel;

import javax.swing.*;
import java.awt.*;

public class Grafica extends JPanel {

    private Plot2DPanel plot;

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

        // Crear el panel de grafica
        plot = new Plot2DPanel();
        plot.addLegend("SOUTH");

        // Añadir curvas
        plot.addLinePlot("Mejor por generacion", x, mejoresGen);
        plot.addLinePlot("Mejor absoluto", x, mejoresAbs);
        plot.addLinePlot("Media por generacion", x, mediaPorGeneracion);

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

    public void actualizarGrafica(int[] nuevosMejoresGen, int[] nuevosMejoresAbs, double[] nuevaMediaGen) {
        // Convertir int[] a double[]
        double[] mejoresGen = toDouble(nuevosMejoresGen);
        double[] mejoresAbs = toDouble(nuevosMejoresAbs);

        int generaciones = nuevosMejoresGen.length;
        double[] x = new double[generaciones];
        for (int i = 0; i < generaciones; i++) x[i] = i;

        // Limpiar todas las curvas
        plot.removeAllPlots();

        // Añadir nuevas curvas
        plot.addLinePlot("Mejor por generacion", x, mejoresGen);
        plot.addLinePlot("Mejor absoluto", x, mejoresAbs);
        plot.addLinePlot("Media por generacion", x, nuevaMediaGen);

        // Forzar repaint para que se vea actualizado
        plot.repaint();
    }
}
