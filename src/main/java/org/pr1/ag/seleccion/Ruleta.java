package org.pr1.ag.seleccion;

import org.pr1.cromosomas.Cromosoma;

import java.util.Random;

public class Ruleta implements Seleccion{
    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {
        int min = Integer.MAX_VALUE;
        for (int fit :  fitness) {
            if (fit < min) {
                min = fit;
            }
        }
        int minAbs = Math.abs(min);

        //ajusta los fitness para que los negativos sean 0
        //y aprovecha y saca el acumulado
        int total  = 0;
        int fitnessAjustado[] = new int[fitness.length];
        for (int i = 0; i < fitness.length; i++) {
            if (fitness[i] < 0)
                fitnessAjustado[i] = fitness[i] + minAbs;
            else
                fitnessAjustado[i] = fitness[i];

            total += fitnessAjustado[i];
        }

        //se saca el porcentaje de cada fitness correspondiente con el acumulado
        double puntuacion[] = new double[fitness.length];
        for (int i = 0; i < fitnessAjustado.length; i++) {
             puntuacion[i] = fitnessAjustado[i] / total;
        }

        //se saca la nueva poblacion
        Cromosoma nuevaPoblacion[] = new Cromosoma[poblacion.length];
        for (int i = 0; i < poblacion.length; i++) {
            //se saca un random de 0 a 1
            Random rand = new Random();
            double random = rand.nextDouble();
            double acumulado = 0;

            //se va sumando las puntuacion y cuando el random pase a ser menor significa que
            //que entra en el rango de la ultima puntuacion sumada
            for (int j = 0; j < puntuacion.length; j++) {
                acumulado += puntuacion[j];
                if (random < acumulado)
                    nuevaPoblacion[i] = poblacion[j];
            }
        }

        return nuevaPoblacion;
    }
}
