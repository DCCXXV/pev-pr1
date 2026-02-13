package org.pr1.cromosomas;

import org.pr1.Scene;
import org.pr1.evaluacion.EvaluacionBinaria;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CromosomaBinario implements Cromosoma {
    /*
    Presion selectiva en las graficas
    Puede haber fitnes negativos esos datos no se cogen con la ruleta
    es decir se ponen a 0 por como funciona la ruleta
    Las posiciones fuera del tablero se arreglan y las columnas se penalizan
     */
    private boolean[][] cromosoma;
    private Scene scene;
    private int numCamaras;
    private int evaluacion;

    CromosomaBinario(int numCamaras, Scene scene) {
        //se coloca la escena y las camaras
        this.scene = scene;
        this.numCamaras = numCamaras;

        //se crea el array del cromosoma
        boolean[][] matriz = new boolean[numCamaras * 2][];

        //saca el numero de bits necesarios para la longitud x
        double logBase2X = Math.log(scene.getCols()) / Math.log(2);
        int bitsX = (int) Math.ceil(logBase2X);

        //saca el numero de bits necesarios para la longitud y
        double logBase2Y = Math.log(scene.getRows()) / Math.log(2);
        int bitsY = (int) Math.ceil(logBase2Y);

        //se itera creando los genes con sus respectivas cantidades de bits
        for (int i = 0; i <= numCamaras; i++) {
            this.cromosoma[i * 2] = new boolean[bitsX];
            this.cromosoma[i * 2 + 1] = new boolean[bitsY];
        }

        this.evaluacion = EvaluacionBinaria.evaluar(this);
    }

    public int evaluar() {return evaluacion;}

    public boolean[][] getCromosoma() {return cromosoma;}
    public Scene getScene() {return scene;}
    public int getNumCamaras() {return numCamaras;}
}
