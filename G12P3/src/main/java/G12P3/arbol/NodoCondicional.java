package G12P3.arbol;

import G12P3.evaluacion.Contexto;
import G12P3.evaluacion.Operador;
import G12P3.evaluacion.TipoSensor;
import java.util.ArrayList;
import java.util.List;

public class NodoCondicional implements NodoAst {

    private TipoSensor sensor;
    private Operador operador;
    private int umbral; // 10, 50 o 100
    private NodoAst hijoTrue;
    private NodoAst hijoFalse;
    private int profundidad;

    public NodoCondicional(TipoSensor sensor, Operador operador, int umbral) {
        this.sensor = sensor;
        this.operador = operador;
        this.umbral = umbral;
    }

    @Override
    public void ejecutar(Contexto ctx) {
        if (!ctx.vivo || ctx.ticks >= ctx.MAX_TICKS) return;

        double valorSensor = ctx.leerSensor(sensor);
        if (operador.evaluar(valorSensor, umbral)) hijoTrue.ejecutar(ctx);
        else if (hijoFalse != null) hijoFalse.ejecutar(ctx);
    }

    @Override
    public int getProfundidad() { return profundidad; }

    @Override
    public void setProfundidad(int profundidad) { this.profundidad = profundidad; }

    @Override
    public NodoAst clonar() {
        NodoCondicional copia = new NodoCondicional(sensor, operador, umbral);
        copia.profundidad = this.profundidad;
        copia.hijoTrue = this.hijoTrue.clonar();
        if (this.hijoFalse != null) copia.hijoFalse = this.hijoFalse.clonar();
        return copia;
    }

    @Override
    public int contarNodos() {
        int total = 1;
        total += hijoTrue.contarNodos();
        if (hijoFalse != null) total += hijoFalse.contarNodos();
        return total;
    }

    @Override
    public int calcularProfundidadMaxima() {
        int d = hijoTrue.calcularProfundidadMaxima();
        if (hijoFalse != null) d = Math.max(d, hijoFalse.calcularProfundidadMaxima());
        return 1 + d;
    }

    @Override
    public List<NodoAst> obtenerTodosNodos() {
        List<NodoAst> lista = new ArrayList<>();
        lista.add(this);
        lista.addAll(hijoTrue.obtenerTodosNodos());
        if (hijoFalse != null) lista.addAll(hijoFalse.obtenerTodosNodos());
        return lista;
    }

    @Override
    public boolean isConditional() {
        return true;
    }

    public void setHijoTrue(NodoAst hijoTrue) {
        this.hijoTrue = hijoTrue;
    }

    public void setHijoFalse(NodoAst hijoFalse) {
        this.hijoFalse = hijoFalse;
    }

    public NodoAst getHijoTrue() {
        return hijoTrue;
    }

    public NodoAst getHijoFalse() {
        return hijoFalse;
    }

    public TipoSensor getSensor() {
        return sensor;
    }

    public void setSensor(TipoSensor sensor) {
        this.sensor = sensor;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public int getUmbral() {
        return umbral;
    }

    public void setUmbral(int umbral) {
        this.umbral = umbral;
    }

    @Override
    public String toString() {
        return toStringIndentado(0);
    }

    String toStringIndentado(int nivel) {
        StringBuilder sb = new StringBuilder();
        String indent = "  ".repeat(nivel);

        sb
            .append(indent)
            .append("IF ( ")
            .append(sensor)
            .append(" ")
            .append(operador)
            .append(" ")
            .append(umbral)
            .append(" ) {\n");

        appendHijo(sb, hijoTrue, nivel + 1);

        sb.append(indent).append("}");

        if (hijoFalse != null) {
            sb.append("\n").append(indent).append("ELSE {\n");
            appendHijo(sb, hijoFalse, nivel + 1);
            sb.append(indent).append("}");
        }
        sb.append("\n");
        return sb.toString();
    }

    private void appendHijo(StringBuilder sb, NodoAst hijo, int nivel) {
        if (hijo instanceof NodoCondicional cond) sb.append(
            cond.toStringIndentado(nivel)
        );
        else if (hijo instanceof NodoBloque bloque) sb.append(
            bloque.toStringIndentado(nivel)
        );
        else sb
            .append("  ".repeat(nivel))
            .append(hijo.toString())
            .append(";\n");
    }
}
