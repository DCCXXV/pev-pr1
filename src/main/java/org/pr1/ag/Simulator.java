package org.pr1.ag;

import java.util.Arrays;
import java.util.function.Supplier;
import org.pr1.ag.cruce.Cruce;
import org.pr1.ag.mutacion.Mutacion;
import org.pr1.ag.seleccion.Seleccion;
import org.pr1.cromosomas.Cromosoma;

public class Simulator {

    private int maxGeneraciones;
    private int generacionActual;

    //private Seleccion seleccion;
    private Cruce cruce;
    private Mutacion mutacion;

    private double probCruce;
    private double probMutacion;
    private int elitismo;

    private Cromosoma[] poblacion;
    private int tamPoblacion;
    private Supplier<Cromosoma> factoriaCromosomas;
    private int[] fitness;

    private Cromosoma[] elite;
    private int[] fitnessElite;

    public Simulator(
        int maxGeneraciones,
        int tamPoblacion,
        Seleccion seleccion,
        Mutacion mutacion,
        Supplier<Cromosoma> factoriaCromosomas
    ) {
        this.maxGeneraciones = maxGeneraciones;
        this.tamPoblacion = tamPoblacion;
        this.factoriaCromosomas = factoriaCromosomas;
        this.generacionActual = 0;

        this.mutacion = mutacion;

        iniciarPoblacion();
        evaluarPoblacion();
        while (this.generacionActual < this.maxGeneraciones) {
            generaElite();
            poblacion = seleccion.seleccionar(poblacion, fitness);
            //     cruce con probCruce
            mutacion(probMutacion);
            introducirElite();
            evaluarPoblacion();
            //     generacionActual++;
        }
    }

    private void iniciarPoblacion() {
        poblacion = new Cromosoma[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            poblacion[i] = factoriaCromosomas.get();
        }
    }

    private void evaluarPoblacion() {
        fitness = new int[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) {
            fitness[i] = poblacion[i].evaluar();
        }
    }

    private void generaElite() {
        int[][] paresFitness = new int[tamPoblacion][2];
        for (int i = 0; i < tamPoblacion; i++) {
            paresFitness[i][0] = i;
            paresFitness[i][1] = fitness[i];
        }
        Arrays.sort(paresFitness, (a, b) -> Integer.compare(b[1], a[1]));

        elite = new Cromosoma[elitismo];
        fitnessElite = new int[elitismo];
        for (int i = 0; i < elitismo; i++) {
            int idx = paresFitness[i][0];
            elite[i] = poblacion[idx].copia();
            fitnessElite[i] = fitness[idx];
        }
    }

    private void mutacion(double probMutacion) {
        for (int i = 0; i < tamPoblacion; i++) {
            mutacion.mutar(poblacion[i], probMutacion);
        }
    }

    private void introducirElite() {
        int[][] paresFitness = new int[tamPoblacion][2];
        for (int i = 0; i < tamPoblacion; i++) {
            paresFitness[i][0] = i;
            paresFitness[i][1] = fitness[i];
        }
        Arrays.sort(paresFitness, (a, b) -> Integer.compare(a[1], b[1]));

        for (int i = 0; i < elitismo; i++) {
            int idx = paresFitness[i][0];
            poblacion[idx] = elite[i];
            fitness[idx] = fitnessElite[i];
        }
    }
}
