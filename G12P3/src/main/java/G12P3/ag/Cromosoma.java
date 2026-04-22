package G12P3.ag;

import G12P3.arbol.NodoAst;
import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.MapaLunar;
import G12P3.evaluacion.ResultadoSimulacion;

import java.util.List;
import java.util.Random;

public class Cromosoma {

    public NodoAst arbol;

    // resultados de la ultima evaluacion
    public double fitness;          // fitness final (con penalizacion de bloat)
    public double fitnessBase;      // media del fitness base en los 3 mapas
    public int nodos;               // tamaño del AST

    // detalles del primer mapa (para visualizacion)
    public int[][] mapaMuestra;
    public boolean[][] visitadoMuestra;
    public int posFilaFinal;
    public int posColFinal;
    public int direccionFinal;
    public double energiaRestante;
    public int muestrasRecogidas;
    public int casillasExploradas;
    public int pisadasArena;
    public int colisiones;

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
        return copia;
    }

    // evalua el cromosoma en 3 mapas y calcula el fitness final.
    public double evaluar(long[] semillas, double coefBloat) {
        double suma = 0;
        for (int i = 0; i < semillas.length; i++) {
            int[][] mapa = MapaLunar.generar(semillas[i]);
            Contexto ctx = new Contexto(mapa);
            ResultadoSimulacion res = ctx.simular(arbol);
            suma += res.calcularFitnessBase();

            // guardamos el primer mapa para visualizar
            if (i == 0) {
                this.mapaMuestra = ctx.mapa;
                this.visitadoMuestra = ctx.getVisitado();
                this.posFilaFinal = ctx.y;
                this.posColFinal = ctx.x;
                this.direccionFinal = ctx.direccion;
                this.energiaRestante = ctx.energia;
                this.muestrasRecogidas = ctx.muestrasRecogidas;
                this.casillasExploradas = ctx.casillasExploradas;
                this.pisadasArena = ctx.pisadasArena;
                this.colisiones = ctx.colisiones;
            }
        }
        this.fitnessBase = suma / semillas.length;
        this.nodos = arbol.contarNodos();
        this.fitness = this.fitnessBase - (this.nodos * coefBloat);
        return this.fitness;
    }
}
