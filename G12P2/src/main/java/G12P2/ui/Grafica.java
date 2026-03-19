package G12P2.ui;

import org.math.plot.Plot2DPanel;
import org.math.plot.plots.LinePlot;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Grafica extends JPanel {

    private Plot2DPanel plot;

    int generaciones;
    LinePlot plotMejorGen;
    LinePlot plotMejorAbs;
    LinePlot plotMedia;

    double[][] mejoresPorGeneracion;
    double[][] mejoresAbsolutos;
    double[][] mediaPorGeneracion;

    public Grafica() {
        plot = new Plot2DPanel();
        plot.addLegend("SOUTH");
        plot.setAxisLabels("Generacion", "Fitness");

        setLayout(new BorderLayout());
        add(plot, BorderLayout.CENTER);
    }

    private double[] toDouble(int[] array) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public void setGeneraciones(int generaciones) {

        //limpio la tabla de los datos que tuviera antes
        plot.removeAllPlots();

        //numero de generaciones para fijar el eje x
        this.generaciones = generaciones;

        mejoresPorGeneracion = new double[generaciones][2];
        mejoresAbsolutos = new double[generaciones][2];
        mediaPorGeneracion = new double[generaciones][2];

        // eje X
        double[] x = new double[generaciones];
        for (int i = 0; i < generaciones; i++) x[i] = i;

        for (int i = 0; i < generaciones; i++) {
            mejoresPorGeneracion[i][0] = i;
            mejoresPorGeneracion[i][1] = 0;

            mejoresAbsolutos[i][0] = i;
            mejoresAbsolutos[i][1] = 0;

            mediaPorGeneracion[i][0] = i;
            mediaPorGeneracion[i][1] = 0;
        }

        plotMejorGen = new LinePlot("Mejor por generacion", Color.RED, null, mejoresPorGeneracion);
        plotMejorAbs = new LinePlot("Mejor absoluto", Color.BLUE, null, mejoresAbsolutos);
        plotMedia = new LinePlot("Media por generacion", Color.GREEN, null, mediaPorGeneracion);

        // fijar límites
        plot.setFixedBounds(0, 0, generaciones);

        plot.addPlot(plotMejorGen);
        plot.addPlot(plotMejorAbs);
        plot.addPlot(plotMedia);
    }

    public void actualizarGrafica(int generacion, double mejorGen, double mejorAbsoluto, double media) {

        // actualizar solo la Y (la X ya es i)
        mejoresPorGeneracion[generacion][1] = mejorGen;
        mejoresAbsolutos[generacion][1] = mejorAbsoluto;
        mediaPorGeneracion[generacion][1] = media;

        plotMejorGen.setData(mejoresPorGeneracion);
        plotMejorAbs.setData(mejoresAbsolutos);
        plotMedia.setData(mediaPorGeneracion);

        plot.setFixedBounds(0, 0, generaciones);
        plot.setFixedBounds(1, 0, 1500);
        plot.repaint();
        SwingUtilities.invokeLater(plot::repaint);
    }
}
