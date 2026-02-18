package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;

public class CruceUniformeBin implements Cruce {

    private final double prob;
    private final Random rng = new Random();

    public CruceUniformeBin(double prob) {
        this.prob = prob;
    }

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaBinario p1 = (CromosomaBinario) padre1;
        CromosomaBinario p2 = (CromosomaBinario) padre2;

        boolean[][] genes1 = p1.getGenes();
        boolean[][] genes2 = p2.getGenes();

        // para cada gen, con probabilidad prob se intercambian entre los dos padres
        for (int i = 0; i < genes1.length; i++) {
            if (rng.nextDouble() < prob) {
                boolean[] temp = genes1[i];
                genes1[i] = genes2[i];
                genes2[i] = temp;
            }
        }
    }
}
