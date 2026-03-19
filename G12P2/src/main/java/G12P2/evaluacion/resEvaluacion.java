package G12P2.evaluacion;

import java.util.List;

public class resEvaluacion {
    private int fitness;
    private List<List<int[]>> caminos;

    public resEvaluacion(int fitness, List<List<int[]>> caminos) {
        this.fitness = fitness;
        this.caminos = caminos;
    }

    public int getFitness() {
        return fitness;
    }
    public List<List<int[]>> getCaminos() {
        return caminos;
    }
}
