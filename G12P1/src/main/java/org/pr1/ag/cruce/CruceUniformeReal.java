package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class CruceUniformeReal implements Cruce {

    private final double prob;
    private final Random rng = new Random();

    public CruceUniformeReal(double prob) {
        this.prob = prob;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaReal p1 = (CromosomaReal) padre1;
        CromosomaReal p2 = (CromosomaReal) padre2;

        double[] genes1 = p1.getGenes();
        double[] genes2 = p2.getGenes();

        // para cada gen, con probabilidad prob se intercambian entre los dos padres
        for (int i = 0; i < genes1.length; i++) {
            if (rng.nextDouble() < prob) {
                double temp = genes1[i];
                genes1[i] = genes2[i];
                genes2[i] = temp;
            }
        }
    }
}
