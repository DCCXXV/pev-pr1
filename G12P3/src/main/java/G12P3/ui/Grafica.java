package G12P3.ui;

import java.awt.*;
import javax.swing.*;
import org.math.plot.Plot2DPanel;
import org.math.plot.plots.LinePlot;

public class Grafica extends JPanel {

    private final Plot2DPanel plot;

    private int generaciones;
    private double min;
    private double max;

    private LinePlot plotMejorGen;
    private LinePlot plotMejorAbs;
    private LinePlot plotMedia;

    private double[][] mejoresPorGeneracion;
    private double[][] mejoresAbsolutos;
    private double[][] mediaPorGeneracion;

    public Grafica() {
        plot = new Plot2DPanel();
        plot.addLegend("SOUTH");
        plot.setAxisLabels("Generación", "Fitness");

        setLayout(new BorderLayout());
        add(plot, BorderLayout.CENTER);
    }

    public void setGeneraciones(int generaciones) {
        plot.removeAllPlots();
        this.generaciones = generaciones;
        this.min = 0;
        this.max = 0;

        mejoresPorGeneracion = new double[generaciones][2];
        mejoresAbsolutos = new double[generaciones][2];
        mediaPorGeneracion = new double[generaciones][2];

        for (int i = 0; i < generaciones; i++) {
            mejoresPorGeneracion[i][0] = i;
            mejoresAbsolutos[i][0] = i;
            mediaPorGeneracion[i][0] = i;
        }

        plotMejorGen = new LinePlot(
            "Mejor por generación",
            Color.RED,
            null,
            mejoresPorGeneracion
        );
        plotMejorAbs = new LinePlot(
            "Mejor absoluto",
            Color.BLUE,
            null,
            mejoresAbsolutos
        );
        plotMedia = new LinePlot(
            "Media por generación",
            Color.GREEN,
            null,
            mediaPorGeneracion
        );

        plot.setFixedBounds(0, 0, Math.max(1, generaciones - 1));
        plot.addPlot(plotMejorGen);
        plot.addPlot(plotMejorAbs);
        plot.addPlot(plotMedia);
    }

    public void actualizarGrafica(
        int generacion,
        double mejorGen,
        double mejorAbs,
        double media
    ) {
        if (mejorAbs > max) max = mejorAbs;
        if (media < min) min = media;
        if (mejorGen < min) min = mejorGen;

        mejoresPorGeneracion[generacion][1] = mejorGen;
        mejoresAbsolutos[generacion][1] = mejorAbs;
        mediaPorGeneracion[generacion][1] = media;

        // rellenamos el resto con el ultimo valor para no tener cero plano
        for (int i = generacion + 1; i < generaciones; i++) {
            mejoresPorGeneracion[i][1] = mejorGen;
            mejoresAbsolutos[i][1] = mejorAbs;
            mediaPorGeneracion[i][1] = media;
        }

        plotMejorGen.setData(mejoresPorGeneracion);
        plotMejorAbs.setData(mejoresAbsolutos);
        plotMedia.setData(mediaPorGeneracion);

        SwingUtilities.invokeLater(() -> {
            plot.setFixedBounds(0, 0, Math.max(1, generaciones - 1));
            if (max > min) plot.setFixedBounds(1, min, max);
            plot.repaint();
        });
    }
}
