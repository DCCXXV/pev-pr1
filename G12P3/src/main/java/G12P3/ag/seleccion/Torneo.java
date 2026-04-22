package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;
import java.util.Random;

public class Torneo implements Seleccion {

    private static final int TAM_TORNEO = 3;
    private final Random rnd;

    public Torneo(Random rnd) {
        this.rnd = rnd;
    }

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion) {
        Cromosoma[] seleccionados = new Cromosoma[poblacion.length];
        for (int i = 0; i < poblacion.length; i++) {
            Cromosoma mejor = poblacion[rnd.nextInt(poblacion.length)];
            for (int j = 1; j < TAM_TORNEO; j++) {
                Cromosoma candidato = poblacion[rnd.nextInt(poblacion.length)];
                if (candidato.fitness > mejor.fitness) mejor = candidato;
            }
            seleccionados[i] = mejor.clonar();
        }
        return seleccionados;
    }
}
