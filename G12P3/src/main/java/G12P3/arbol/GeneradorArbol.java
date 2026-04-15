package G12P3.arbol;

import G12P3.evaluacion.TipoAccion;
import G12P3.evaluacion.TipoSensor;

import java.util.Random;

public class GeneradorArbol {
    private static final Random rnd = new Random();

    //le pasa la profundidad actual y la maxima y te devuelve un nodo
    public static NodoAst crearGrow(int profActual, int profMax) {
        //si la profundidad ya es maxima se fuerza un nodo hoja
        if (profActual == profMax)
            return new NodoAccion(TipoAccion.values()[rnd.nextInt(3)]);

        //se saca aleatoriamente una de las 3 opciones posibles
        int tipo = rnd.nextInt(3); // 0=Accion, 1=Condicional, 2=Bloque

        //sale un nodo hoja
        if (tipo == 0)
            return new NodoAccion(TipoAccion.values()[rnd.nextInt(3)]);

        //sale un nodo condicional
        else if (tipo == 1) {
            NodoCondicional n = new NodoCondicional(
                    TipoSensor.values()[rnd.nextInt(4)],
                    rnd.nextDouble() * 14 + 1
            );
            n.setHijoIzquierdo(crearGrow(profActual + 1, profMax));
            n.setHijoDerecho(crearGrow(profActual + 1, profMax));
            return n;
        }

        //sale un nodo bloque
        else {
            NodoBloque b = new NodoBloque();
            int numHijos = rnd.nextInt(2) + 2; // 2 o 3 hijos
            for (int i = 0; i < numHijos; i++)
                b.setHijo(crearGrow(profActual + 1, profMax));
            return b;
        }
    }
}
