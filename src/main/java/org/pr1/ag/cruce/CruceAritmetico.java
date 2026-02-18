package org.pr1.ag.cruce;

import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaReal;

public class CruceAritmetico implements Cruce {

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaReal p1 = (CromosomaReal) padre1;
        CromosomaReal p2 = (CromosomaReal) padre2;

        double[] genes1 = p1.getGenes();
        double[] genes2 = p2.getGenes();

        /*
            De la teoría
            Realiza una combinación lineal entre los cromosomas de los padres. El caso más
            simple es el cruce de media aritmética, donde el hijo se genera de alguno de los
            siguientes modos: hi = (p1i +p2i) / 2 o hi = αp1i + (1-α)p2i 0<=α<=1.

            Se implementa el primero.
        */
        for (int i = 0; i < genes1.length; i++) {
            double temp1 = genes1[i];
            double temp2 = genes2[i];

            genes1[i] = (temp1 + temp2) / 2;
            genes2[i] = (temp1 + temp2) / 2;
        }
    }
}
