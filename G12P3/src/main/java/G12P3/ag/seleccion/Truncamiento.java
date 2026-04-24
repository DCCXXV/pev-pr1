package G12P3.ag.seleccion;

import G12P3.ag.Cromosoma;

import java.util.Arrays;

public class Truncamiento implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion) {
        int n = poblacion.length;

        Cromosoma[] ordenados = poblacion.clone();
        Arrays.sort(ordenados, (a, b) -> Double.compare(b.fitness, a.fitness));

        // toma el 30% mejor y rellena ciclicamente
        int corte = Math.max(1, (n * 30) / 100);
        Cromosoma[] nuevaPoblacion = new Cromosoma[n];
        for (int i = 0; i < n; i++) {
            nuevaPoblacion[i] = ordenados[i % corte].clonar();
        }

        return nuevaPoblacion;
    }
}
