package G12P2.ag.seleccion;

import G12P2.cromosomas.Cromosoma;
import java.util.Random;

public class Ranking implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {
        int n = poblacion.length;

        // ordenar de menor a mayor fitness -> rank 1 al de menor, rank N al de mayor
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) indices[i] = i;
        java.util.Arrays.sort(indices, (a, b) -> fitness[a] - fitness[b]);

        // asignar probabilidad a cada rango (usamos Prob. rangoK = rangoK / N*(N+1)/2)
        double total = (n * (n + 1)) / 2.0;
        double[] prob = new double[n];
        for (int r = 0; r < n; r++) {
            prob[indices[r]] = (r + 1) / total;
        }

        // ruleta sobre las probabilidades por ranking
        Cromosoma[] nuevaPoblacion = new Cromosoma[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            double aleatorio = rand.nextDouble();
            int j = 0;
            double acumulado = prob[0];
            while (j < n - 1 && aleatorio > acumulado) {
                j++;
                acumulado += prob[j];
            }
            nuevaPoblacion[i] = poblacion[j].copia();
        }

        return nuevaPoblacion;
    }
}
