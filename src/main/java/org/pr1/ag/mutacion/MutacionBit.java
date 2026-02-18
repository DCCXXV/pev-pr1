package org.pr1.ag.mutacion;

import java.util.Random;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;

public class MutacionBit implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma) {
        CromosomaBinario c = (CromosomaBinario) cromosoma;
    }
}
