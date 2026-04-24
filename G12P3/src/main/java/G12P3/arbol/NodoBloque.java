package G12P3.arbol;

import G12P3.evaluacion.Contexto;

import java.util.ArrayList;
import java.util.List;

public class NodoBloque implements NodoAst {
    private List<NodoAst> hijos = new ArrayList<>();
    private int profundidad;

    @Override
    public void ejecutar(Contexto ctx) {
        for (NodoAst hijo : hijos) {
            if (!ctx.vivo || ctx.ticks >= ctx.MAX_TICKS) return;
            hijo.ejecutar(ctx);
        }
    }

    public void setHijo(NodoAst hijo) {
        hijos.add(hijo);
    }

    public List<NodoAst> getHijos() {
        return hijos;
    }

    @Override
    public int getProfundidad() { return profundidad; }

    @Override
    public void setProfundidad(int profundidad) { this.profundidad = profundidad; }

    @Override
    public NodoAst clonar() {
        NodoBloque copia = new NodoBloque();
        copia.profundidad = this.profundidad;
        for (NodoAst hijo : hijos)
            copia.setHijo(hijo.clonar());
        return copia;
    }

    @Override
    public int contarNodos() {
        int total = 1;
        for (NodoAst hijo : hijos)
            total += hijo.contarNodos();
        return total;
    }

    @Override
    public int calcularProfundidadMaxima() {
        int d = 0;
        for (NodoAst hijo : hijos) d = Math.max(d, hijo.calcularProfundidadMaxima());
        return 1 + d;
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
    public boolean isConditional() {
        return true;
    }

    @Override
    public String toString() {
        return toStringIndentado(0);
    }

    String toStringIndentado(int nivel) {
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
