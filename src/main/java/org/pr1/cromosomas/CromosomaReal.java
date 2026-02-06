package org.pr1.cromosomas;

import java.util.Random;

public class CromosomaReal {

    private double[] genes;
    private int numCamaras;
    private int rows;
    private int cols;
    private double rangoVision;
    private double anguloApertura;
    private boolean ponderado;
    private int[][] grid;

    private static final int GENES_POR_CAMARA = 3; // x, y, theta

    private Random rng = new Random();

    public CromosomaReal(
        int numCamaras,
        int rows,
        int cols,
        double rangoVision,
        double anguloApertura,
        int[][] grid,
        boolean ponderado
    ) {
        this.numCamaras = numCamaras;
        this.rows = rows;
        this.cols = cols;
        this.rangoVision = rangoVision;
        this.anguloApertura = anguloApertura;
        this.grid = grid;
        this.ponderado = ponderado;
        this.genes = new double[numCamaras * GENES_POR_CAMARA];
        randomInit();
        // evaluar();
    }

    private void randomInit() {
        for (int k = 0; k < numCamaras; k++) {
            int base = k * GENES_POR_CAMARA;
            genes[base] = rng.nextDouble() * cols;
            genes[base + 1] = rng.nextDouble() * rows;
            genes[base + 2] = rng.nextDouble() * 360.0;
        }
    }
}
