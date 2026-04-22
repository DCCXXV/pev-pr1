package G12P3.ag;

import G12P3.ag.cruce.Cruce;
import G12P3.ag.cruce.CruceSubArboles;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ag.seleccion.Seleccion;
import G12P3.ag.seleccion.Torneo;
import G12P3.arbol.GeneradorArbol;
import G12P3.arbol.NodoAst;
import G12P3.ui.Grafica;
import G12P3.ui.PanelFenotipo;
import G12P3.ui.Tablero;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Simulator {

    private final int maxGeneraciones;
    private final int tamPoblacion;
    private final double probCruce;
    private final double probMutacion;
    private final double elitismo;
    private final int profMaxInicial;
    private final double coefBloat;
    private final long[] semillasMapas;

    private final Random rnd;
    private final Seleccion seleccion;
    private final Cruce cruce;
    private final Mutacion mutacion;

    private Cromosoma[] poblacion;

    public Simulator(
        int maxGeneraciones,
        int tamPoblacion,
        double probCruce,
        double probMutacion,
        double elitismo,
        int profMaxInicial,
        double coefBloat,
        long semilla,
        Mutacion mutacion,
        Tablero tablero,
        Grafica grafica,
        PanelFenotipo fenotipo
    ) {
        this.maxGeneraciones = maxGeneraciones;
        this.tamPoblacion = tamPoblacion;
        this.probCruce = probCruce;
        this.probMutacion = probMutacion;
        this.elitismo = elitismo;
        this.profMaxInicial = profMaxInicial;
        this.coefBloat = coefBloat;
        this.rnd = new Random(semilla);
        // 3 mapas derivados de la semilla para evaluar el fitness
        this.semillasMapas = new long[] { semilla, semilla + 1, semilla + 2 };
        this.mutacion = mutacion;
        this.cruce = new CruceSubArboles(rnd);
        this.seleccion = new Torneo(rnd);

        iniciarPoblacion();
        evaluarPoblacion();

        Cromosoma mejorAbs = null;
        double mejorFitnessAbs = -Double.MAX_VALUE;
        for (Cromosoma c : poblacion) {
            if (c.fitness > mejorFitnessAbs) {
                mejorFitnessAbs = c.fitness;
                mejorAbs = c.clonar();
            }
        }

        // reevaluamos el mejor absoluto para rellenar sus campos de visualizacion
        mejorAbs.evaluar(semillasMapas, coefBloat);

        for (
            int gen = 0;
            gen < maxGeneraciones && !Thread.currentThread().isInterrupted();
            gen++
        ) {
            Cromosoma[] elite = generarElite();
            poblacion = seleccion.seleccionar(poblacion);
            cruzar();
            mutar();
            introducirElite(elite);
            evaluarPoblacion();

            double mejorGen = -Double.MAX_VALUE;
            double suma = 0;
            for (Cromosoma c : poblacion) {
                suma += c.fitness;
                if (c.fitness > mejorGen) mejorGen = c.fitness;
                if (c.fitness > mejorFitnessAbs) {
                    mejorFitnessAbs = c.fitness;
                    mejorAbs = c.clonar();
                    mejorAbs.evaluar(semillasMapas, coefBloat);
                }
            }

            double media = suma / tamPoblacion;
            grafica.actualizarGrafica(gen, mejorGen, mejorFitnessAbs, media);
            tablero.setMejor(mejorAbs);
            fenotipo.setMejor(mejorAbs);
            System.out.println(
                gen +
                    " | " +
                    maxGeneraciones +
                    " | " +
                    mejorFitnessAbs +
                    " | nodos=" +
                    mejorAbs.nodos
            );
        }
    }

    private void iniciarPoblacion() {
        poblacion = new Cromosoma[tamPoblacion];
        NodoAst[] arboles = GeneradorArbol.rampedHalfAndHalf(
            tamPoblacion,
            profMaxInicial,
            rnd
        );
        for (int i = 0; i < tamPoblacion; i++) poblacion[i] = new Cromosoma(
            arboles[i]
        );
    }

    private void evaluarPoblacion() {
        for (Cromosoma c : poblacion) c.evaluar(semillasMapas, coefBloat);
    }

    private Cromosoma[] generarElite() {
        int n = (int) (elitismo * tamPoblacion);
        if (n <= 0) return new Cromosoma[0];
        Cromosoma[] orden = poblacion.clone();
        Arrays.sort(orden, (a, b) -> Double.compare(b.fitness, a.fitness));
        Cromosoma[] elite = new Cromosoma[n];
        for (int i = 0; i < n; i++) elite[i] = orden[i].clonar();
        return elite;
    }

    private void introducirElite(Cromosoma[] elite) {
        if (elite.length == 0) return;
        Integer[] idx = new Integer[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) ->
            Double.compare(poblacion[a].fitness, poblacion[b].fitness)
        );
        for (int i = 0; i < elite.length; i++) poblacion[idx[i]] = elite[i];
    }

    private void cruzar() {
        List<Integer> seleccionados = new ArrayList<>();
        for (int i = 0; i < tamPoblacion; i++) {
            if (rnd.nextDouble() < probCruce) seleccionados.add(i);
        }
        if (seleccionados.size() % 2 != 0) seleccionados.remove(
            seleccionados.size() - 1
        );
        for (int i = 0; i < seleccionados.size(); i += 2) {
            cruce.cruzar(
                poblacion[seleccionados.get(i)],
                poblacion[seleccionados.get(i + 1)]
            );
        }
    }

    private void mutar() {
        for (int i = 0; i < tamPoblacion; i++) {
            if (rnd.nextDouble() < probMutacion) mutacion.mutar(poblacion[i]);
        }
    }
}
