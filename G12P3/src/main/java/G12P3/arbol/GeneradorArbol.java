package G12P3.arbol;

import G12P3.evaluacion.Operador;
import G12P3.evaluacion.TipoAccion;
import G12P3.evaluacion.TipoSensor;
import java.util.Random;

public class GeneradorArbol {

    private static final int[] UMBRALES = { 10, 50, 100 };
    private static final TipoSensor[] SENSORES = TipoSensor.values();
    private static final Operador[] OPERADORES = Operador.values();

    // genera un arbol usando Ramped Half-and-Half.
    // la mitad de la poblacion usa Full, la otra mitad usa Grow.
    // la profundidad se distribuye uniformemente entre 2 y profMax.
    public static NodoAst rampedHalfAndHalf(int profMax, Random rnd) {
        // profundidad aleatoria entre 2 y profMax
        int prof = rnd.nextInt(profMax - 1) + 2; // [2, profMax]

        if (rnd.nextBoolean()) return crearFull(0, prof, rnd);
        else return crearGrow(0, prof, rnd);
    }

    // grow: los nodos se eligen entre terminales y funciones hasta profMax.
    // en profMax se fuerza un terminal.
    // AVANZAR tiene el doble de probabilidad que GIRAR_IZQ y GIRAR_DER.
    public static NodoAst crearGrow(int profActual, int profMax, Random rnd) {
        if (profActual >= profMax) return crearTerminal(rnd);

        // terminal, condicional, bloque
        int tipo = rnd.nextInt(3);

        if (tipo == 0) return crearTerminal(rnd);
        else if (tipo == 1) return crearCondicional(
            profActual,
            profMax,
            rnd,
            false
        );
        else return crearBloque(profActual, profMax, rnd, false);
    }

    // full: solo nodos funcion hasta profMax-1, terminales en profMax.
    public static NodoAst crearFull(int profActual, int profMax, Random rnd) {
        if (profActual >= profMax) return crearTerminal(rnd);

        // solo nodos funcion: condicional o bloque
        if (rnd.nextBoolean()) return crearCondicional(
            profActual,
            profMax,
            rnd,
            true
        );
        else return crearBloque(profActual, profMax, rnd, true);
    }

    // crea un terminal con sesgo: AVANZAR tiene el doble de probabilidad.
    // probabilidades: AVANZAR=50%, GIRAR_IZQ=25%, GIRAR_DER=25%
    private static NodoAccion crearTerminal(Random rnd) {
        int r = rnd.nextInt(4); // 0,1 -> AVANZAR, 2 -> GIRAR_IZQ, 3 -> GIRAR_DER
        TipoAccion accion;
        if (r <= 1) accion = TipoAccion.AVANZAR;
        else if (r == 2) accion = TipoAccion.GIRAR_IZQ;
        else accion = TipoAccion.GIRAR_DER;

        return new NodoAccion(accion);
    }

    private static NodoCondicional crearCondicional(
        int profActual,
        int profMax,
        Random rnd,
        boolean full
    ) {
        TipoSensor sensor = SENSORES[rnd.nextInt(SENSORES.length)];
        Operador op = OPERADORES[rnd.nextInt(OPERADORES.length)];
        int umbral = UMBRALES[rnd.nextInt(UMBRALES.length)];

        NodoCondicional nodo = new NodoCondicional(sensor, op, umbral);

        if (full) {
            nodo.setHijoTrue(crearFull(profActual + 1, profMax, rnd));
            nodo.setHijoFalse(crearFull(profActual + 1, profMax, rnd));
        } else {
            nodo.setHijoTrue(crearGrow(profActual + 1, profMax, rnd));
            nodo.setHijoFalse(crearGrow(profActual + 1, profMax, rnd));
        }
        return nodo;
    }

    private static NodoBloque crearBloque(
        int profActual,
        int profMax,
        Random rnd,
        boolean full
    ) {
        NodoBloque bloque = new NodoBloque();
        int numHijos = rnd.nextInt(2) + 2; // 2 o 3 hijos
        for (int i = 0; i < numHijos; i++) {
            if (full) bloque.añadirHijo(
                crearFull(profActual + 1, profMax, rnd)
            );
            else bloque.añadirHijo(crearGrow(profActual + 1, profMax, rnd));
        }
        return bloque;
    }
}
