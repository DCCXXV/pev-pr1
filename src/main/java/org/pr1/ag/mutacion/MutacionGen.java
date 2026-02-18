package org.pr1.ag.mutacion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class MutacionGen implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaReal c = (CromosomaReal) cromosoma;
        double[] genes = c.getGenes();

        for (int i = 0; i < genes.length; i++) {
            if (rng.nextDouble() < probMutacion) {
                // generar valor aleatorio dentro del rango de este gen (0, limiteMax)
                double limiteMax = c.getMax(i);
                genes[i] = rng.nextDouble() * limiteMax;
            }
        }
    }
}
