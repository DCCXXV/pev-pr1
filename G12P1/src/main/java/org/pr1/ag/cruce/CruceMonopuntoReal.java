package org.pr1.ag.cruce;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class CruceMonopuntoReal implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaReal p1 = (CromosomaReal) padre1;
        CromosomaReal p2 = (CromosomaReal) padre2;

        double[] genes1 = p1.getGenes();
        double[] genes2 = p2.getGenes();

        // punto de corte elegido al azar
        int puntoCorte = rng.nextInt(genes1.length);

        for (int i = puntoCorte; i < genes1.length; i++) {
            double temp = genes1[i];
            genes1[i] = genes2[i];
            genes2[i] = temp;
        }
    }
}
