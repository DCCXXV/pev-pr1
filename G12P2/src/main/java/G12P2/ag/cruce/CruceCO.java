package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

// codificacion ordinal
public class CruceCO implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaDrones p1 = (CromosomaDrones) padre1;
        CromosomaDrones p2 = (CromosomaDrones) padre2;

        int[] genes1 = p1.getGenes().clone();
        int[] genes2 = p2.getGenes().clone();
        int n = genes1.length;

        // inicializamos la lista de referencia
        ArrayList<Integer> L = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            L.add(i + 1);
        }
        ArrayList<Integer> L1 = new ArrayList<>(L);
        ArrayList<Integer> L2 = new ArrayList<>(L);

        for (int i = 0; i < n; i++) {
            /**
             * De los apuntes:
             * Para construir un individuo se van sacando una a una las ciudades recorridas,
             * codificando en el j-ésimo gen del individuo la posición que tiene la j-ésima
             * ciudad en la lista dinámica
             */

            // buscar en la lista dinamica
            int j1 = Collections.binarySearch(L1, genes1[i]);
            int j2 = Collections.binarySearch(L2, genes2[i]);

            // codificarlo en el gen del individuo
            genes1[i] = j1;
            genes2[i] = j2;

            // sacarlo de la lista
            L1.remove(j1);
            L2.remove(j2);
        }

        // aplicar cruce clasico
        int puntoCorte = rng.nextInt(genes1.length);

        for (int i = puntoCorte; i < n; i++) {
            int temp = genes1[i];
            genes1[i] = genes2[i];
            genes2[i] = temp;
        }

        // decodificar los hijos
        ArrayList<Integer> DL1 = new ArrayList<>(L);
        ArrayList<Integer> DL2 = new ArrayList<>(L);
        for (int i = 0; i < n; i++) {
            genes1[i] = DL1.remove(genes1[i]);
            genes2[i] = DL2.remove(genes2[i]);
        }

        p1.setGenes(genes1);
        p2.setGenes(genes2);
    }
}
