package org.pr1.cromosomas;

import java.util.Random;
import org.pr1.Scene;

public class CromosomaReal {

    private double[] genes;
    private int numCamaras;
    private double rangoVision;
    private double anguloApertura;
    private boolean ponderado;
    private Scene scene;

    private static final int GENES_POR_CAMARA = 3; // x, y, theta

    private Random rng = new Random();

    public CromosomaReal(
        int numCamaras,
        Scene scene,
        double rangoVision,
        double anguloApertura,
    ) {
        this.numCamaras = numCamaras;
        this.scene = scene;
        this.rangoVision = rangoVision;
        this.anguloApertura = anguloApertura;
        this.ponderado = ponderado;
        this.genes = new double[numCamaras * GENES_POR_CAMARA];
        randomInit();
    }

    public double[] getGenes() { return genes; }
    public int getNumCamaras() { return numCamaras; }
    public double getRangoVision() { return rangoVision; }
    public double getAnguloApertura() { return anguloApertura; }
    public boolean isPonderado() { return ponderado; }
    public Scene getScene() { return scene; }

    private void randomInit() {
        for (int k = 0; k < numCamaras; k++) {
            int base = k * GENES_POR_CAMARA;
            genes[base] = rng.nextDouble() * scene.getCols();
            genes[base + 1] = rng.nextDouble() * scene.getRows();
            genes[base + 2] = rng.nextDouble() * 360.0;
        }
    }
}
