package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;

// cruce por ciclos
public class CruceCX implements Cruce {

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
        int[] o1 = p1.clone();

        for (int i = 1; i < n; i++) {
            p1[i] = -1;
        }

        // ciclo hasta volver a la primera posicion
        int val = p2[0];
        do {
            int t = search(val, o1);
            p1[t] = val;
            val = p2[t];
        } while (val != p1[0]);

        // rellenar con el otro padre
        for (int i = 0; i < n; i++) {
            if (p1[i] == -1) {
                p1[i] = p2[i];
            }
        }

        return p1;
    }

    private int search(int x, int[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == x) return i;
        }
        return -1;
    }
}
