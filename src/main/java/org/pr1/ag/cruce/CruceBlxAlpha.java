package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class CruceBlxAlpha implements Cruce {

    private final double alpha;
    private final Random rng = new Random();

    public CruceBlxAlpha(double alpha) {
        this.alpha = alpha;
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

            // se genera aleatoriamente en el intervalo [Cmin – I * alpha, Cmax + I * alpha]
            genes1[i] =
                cmin -
                I *
                alpha *
                rng.nextDouble() *
                ((cmax + I * alpha) - (cmin - I * alpha));
            genes2[i] =
                cmin -
                I *
                alpha *
                rng.nextDouble() *
                ((cmax + I * alpha) - (cmin - I * alpha));
        }
    }
}
