package org.pr1.ag.mutacion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class MutacionGen implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaReal c = (CromosomaReal) cromosoma;
        double[] genes = c.getGenes();
        // elegir gen aleatoriamente
        int gen = rng.nextInt(genes.length);
        // conseguir su máximo
        double limiteMax = c.getMax(gen);
        // generar valor aleatorio dentro del rango de este gen (0, limiteMax)
        genes[gen] = rng.nextDouble() * limiteMax;
    }
}
