package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.TipoSensor;

import java.util.List;

public class NodoCondicional implements NodoAst {
    private TipoSensor sensor;
    private double umbral;
    private NodoAst hijoIzquierdo; // rama TRUE
    private NodoAst hijoDerecho;   // rama FALSE

    public NodoCondicional(TipoSensor sensor, double umbral) {
        this.sensor = sensor;
        this.umbral = umbral;
    }

    @Override
    public void ejecutar(Contexto ctx) {
        if (ctx.leerSensor(sensor) < umbral)
            hijoIzquierdo.ejecutar(ctx);
        else
            hijoDerecho.ejecutar(ctx);
    }

    @Override
    public NodoAst clonar() {
        NodoCondicional copia = new NodoCondicional(sensor, umbral);
        copia.hijoIzquierdo = this.hijoIzquierdo.clonar(); // recursivo
        copia.hijoDerecho   = this.hijoDerecho.clonar();
        return (NodoAst) copia;
    }

    @Override
    public int contarNodos() {
        return 0; //TODO
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        return List.of(); //TODO
    }

    public void setHijoIzquierdo(NodoAst hijoIzquierdo) {
        this.hijoIzquierdo = hijoIzquierdo;
    }

    public void setHijoDerecho(NodoAst hijoDerecho) {
        this.hijoDerecho = hijoDerecho;
    }
}
