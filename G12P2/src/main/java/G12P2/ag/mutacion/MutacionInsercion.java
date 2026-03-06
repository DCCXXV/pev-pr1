package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.Random;

// mutación por insercion
public class MutacionInsercion implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaDrones c = (CromosomaDrones) cromosoma;
        int[] genes = c.getGenes();

        /**
         * según la teoría puede ser de una o múltiples inserciones/desplazamientos
         * se implementa una versión de 2 desplazamientos
         */
        if (rng.nextDouble() < probMutacion) {
            for (int k = 1; k <= 2; k++) {
                int posElegido = rng.nextInt(genes.length);
                int envElegido = rng.nextInt(genes.length);

                if (posElegido > envElegido) {
                    int elem = genes[posElegido];
                    for (int i = posElegido; i > envElegido; i--) {
                        genes[i] = genes[i - 1];
                    }
                    genes[envElegido] = elem;
                } else if (posElegido < envElegido) {
                    int elem = genes[posElegido];
                    for (int i = posElegido; i < envElegido; i++) {
                        genes[i] = genes[i + 1];
                    }
                    genes[envElegido] = elem;
                }
            }
        }
    }
}
