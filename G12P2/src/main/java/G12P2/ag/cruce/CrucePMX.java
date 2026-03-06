package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// cruce por emparejamiento parcial
public class CrucePMX implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaDrones p1 = (CromosomaDrones) padre1;
        CromosomaDrones p2 = (CromosomaDrones) padre2;

        int[] genes1 = p1.getGenes();
        int[] genes2 = p2.getGenes();

        // puntos de corte elegido al azar
        int puntoCorte1 = rng.nextInt(genes1.length);
        int puntoCorte2 = rng.nextInt(genes1.length);
        if (puntoCorte1 > puntoCorte2) {
            int temp = puntoCorte1;
            puntoCorte1 = puntoCorte2;
            puntoCorte2 = temp;
        }

        Map<Integer, Integer> pareja = new HashMap<>();
        Set<Integer> segGenes1 = new HashSet<>();
        Set<Integer> segGenes2 = new HashSet<>();
        for (int i = puntoCorte1; i < puntoCorte2; i++) {
            // gruardar elementos de los segmentos centrales
            segGenes1.add(genes1[i]);
            segGenes2.add(genes2[i]);
            // parejas
            pareja.put(genes1[i], genes2[i]);
            pareja.put(genes2[i], genes1[i]);
            // intercambio de segmento central
            int temp = genes1[i];
            genes1[i] = genes2[i];
            genes2[i] = temp;
        }

        for (int i = 0; i < genes1.length; i++) {
            if (i >= puntoCorte1 && i < puntoCorte2) continue;
            while (segGenes2.contains(genes1[i])) {
                genes1[i] = pareja.get(genes1[i]);
            }
            while (segGenes1.contains(genes2[i])) {
                genes2[i] = pareja.get(genes2[i]);
            }
        }
    }
}
