package G12P3.arbol;

import G12P3.evaluacion.Contexto;

import java.util.List;

public interface NodoAst {
    void ejecutar(Contexto ctx);
    NodoAst clonar();
    int contarNodos();
    List<NodoAst> obtenerTodosNodos();
}
