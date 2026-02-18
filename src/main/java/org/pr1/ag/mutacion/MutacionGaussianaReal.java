package org.pr1.ag.mutacion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class MutacionGaussianaReal implements Mutacion {

    private Random rng = new Random();

    private static final double SIGMA_MUTACION = 2.0;

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaReal c = (CromosomaReal) cromosoma;
        double[] genes = c.getGenes();

        for (int i = 0; i < genes.length; i++) {
            if (rng.nextDouble() < probMutacion) {
                /*
                    sumar ruido gaussiano
                    De la teoria: Si Sigma es bajo (ej. 2.0), los pasos son cortos. Si es alto (ej. 20.0),
                    los pasos son largos. Podemos elegir el sigma según el valor que queremos mutar (coordenadas,
                    grados…)
                */
                genes[i] += rng.nextGaussian() * SIGMA_MUTACION;
                // truncar al rango válido
                double limiteMax = c.getMax(i);
                if (genes[i] < 0) genes[i] = 0;
                if (genes[i] >= limiteMax) genes[i] = limiteMax - 0.001;
            }
        }
    }
}
