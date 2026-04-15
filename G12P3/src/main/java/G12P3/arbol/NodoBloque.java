package G12P3.arbol;

import G12P3.evaluacion.Contexto;

import java.util.ArrayList;
import java.util.List;

public class NodoBloque implements NodoAst {
    private List<NodoAst> listaHijos = new ArrayList<>();

    @Override
    public void ejecutar(Contexto ctx) {
        for (NodoAst hijo : listaHijos)
            hijo.ejecutar(ctx); // el seguro accionTomada corta la ejecución
    }

    public void setHijo(NodoAst hijo) {
        listaHijos.add(hijo);
    }

    @Override
    public NodoAst clonar() {
        NodoBloque copia = new NodoBloque();
        for (NodoAst hijo : listaHijos)
            copia.añadirHijo(hijo.clonar());
        return copia;
    }

    @Override
    public int contarNodos() {
        return 0; //TODO
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        return List.of(); //TODO
    }
}
