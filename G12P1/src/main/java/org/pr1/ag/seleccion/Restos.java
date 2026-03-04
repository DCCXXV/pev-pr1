package org.pr1.ag.seleccion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;

public class Restos implements Seleccion {

    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {
        int n = poblacion.length;

        // desplazar fitness para que no sea negativo
        int min = Integer.MAX_VALUE;
        for (int fit : fitness) if (fit < min) min = fit;
        int offset = min < 0 ? -min : 0;

        int total = 0;
        int[] fitnessAjustado = new int[n];
        for (int i = 0; i < n; i++) {
            fitnessAjustado[i] = fitness[i] + offset;
            total += fitnessAjustado[i];
        }

        // si todos valen 0, devolver la población tal cual
        if (total == 0) {
            Cromosoma[] copia = new Cromosoma[n];
            for (int i = 0; i < n; i++) copia[i] = poblacion[i].copia();
            return copia;
        }

        double media = (double) total / n;

        // copias garantizadas (parte entera de fitness_i / media)
        // y parte fraccional para la ruleta de restos
        int[] garantizados = new int[n];
        double[] fraccion = new double[n];
        for (int i = 0; i < n; i++) {
            double esperado = fitnessAjustado[i] / media;
            garantizados[i] = (int) esperado;
            fraccion[i] = esperado - garantizados[i];
        }

        // llenar la nueva población con las copias garantizadas
        Cromosoma[] nuevaPoblacion = new Cromosoma[n];
        int num = 0;
        for (int i = 0; i < n && num < n; i++) for (
            int j = 0;
            j < garantizados[i] && num < n;
            j++
        ) nuevaPoblacion[num++] = poblacion[i].copia();

        // rellenar los huecos restantes con ruleta sobre las fracciones
        if (num < n) {
            double totalFrac = 0;
            for (double f : fraccion) totalFrac += f;

            Random rand = new Random();
            while (num < n) {
                double r = rand.nextDouble() * totalFrac;
                double acumulado = fraccion[0];
                int j = 0;
                while (j < n - 1 && r > acumulado) {
                    j++;
                    acumulado += fraccion[j];
                }
                nuevaPoblacion[num++] = poblacion[j].copia();
            }
        }

        return nuevaPoblacion;
    }
}
