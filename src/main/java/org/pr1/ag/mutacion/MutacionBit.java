package org.pr1.ag.mutacion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;

public class MutacionBit implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaBinario c = (CromosomaBinario) cromosoma;
        boolean[][] genes = c.getGenes();
        int numGenes = genes.length;
        int numBits = genes[0].length;

        for (int i = 0; i < numGenes; i++) {
            for (int j = 0; j < numBits; j++) {
                if (rng.nextDouble() < probMutacion) {
                    // alternar el bit
                    genes[i][j] = !genes[i][j];
                }
            }
        }
    }
}
