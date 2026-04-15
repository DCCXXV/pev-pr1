package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.TipoAccion;

import java.util.List;

public class NodoAccion implements NodoAst{
    private TipoAccion tipoAccion;

    public NodoAccion(TipoAccion tipo) {
        this.tipoAccion = tipo;
    }

    @Override
    public void ejecutar(Contexto ctx) {
        //TODO
    }

    @Override
    public NodoAst clonar() {
        return new NodoAccion(this.tipoAccion); // primitivo, copia directa
    }

    @Override
    public int contarNodos() {
        return 0;
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        return null;
    }
}
