package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
    mutación por scramble (invencion)

    selecciona dos puntos al azar y mezcla aleatoriamente los elementos del segmento entre ellos.
    A diferencia de la inversión (que invierte el segmento de forma determinista),
    esta aplica una permutación aleatoria, explorando más variedad en el espacio de búsqueda.
*/
public class MutacionScramble implements Mutacion {

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

            List<Integer> segment = new ArrayList<>();
            for (int k = i; k <= j; k++) segment.add(genes[k]);

            Collections.shuffle(segment, rng);

            for (int k = i; k <= j; k++) genes[k] = segment.get(k - i);
        }
    }
}
