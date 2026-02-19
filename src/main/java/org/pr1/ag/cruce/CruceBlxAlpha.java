package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class CruceBlxAlpha implements Cruce {

    private final double alpha;
    private final Random rng = new Random();

    public CruceBlxAlpha(double alpha) {
        this.alpha = alpha; // [0, 1]
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaReal p1 = (CromosomaReal) padre1;
        CromosomaReal p2 = (CromosomaReal) padre2;

        double[] genes1 = p1.getGenes();
        double[] genes2 = p2.getGenes();

        for (int i = 0; i < genes1.length; i++) {
            double cmin = Math.min(genes1[i], genes2[i]);
            double cmax = Math.max(genes1[i], genes2[i]);
            double I = cmax - cmin;

            // hki generado aleatoriamente en el intervalo [Cmin – I * alpha, Cmax + I * alpha]
            genes1[i] =
                rng.nextDouble() *
                (cmin - I * alpha) +
                rng.nextDouble() * ((cmax + I * alpha) - (cmin - I * alpha));
            genes2[i] =
                (cmin - I * alpha) +
                rng.nextDouble() * ((cmax + I * alpha) - (cmin - I * alpha));

            // truncar al rango válido del gen
            double max = p1.getMax(i);
            genes1[i] = Math.min(Math.max(genes1[i], 0), max - 0.001);
            genes2[i] = Math.min(Math.max(genes2[i], 0), max - 0.001);
        }
    }
}
