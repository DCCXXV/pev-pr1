package G12P3.evaluacion;

public enum Operador {
    MENOR,   // <
    MAYOR,   // >
    IGUAL;   // ==

    public boolean evaluar(double valorSensor, double umbral) {
        return switch (this) {
            case MENOR -> valorSensor < umbral;
            case MAYOR -> valorSensor > umbral;
            case IGUAL -> valorSensor == umbral;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case MENOR -> "<";
            case MAYOR -> ">";
            case IGUAL -> "==";
        };
    }
}
