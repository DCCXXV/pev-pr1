package G12P3.evaluacion;

public class ResultadoSimulacion {

    public int muestrasRecogidas;
    public int casillasExploradas;
    public int recompensaVisual;
    public int pisadasArena;
    public int colisiones;
    public double energiaRestante;

    public double calcularFitnessBase() {
        double fitness =
            (muestrasRecogidas * 500) +
            (casillasExploradas * 20) +
            (recompensaVisual * 2) -
            (pisadasArena * 30) -
            (colisiones * 10);

        // penalizacion por pereza
        if (casillasExploradas < 4) fitness -= 1000;

        return fitness;
    }
}
