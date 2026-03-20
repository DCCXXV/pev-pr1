package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.Random;

// mutación por inversión
public class MutacionInversion implements Mutacion {

    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaDrones c = (CromosomaDrones) cromosoma;
        int[] genes = c.getGenes();

        if (rng.nextDouble() < probMutacion) {
            int i = rng.nextInt(genes.length);
            int j = rng.nextInt(genes.length);

            if (i > j) {
                int tmp = i;
                i = j;
                j = tmp;
            }

            while (i < j) {
                int temp = genes[i];
                genes[i] = genes[j];
                genes[j] = temp;
                i++;
                j--;
            }
        }
    }
}
