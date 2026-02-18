package org.pr1.ag.seleccion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;

public class Estocastico implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {
        int n = poblacion.length;

        // desplazar fitness para que no sea negativo
        int min = Integer.MAX_VALUE;
        for (int fit : fitness) if (fit < min) min = fit;
        int offset = min < 0 ? -min : 0;

        //calcular la suma total de los fitness ajustados
        int total = 0;
        int[] fitnessAjustado = new int[n];
        for (int i = 0; i < n; i++) {
            fitnessAjustado[i] = fitness[i] + offset;
            total += fitnessAjustado[i];
        }

        //distancia fija entre punteros (paso uniforme)
        double distMax = (double) total / n;

        //punto de inicio aleatorio en [0, distMax)
        Random rand = new Random();
        double current = rand.nextDouble() * distMax;

        //acumulado del fitness y variables auxiliares
        int acumulado = 0;
        int i = 0;
        int numEncontrados = 0;
        Cromosoma nuevaPoblacion[] = new Cromosoma[n];

        //se saca la nueva poblacion
        while (i < n && numEncontrados < n) {
            //se va sumando el fitness de cada individuo
            acumulado += fitnessAjustado[i];

            //se meten todos los punteros que se hayan quedado por detras del acumulado actual
            while (numEncontrados < n && current < acumulado) {
                nuevaPoblacion[numEncontrados] = poblacion[i].copia();
                numEncontrados++;
                current += distMax;
            }
            i++;
        }

        return nuevaPoblacion;
    }
}
