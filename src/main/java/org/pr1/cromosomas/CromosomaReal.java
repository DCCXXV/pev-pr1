package org.pr1.cromosomas;

import java.util.Random;
import org.pr1.Scene;
import org.pr1.evaluacion.EvaluacionConica;

public class CromosomaReal implements Cromosoma {

    private double[] genes;
    private int numCamaras;
    private double rangoVision;
    private double anguloApertura;
    private Scene scene;

    private static final int GENES_POR_CAMARA = 3; // x, y, theta

    private Random rng = new Random();

    public CromosomaReal(
        int numCamaras,
        Scene scene,
        double rangoVision,
        double anguloApertura
    ) {
        this.numCamaras = numCamaras;
        this.scene = scene;
        this.rangoVision = rangoVision;
        this.anguloApertura = anguloApertura;
        this.genes = new double[numCamaras * GENES_POR_CAMARA];
        randomInit();
    }

    private CromosomaReal(CromosomaReal other) {
        this.numCamaras = other.numCamaras;
        this.scene = other.scene;
        this.rangoVision = other.rangoVision;
        this.anguloApertura = other.anguloApertura;
        this.genes = other.genes.clone();
    }

    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
    }

    public int getNumCamaras() {
        return numCamaras;
    }

    public double getRangoVision() {
        return rangoVision;
    }

    public double getAnguloApertura() {
        return anguloApertura;
    }

    public Scene getScene() {
        return scene;
    }

    private void randomInit() {
        for (int k = 0; k < numCamaras; k++) {
            int base = k * GENES_POR_CAMARA;
            genes[base] = rng.nextDouble() * scene.getCols();
            genes[base + 1] = rng.nextDouble() * scene.getRows();
            genes[base + 2] = rng.nextDouble() * 360.0;
        }
    }

    public int evaluar() {
        return EvaluacionConica.evaluar(scene, this);
    }

    @Override
    public int[][] generarMapa() {
        return EvaluacionConica.generarMapa(scene, this);
    }

    public CromosomaReal copia() {
        return new CromosomaReal(this);
    }

    // devuelve el valor máximo válido del gen
    public double getMax(int gen) {
        int tipo = gen % GENES_POR_CAMARA;
        return switch (tipo) {
            case 0 -> scene.getCols();
            case 1 -> scene.getRows();
            case 2 -> 360.0;
            default -> throw new IllegalArgumentException();
        };
    }
}
