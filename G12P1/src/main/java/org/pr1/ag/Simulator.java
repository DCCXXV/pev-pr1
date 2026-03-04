package org.pr1.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
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

    private Cromosoma[] elite;
    private int[] fitnessElite;

    // resultados acumulados durante la ejec
    private SimulatorResult resultado;

    public Simulator(
        int maxGeneraciones,
        int tamPoblacion,
        double probCruce,
        double probMutacion,
        int elitismo,
        Seleccion seleccion,
        Cruce cruce,
        Mutacion mutacion,
        Supplier<Cromosoma> factoriaCromosomas
    ) {
        this.maxGeneraciones = maxGeneraciones;
        this.tamPoblacion = tamPoblacion;
        this.probCruce = probCruce;
        this.probMutacion = probMutacion;
        this.elitismo = elitismo;
        this.factoriaCromosomas = factoriaCromosomas;
        this.generacionActual = 0;

        this.seleccion = seleccion;
        this.cruce = cruce;
        this.mutacion = mutacion;

        // arrays para la gráfica de evolución
        int[] mejoresPorGeneracion = new int[maxGeneraciones];
        int[] mejoresAbsolutos = new int[maxGeneraciones];
        double[] mediaPorGeneracion = new double[maxGeneraciones];

        // inicializar población y evaluarla
        iniciarPoblacion();
        evaluarPoblacion();

        // mejor absoluto inicial (población 0, antes del primer bucle)
        int mejorFitnessAbsoluto = Integer.MIN_VALUE;
        Cromosoma mejorCromosomaAbsoluto = null;
        for (int i = 0; i < tamPoblacion; i++) {
            if (fitness[i] > mejorFitnessAbsoluto) {
                mejorFitnessAbsoluto = fitness[i];
                mejorCromosomaAbsoluto = poblacion[i].copia();
            }
        }

        while (this.generacionActual < this.maxGeneraciones) {
            generaElite();
            poblacion = seleccion.seleccionar(poblacion, fitness);
            cruce(probCruce);
            mutacion(probMutacion);
            introducirElite();
            evaluarPoblacion();

            // recoger stats de esta generacion
            int mejorGen = Integer.MIN_VALUE;
            double suma = 0;
            for (int i = 0; i < tamPoblacion; i++) {
                suma += fitness[i];
                if (fitness[i] > mejorGen) {
                    mejorGen = fitness[i];
                }
                if (fitness[i] > mejorFitnessAbsoluto) {
                    mejorFitnessAbsoluto = fitness[i];
                    mejorCromosomaAbsoluto = poblacion[i].copia();
                }
            }

            mejoresPorGeneracion[generacionActual] = mejorGen;
            mejoresAbsolutos[generacionActual] = mejorFitnessAbsoluto;
            mediaPorGeneracion[generacionActual] = suma / tamPoblacion;

            generacionActual++;
        }

        resultado = new SimulatorResult(
            mejoresPorGeneracion,
            mejoresAbsolutos,
            mediaPorGeneracion,
            mejorFitnessAbsoluto,
            mejorCromosomaAbsoluto
        );
    }

    // devuelve todos los resultados de la ejecución
    public SimulatorResult getResultado() {
        return resultado;
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

    private void cruce(double probCruce) {
        Random rng = new Random();
        ArrayList<Integer> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamPoblacion; i++) {
            if (rng.nextDouble() < probCruce) {
                seleccionados.add(i);
            }
        }
        if (seleccionados.size() % 2 != 0) {
            seleccionados.remove(seleccionados.size() - 1);
        }
        for (int i = 0; i < seleccionados.size(); i += 2) {
            cruce.cruzar(
                poblacion[seleccionados.get(i)],
                poblacion[seleccionados.get(i + 1)]
            );
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
