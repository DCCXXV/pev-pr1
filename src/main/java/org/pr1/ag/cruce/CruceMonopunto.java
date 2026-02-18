package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;

public class CruceMonopunto implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaBinario p1 = (CromosomaBinario) padre1;
        CromosomaBinario p2 = (CromosomaBinario) padre2;

        boolean[][] genes1 = p1.getGenes();
        boolean[][] genes2 = p2.getGenes();

        // punto de corte elegido al azar
        int puntoCorte = rng.nextInt(genes1.length);

        for (int i = puntoCorte; i < genes1.length; i++) {
            boolean[] temp = genes1[i];
            genes1[i] = genes2[i];
            genes2[i] = temp;
        }
    }
}
