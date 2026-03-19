package G12P2.evaluacion;

import G12P2.cromosomas.CromosomaDrones;

import java.util.List;

public class ResEvaluacion {
    private double fitness;
    private double[] tiemposDrones;
    private List<List<int[]>> caminos;
    private CromosomaDrones cromosoma;

    public ResEvaluacion(double fitness, double[] tiemposDrones, List<List<int[]>> caminos, CromosomaDrones cromosoma) {
        this.fitness = fitness;
        this.tiemposDrones = tiemposDrones;
        this.caminos = caminos;
        this.cromosoma = cromosoma;
    }

    public double getFitness() {
        return fitness;
    }
    public double[] getTiemposDrones() {
        return tiemposDrones;
    }
    public List<List<int[]>> getCaminos() {
        return caminos;
    }
    public CromosomaDrones getCromosoma() {return cromosoma;}
}
