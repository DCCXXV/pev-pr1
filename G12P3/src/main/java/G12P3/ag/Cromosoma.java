package G12P3.ag;

import G12P3.arbol.NodoAst;
import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.MapaLunar;
import G12P3.evaluacion.ResultadoSimulacion;

import java.util.List;
import java.util.Random;

public class Cromosoma {

    public NodoAst arbol;

    public double fitness;          // fitness final (con penalizacion de bloat)
    public double fitnessBase;      // media del fitness base en los 3 mapas
    public int nodos;
    public int profundidadMax;

    // detalles de cada mapa evaluado (para poder visualizar los 3 recorridos)
    public DatosMapa[] datosPorMapa;

    public static class DatosMapa {
        public int[][] mapaInicial;
        public int[][] mapaFinal;
        public boolean[][] visitado;
        public int posFila;
        public int posCol;
        public int direccion;
        public double energiaRestante;
        public int muestrasRecogidas;
        public int casillasExploradas;
        public int pisadasArena;
        public int colisiones;
        public double fitnessBase;
    }

    public Cromosoma(NodoAst arbol) {
        this.arbol = arbol;
    }

    public NodoAst getNodoAleatorio(Random rnd) {
        List<NodoAst> nodos = arbol.obtenerTodosNodos();
        return nodos.get(rnd.nextInt(nodos.size()));
    }

    public Cromosoma clonar() {
        Cromosoma copia = new Cromosoma(arbol.clonar());
        copia.fitness = this.fitness;
        copia.fitnessBase = this.fitnessBase;
        copia.nodos = this.nodos;
        copia.profundidadMax = this.profundidadMax;
        copia.datosPorMapa = this.datosPorMapa;
        return copia;
    }

    // evalua el arbol en N mapas distintos para evitar dependencia de un unico escenario
    public double evaluar(long[] semillas, double coefBloat) {
        double suma = 0;
        this.datosPorMapa = new DatosMapa[semillas.length];
        for (int i = 0; i < semillas.length; i++) {
            int[][] mapa = MapaLunar.generar(semillas[i]);
            Contexto ctx = new Contexto(mapa);
            ResultadoSimulacion res = ctx.simular(arbol);
            double fb = res.calcularFitnessBase();
            suma += fb;

            // guardamos el estado de cada mapa para la visualizacion
            DatosMapa dm = new DatosMapa();
            dm.mapaInicial = mapa;
            dm.mapaFinal = ctx.mapa;
            dm.visitado = ctx.getVisitado();
            dm.posFila = ctx.y;
            dm.posCol = ctx.x;
            dm.direccion = ctx.direccion;
            dm.energiaRestante = ctx.energia;
            dm.muestrasRecogidas = ctx.muestrasRecogidas;
            dm.casillasExploradas = ctx.casillasExploradas;
            dm.pisadasArena = ctx.pisadasArena;
            dm.colisiones = ctx.colisiones;
            dm.fitnessBase = fb;
            this.datosPorMapa[i] = dm;
        }
        this.fitnessBase = suma / semillas.length;
        this.nodos = arbol.contarNodos();
        this.profundidadMax = arbol.calcularProfundidadMaxima();
        // penalizacion por bloat: arboles grandes reciben menos fitness para frenar el crecimiento
        this.fitness = this.fitnessBase - (this.nodos * coefBloat);
        return this.fitness;
    }
}
