package G12P2.ag;

import G12P2.Scene;
import G12P2.ag.cruce.Cruce;
import G12P2.ag.mutacion.Mutacion;
import G12P2.ag.seleccion.Seleccion;
import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import G12P2.ui.Tablero;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Supplier;

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

    // TODO: interfaz !!
    private boolean memetico;

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
        Supplier<Cromosoma> factoriaCromosomas,
        boolean memetico,
        Tablero tablero
    ) {
        this.memetico = memetico;
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
            if (memetico) aplicarBusquedaLocal();
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

            //aqui manda el resultado a al tablero y la grafica
            Scene sceneMejor = mejorCromosomaAbsoluto.getScene();
            int numCamaras = sceneMejor.getNumCamaras();
            int[][] gridMejor = sceneMejor.getGrid();
            int[][] camarasMejor = sceneMejor.getPosCamaras();
            tablero.setTablero(gridMejor, camarasMejor, mejorFitnessAbsoluto, null);

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

    /**
     * aplica búsqueda local 2-Opt al 10% de la población
     *
     * por cada individuo seleccionado, prueba todas las inversiones posibles
     * de subrutas y como nos quedamos con la mejor se "desenredan" las rutas
     */
    private void aplicarBusquedaLocal() {
        for (int k = 0; k < tamPoblacion; k++) {
            if (Math.random() >= 0.10) continue; // 10%
            if (!(poblacion[k] instanceof CromosomaDrones)) continue;

            CromosomaDrones ind = (CromosomaDrones) poblacion[k];
            int fitActual = fitness[k];
            int[] genesActuales = ind.getGenes();

            // probar todas las inversiones posibles de tramos (i, j)
            for (int i = 0; i < genesActuales.length - 1; i++) {
                for (int j = i + 2; j < genesActuales.length; j++) {
                    // invertir el subtramo entre i+1 y j
                    int[] nuevaRuta = invertirSubRuta(genesActuales, i + 1, j);

                    // crear un clon con esa nueva ruta para evaluarla
                    CromosomaDrones clon = (CromosomaDrones) ind.copia();
                    clon.setGenes(nuevaRuta);

                    // si "desenredar" la ruta mejora el fitness, nos lo quedamos
                    int nuevaFit = clon.evaluar();
                    if (nuevaFit > fitActual) {
                        poblacion[k] = clon;
                        fitness[k] = nuevaFit;
                        ind = clon;
                        genesActuales = nuevaRuta;
                        fitActual = nuevaFit;
                    }
                }
            }
        }
    }

    // devuelve una copia de genes con el tramo [desde, hasta] invertido.
    private int[] invertirSubRuta(int[] genes, int desde, int hasta) {
        int[] nueva = genes.clone();
        int lo = desde,
            hi = hasta;
        while (lo < hi) {
            int tmp = nueva[lo];
            nueva[lo] = nueva[hi];
            nueva[hi] = tmp;
            lo++;
            hi--;
        }
        return nueva;
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
