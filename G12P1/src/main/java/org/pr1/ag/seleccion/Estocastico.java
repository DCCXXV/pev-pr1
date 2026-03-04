package org.pr1.ag.seleccion;

import org.pr1.cromosomas.Cromosoma;

import java.util.Random;

public class Estocastico implements Seleccion{
    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {

        // desplazar fitness para que el mínimo sea 0 (igual que Ruleta)
        int min = Integer.MAX_VALUE;
        for (int fit : fitness)
            if (fit < min) min = fit;
        int offset = (min < 0) ? -min : 0;

        int[] fitnessAjustado = new int[fitness.length];
        int total = 0;
        for (int i = 0; i < fitness.length; i++) {
            fitnessAjustado[i] = fitness[i] + offset;
            total += fitnessAjustado[i];
        }

        Cromosoma[] nuevaPoblacion = new Cromosoma[poblacion.length];
        Random rand = new Random();

        // si todos tienen fitness 0, selección uniforme aleatoria
        if (total == 0) {
            for (int i = 0; i < poblacion.length; i++)
                nuevaPoblacion[i] = poblacion[rand.nextInt(poblacion.length)].copia();
            return nuevaPoblacion;
        }

        //distancia maxima para que puedan entrar todos los "punteros"
        double distMax = (double) total / poblacion.length;

        //punto de inicio aleatorio en [0, distMax)
        double current = rand.nextDouble() * distMax;

        //se saca la nueva poblacion
        int acumulado = 0;
        int i = 0;
        int numEncontrados = 0;
        while (i < fitnessAjustado.length && numEncontrados < poblacion.length) {
            acumulado += fitnessAjustado[i];
            while (current < acumulado && numEncontrados < poblacion.length) {
                nuevaPoblacion[numEncontrados] = poblacion[i].copia();
                numEncontrados++;
                current += distMax;
            }
            i++;
        }

        // relleno de seguridad por si quedan huecos
        while (numEncontrados < poblacion.length) {
            nuevaPoblacion[numEncontrados] = poblacion[poblacion.length - 1].copia();
            numEncontrados++;
        }

        return nuevaPoblacion;
    }
}
