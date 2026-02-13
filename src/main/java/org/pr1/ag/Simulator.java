package org.pr1.ag;

import java.util.function.Supplier;
import org.pr1.ag.cruce.Cruce;
import org.pr1.ag.mutacion.Mutacion;
import org.pr1.ag.seleccion.Seleccion;
import org.pr1.cromosomas.Cromosoma;

public class Simulator {

    private int maxGeneraciones;
    private int generacionActual;

    private Seleccion seleccion;
    private Cruce cruce;
    private Mutacion mutacion;

    private double probCruce;
    private double probMutacion;
    private int elitismo;

    private Cromosoma[] poblacion;
    private int tamPoblacion;
    private Supplier<Cromosoma> factoriaCromosomas;
    private int[] fitness;

    public Simulator(
        int maxGeneraciones,
        int tamPoblacion,
        Supplier<Cromosoma> factoriaCromosomas
    ) {
        this.maxGeneraciones = maxGeneraciones;
        this.tamPoblacion = tamPoblacion;
        this.factoriaCromosomas = factoriaCromosomas;
        this.generacionActual = 0;

        iniciarPoblacion();
        evaluarPoblacion();
        while (this.generacionActual < this.maxGeneraciones) {
            //     generaElite();
            //     seleccion.seleccionar(poblacion, fitness);
            //     cruce con probCruce
            //     mutacion con probMutacion
            //     introducirElite();
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
}
