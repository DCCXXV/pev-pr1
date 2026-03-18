package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;

// cruce alternante
public class CruceA implements Cruce {

    /**
     * se van cogiendo valores alternando desde el inicio entre ambos padres, usando una
     *  lista booleana para saltar los que ya se han colocado, entrelazando así ambas permutaciones.
     *
     * ej:
     * P1: 1 4 2 3 y P2: 3 4 1 2
     * H1: 1 3 4 2 y H2: 3 1 4 2
     */

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaDrones p1 = (CromosomaDrones) padre1;
        CromosomaDrones p2 = (CromosomaDrones) padre2;

        int[] genes1 = p1.getGenes().clone();
        int[] genes2 = p2.getGenes().clone();
        int n = genes1.length;

        p1.setGenes(crearHijo(genes1, genes2, n));
        p2.setGenes(crearHijo(genes2, genes1, n));
    }

    private int[] crearHijo(int[] p1, int[] p2, int n) {
        int[] hijo = new int[n];
        boolean[] usados = new boolean[n + 1];
        int idx = 0;
        int i = 0;
        int j = 0;

        while (idx < n) {
            while (i < n && usados[p1[i]]) i++;
            if (i < n) {
                hijo[idx++] = p1[i];
                usados[p1[i]] = true;
                i++;
            }

            while (j < n && usados[p2[j]]) j++;
            if (j < n && idx < n) {
                hijo[idx++] = p2[j];
                usados[p2[j]] = true;
                j++;
            }
        }

        return hijo;
    }
}
