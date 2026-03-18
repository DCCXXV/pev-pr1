package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

// cruce por orden
public class CruceOX implements Cruce {

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

        Set<Integer> segGenes1 = new HashSet<>();
        Set<Integer> segGenes2 = new HashSet<>();
        int[] orig1 = genes1.clone();
        int[] orig2 = genes2.clone();

        for (int i = puntoCorte1; i < puntoCorte2; i++) {
            // guardar elementos de los segmentos centrales
            segGenes1.add(orig1[i]);
            segGenes2.add(orig2[i]);
            // intercambio de segmento central
            genes1[i] = orig2[i];
            genes2[i] = orig1[i];
        }

        int n = genes1.length;
        int cont1 = puntoCorte2 % n;
        int cont2 = puntoCorte2 % n;

        // empezamos a la derecha del segundo punto de corte
        int j = puntoCorte2;
        while (j < n) {
            while (segGenes2.contains(orig1[cont1])) cont1 = (cont1 + 1) % n;
            genes1[j] = orig1[cont1];
            cont1 = (cont1 + 1) % n;

            while (segGenes1.contains(orig2[cont2])) cont2 = (cont2 + 1) % n;
            genes2[j] = orig2[cont2];
            cont2 = (cont2 + 1) % n;
            j++;
        }

        // seguimos por el principio
        j = 0;
        while (j < puntoCorte1) {
            while (segGenes2.contains(orig1[cont1])) cont1 = (cont1 + 1) % n;
            genes1[j] = orig1[cont1];
            cont1 = (cont1 + 1) % n;

            while (segGenes1.contains(orig2[cont2])) cont2 = (cont2 + 1) % n;
            genes2[j] = orig2[cont2];
            cont2 = (cont2 + 1) % n;
            j++;
        }
    }
}
