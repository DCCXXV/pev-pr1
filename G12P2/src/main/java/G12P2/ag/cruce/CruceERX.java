package G12P2.ag.cruce;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// cruce por recombinacion de rutas
public class CruceERX implements Cruce {

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
        // construir tabla de conectividades por valor
        Map<Integer, Set<Integer>> T = new HashMap<>();
        for (int i = 0; i < n; i++) {
            T.put(p1[i], new HashSet<>());
        }
        for (int i = 0; i < n; i++) {
            T.get(p1[i]).add(p1[(i + 1) % n]);
            T.get(p1[i]).add(p1[(i - 1 + n) % n]);
            T.get(p2[i]).add(p2[(i + 1) % n]);
            T.get(p2[i]).add(p2[(i - 1 + n) % n]);
        }

        int[] hijo = new int[n];
        boolean[] usados = new boolean[n + 1];

        // empezar con el primer elemento de p1
        int actual = p1[0];
        hijo[0] = actual;
        usados[actual] = true;

        for (int i = 1; i < n; i++) {
            // eliminar actual de todas las listas de vecinos
            for (Set<Integer> vecinos : T.values()) {
                vecinos.remove(actual);
            }

            // buscar vecino con menos conexiones
            Set<Integer> vecinos = T.get(actual);
            int mejor = -1;
            int menorSize = Integer.MAX_VALUE;

            for (int v : vecinos) {
                if (!usados[v] && T.get(v).size() < menorSize) {
                    menorSize = T.get(v).size();
                    mejor = v;
                }
            }

            // si no hay vecinos libres elegir uno al azar
            if (mejor == -1) {
                for (int j = 1; j <= n; j++) {
                    if (!usados[j]) {
                        mejor = j;
                        break;
                    }
                }
            }

            hijo[i] = mejor;
            usados[mejor] = true;
            actual = mejor;
        }

        return hijo;
    }
}
