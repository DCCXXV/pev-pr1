package G12P3.arbol;

import G12P3.evaluacion.Contexto;

import java.util.ArrayList;
import java.util.List;

public class NodoBloque implements NodoAst {
    private List<NodoAst> hijos = new ArrayList<>();

    @Override
    public void ejecutar(Contexto ctx) {
        for (NodoAst hijo : hijos) {
            if (ctx.accionTomada || !ctx.vivo) return;
            hijo.ejecutar(ctx);
        }
    }

    public void añadirHijo(NodoAst hijo) {
        hijos.add(hijo);
    }

    public List<NodoAst> getHijos() {
        return hijos;
    }

    @Override
    public NodoAst clonar() {
        NodoBloque copia = new NodoBloque();
        for (NodoAst hijo : hijos)
            copia.añadirHijo(hijo.clonar());
        return copia;
    }

    @Override
    public int contarNodos() {
        int total = 1; // el propio bloque
        for (NodoAst hijo : hijos)
            total += hijo.contarNodos();
        return total;
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        List<NodoAst> lista = new ArrayList<>();
        lista.add(this);
        for (NodoAst hijo : hijos)
            lista.addAll(hijo.obtenerTodosNodos());
        return lista;
    }

    @Override
    public String toString() {
        return toStringIndentado(0);
    }

    private String toStringIndentado(int nivel) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(nivel);
        for (NodoAst hijo : hijos) {
            if (hijo instanceof NodoBloque bloque)
                sb.append(bloque.toStringIndentado(nivel));
            else if (hijo instanceof NodoCondicional cond)
                sb.append(cond.toStringIndentado(nivel));
            else
                sb.append(indent).append(hijo.toString()).append(";\n");
        }
        return sb.toString();
    }
}
