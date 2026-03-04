package org.pr1.ag;

import org.pr1.cromosomas.Cromosoma;

/**
 * contiene todos los resultados de una ejecución del algoritmo genético.
 *
 * arrays de gráfica (índice = generación, desde 0 hasta maxGeneraciones-1):
 *   mejoresPorGeneracion  -> mejor fitness en esa generación
 *   mejoresAbsolutos      -> mejor fitness acumulado hasta esa generación
 *   mediaPorGeneracion    -> fitness medio de la población en esa generación
 */
public class SimulatorResult {

    private final int[] mejoresPorGeneracion;
    private final int[] mejoresAbsolutos;
    private final double[] mediaPorGeneracion;

    private final int mejorFitness;
    private final Cromosoma mejorCromosoma;

    private final int rows;
    private final int cols;

    public SimulatorResult(
        int[] mejoresPorGeneracion,
        int[] mejoresAbsolutos,
        double[] mediaPorGeneracion,
        int mejorFitness,
        Cromosoma mejorCromosoma
    ) {
        this.mejoresPorGeneracion = mejoresPorGeneracion;
        this.mejoresAbsolutos = mejoresAbsolutos;
        this.mediaPorGeneracion = mediaPorGeneracion;
        this.mejorFitness = mejorFitness;
        this.mejorCromosoma = mejorCromosoma;
        this.rows = mejorCromosoma.getRows();
        this.cols = mejorCromosoma.getCols();
    }

    // mejor fitness de la población en cada generación
    public int[] getMejoresPorGeneracion() {
        return mejoresPorGeneracion;
    }

    // mejor fitness absoluto acumulado hasta cada generación
    public int[] getMejoresAbsolutos() {
        return mejoresAbsolutos;
    }

    // fitness medio de la población en cada generación
    public double[] getMediaPorGeneracion() {
        return mediaPorGeneracion;
    }

    // valor óptimo obtenido en toda la ejecución
    public int getMejorFitness() {
        return mejorFitness;
    }

    // cromosoma con la mejor solución encontrada
    public Cromosoma getMejorCromosoma() {
        return mejorCromosoma;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * mapa visual de la mejor solución:
     *   0 = espacio vacío (no cubierto)
     *   1 = cámara
     *   2 = celda cubierta por alguna cámara
     *   3 = pared
     */
    public int[][] getMapa() {
        return mejorCromosoma.generarMapa();
    }
}
