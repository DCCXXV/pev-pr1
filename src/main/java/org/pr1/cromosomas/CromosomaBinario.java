package org.pr1.cromosomas;

import org.pr1.Scene;
import org.pr1.evaluacion.EvaluacionBinaria;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

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

    public CromosomaBinario(int numCamaras, Scene scene) {
        //se coloca la escena y las camaras
        this.scene = scene;
        this.numCamaras = numCamaras;

        //se crea el array del cromosoma
        this.cromosoma = new boolean[numCamaras * 2][];

        //saca el numero de bits necesarios para la longitud x
        double logBase2X = Math.log(scene.getCols()) / Math.log(2);
        int bitsX = (int) Math.ceil(logBase2X);

        //saca el numero de bits necesarios para la longitud y
        double logBase2Y = Math.log(scene.getRows()) / Math.log(2);
        int bitsY = (int) Math.ceil(logBase2Y);

        //se itera creando los genes con sus respectivas cantidades de bits
        for (int i = 0; i < numCamaras; i++) {
            this.cromosoma[i * 2] = new boolean[bitsX];
            this.cromosoma[i * 2 + 1] = new boolean[bitsY];
        }

        //se vuelve a iterar por los genes para darles unos valores
        Random rand = new Random();
        for (int i = 0; i < numCamaras; i++) {
            int j = bitsX - 1;
            int numero = rand.nextInt(scene.getCols());
            while (numero > 0) {
                this.cromosoma[i * 2][j] = numero % 2 != 0;
                numero = numero / 2;
                j--;
            }
            j = bitsY - 1;
            numero = rand.nextInt(scene.getRows());
            while (numero > 0) {
                this.cromosoma[i * 2 + 1][j] = numero % 2 != 0;
                numero = numero / 2;
                j--;
            }
        }
    }

    public CromosomaBinario(CromosomaBinario c) {
        this.cromosoma = new boolean[c.getCromosoma().length][];
        for (int i = 0; i < c.getCromosoma().length; i++) {
            this.cromosoma[i] = new boolean[c.getCromosoma()[i].length];
            for (int j = 0; j < c.getCromosoma()[i].length; j++) {
                this.cromosoma[i][j] = c.getCromosoma()[i][j];
            }
        }
        this.scene = c.getScene();
        this.numCamaras = c.getNumCamaras();
    }

    public int evaluar() {
        return EvaluacionBinaria.evaluar(this);
    }

    @Override
    public Cromosoma copia() {
        return new CromosomaBinario(this);
    }

    @Override
    public int[][] generarMapa() {
//        //se saca el mapa
//        int[][] grid = this.scene.getGrid();
//
//        //matriz con el resultado
//        int[][] res = new int[grid.length][grid[0].length];
//
//        //se sacan las paredes
//        for (int i = 0; i < grid.length; i++) {
//            for  (int j = 0; j < grid[0].length; j++) {
//                if (!this.scene.isPonderado() && grid[i][j] == 1)
//                    res[i][j] = 3;
//                else if (this.scene.isPonderado() && grid[i][j] == 0)
//                    res[i][j] = 3;
//            }
//        }
//
//        return res;

        return EvaluacionBinaria.generarMapa(this);
    }

    @Override
    public int getRows() {
        return 0;
    }

    @Override
    public int getCols() {
        return 0;
    }

    public boolean[][] getGenes() {
        return cromosoma;
    }

    public void setGenes(boolean[][] genes) {
        this.cromosoma = genes;
    }

    public boolean[][] getCromosoma() {return cromosoma;}
    public Scene getScene() {return scene;}
    public int getNumCamaras() {return numCamaras;}
}
