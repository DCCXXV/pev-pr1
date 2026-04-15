package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.TipoAccion;

import java.util.ArrayList;
import java.util.List;

public class NodoAccion implements NodoAst {
    private TipoAccion tipoAccion;

    public NodoAccion(TipoAccion tipo) {
        this.tipoAccion = tipo;
    }

    @Override
    public void ejecutar(Contexto ctx) {
        if (ctx.accionTomada || !ctx.vivo) return;

        switch (tipoAccion) {
            case AVANZAR -> ctx.avanzar();
            case GIRAR_IZQ -> ctx.girarIzquierda();
            case GIRAR_DER -> ctx.girarDerecha();
        }
    }

    @Override
    public NodoAst clonar() {
        return new NodoAccion(this.tipoAccion);
    }

    @Override
    public int contarNodos() {
        return 1;
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        List<NodoAst> lista = new ArrayList<>();
        lista.add(this);
        return lista;
    }

    public TipoAccion getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(TipoAccion tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    @Override
    public String toString() {
        return tipoAccion.name() + "()";
    }
}
