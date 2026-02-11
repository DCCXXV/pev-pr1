package org.pr1.ag;

import org.pr1.ag.cruce.Cruce;
import org.pr1.ag.mutacion.Mutacion;
import org.pr1.ag.seleccion.Seleccion;

public class Simulator {

    private int maxGeneraciones;
    private int generacionActual;

    private Seleccion seleccion;
    private Cruce cruce;
    private Mutacion mutacion;

    private double probCruce;
    private double probMutacion;
    private int elitismo;

    public Simulator(int maxGeneraciones) {
        this.maxGeneraciones = maxGeneraciones;
        this.generacionActual = 0;

        // iniciarPoblacion();
        // evaluarPoblacion();
        // while(this.generacionActual < this.maxGeneraciones) {
        //     generaElite();
        //     seleccion.seleccionar(poblacion, fitness);
        //     cruce con probCruce
        //     mutacion con probMutacion
        //     introducirElite();
        //     evaluarPoblacion();
        //     generacionActual++;
        // }
    }
}
