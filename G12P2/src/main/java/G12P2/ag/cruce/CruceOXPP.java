package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.Random;

public class CruceOXPP implements Cruce {

    private Random rng = new Random();

    @Override
    public void cruzar(Cromosoma padre1, Cromosoma padre2) {
        CromosomaDrones p1 = (CromosomaDrones) padre1;
        CromosomaDrones p2 = (CromosomaDrones) padre2;

        int[] genes1 = p1.getGenes().clone();
        int[] genes2 = p2.getGenes().clone();
        int n = genes1.length;

        // seleccionar posiciones al azar con probabilidad 50%
        boolean[] seleccionadas = new boolean[n];
        for (int i = 0; i < n; i++) {
            seleccionadas[i] = rng.nextDouble() < 0.5;
        }

        p1.setGenes(crearHijo(genes1, genes2, seleccionadas, n));
        p2.setGenes(crearHijo(genes2, genes1, seleccionadas, n));
    }

    private int[] crearHijo(
        int[] p1,
        int[] p2,
        boolean[] seleccionadas,
        int n
    ) {
        // valores prioritarios de p1 en las posiciones seleccionadas
        int[] valores = new int[n];
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (seleccionadas[i]) {
                valores[count++] = p1[i];
            }
        }
        // buscar donde estan esos valores en pb y marcar esas posiciones
        boolean[] huecos = new boolean[n];
        for (int i = 0; i < count; i++) {
            int pos = search(valores[i], p2);
            huecos[pos] = true;
        }
        // copiar p2 dejando huecos
        int[] hijo = new int[n];
        for (int i = 0; i < n; i++) {
            if (huecos[i]) {
                hijo[i] = -1;
            } else {
                hijo[i] = p2[i];
            }
        }
        // rellenar huecos con los valores prioritarios en orden
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (hijo[i] == -1) {
                hijo[i] = valores[idx++];
            }
        }

        return hijo;
    }

    private int search(int x, int[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] == x) return i;
        }
        return -1;
    }
}
