package G12P2.cromosomas;

import G12P2.Scene;
import G12P2.evaluacion.EvaluacionDrones;
import G12P2.evaluacion.ResEvaluacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CromosomaDrones implements Cromosoma {

    private ArrayList<Double> velocidadDronI = new ArrayList<Double>(
        // veloz, estándar, pesado, ágil, tanque
        java.util.Arrays.asList(1.5, 1.0, 0.7, 1.2, 0.5)
    );
    private int[] genes;
    private int D, C; //numero de drones y camaras
    private Scene scene;



    public CromosomaDrones(int D, Scene scene) {
        this.D = D;
        this.C = scene.getNumCamaras();
        this.scene = scene;
        this.genes = generarGenesAleatorios();
    }

    //copia
    public CromosomaDrones(CromosomaDrones other) {
        this.genes = other.getGenes().clone();
        this.D = other.getNumDrones();
        this.C = other.getNumCamaras();
        this.scene = other.getScene();
    }

    // tamaño = C + (D-1).
    private int[] generarGenesAleatorios() {
        List<Integer> valores = new ArrayList<>(C + D - 1);
        // camaras
        for (int i = 1; i <= C; i++) valores.add(i);
        // separadores
        for (int i = C + 1; i <= C + D - 1; i++) valores.add(i);

        Random rng = new Random();
        Collections.shuffle(valores, rng);

        return valores.stream().mapToInt(Integer::intValue).toArray();
    }

    public List<Double> getVelocidades(){
        return velocidadDronI;
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }

    public int getNumCamaras() {
        return C;
    }

    public int getNumDrones() {
        return D;
    }

    public Scene getScene() {
        return scene;
    }
    public int getRows() {
        return 0; //TODO
    }
    public int getCols() {
        return 0; //TODO
    }

    @Override
    public int[][] generarMapa() {
        return null; // TODO
    }

    @Override
    public ResEvaluacion evaluar() {
        return EvaluacionDrones.evaluar(this);
    }

    @Override
    public Cromosoma copia() {
        return new CromosomaDrones(this);
    }
}
