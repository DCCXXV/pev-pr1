package org.pr1.ag.seleccion;

import org.pr1.cromosomas.Cromosoma;

import java.util.Random;

public class Estocastico implements Seleccion{
    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {

        //calcular la suma total de los fitness
        int total = 0;
        for (int fit : fitness)
            total += fit;

        //distancia maxima para que puedan entrar todos los "punteros"
        double distMax = total / poblacion.length;

        //distancia real
        Random rand = new Random();
        double distancia = rand.nextDouble() * distMax;

        //acumulado del los fitness y distancia al puntero actual
        int acumulado = 0;
        double current = distancia;

        //variables auxiliares
        int i = 0;
        int numEncontrados = 0;
        Cromosoma nuevaPoblacion[] = new Cromosoma[poblacion.length];

        //se saca la nueva poblacion
        while (i < fitness.length && numEncontrados != poblacion.length) {

            //se va sumando el fitness de cada individuo
            acumulado += fitness[i];

            //se meten todos los punteros que se hayan quedado por detras del acumulado actual
            while (current < acumulado) {
                nuevaPoblacion[numEncontrados] = poblacion[i].copia();
                numEncontrados++;
                current += distancia;
            }
            i++;
        }

        return nuevaPoblacion;
    }
}
