package G12P2.ag.mutacion;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import G12P2.evaluacion.ResEvaluacion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// mutación heurística
public class MutacionHeuristica implements Mutacion {

    private static final int N = 3;
    private Random rng = new Random();

    @Override
    public void mutar(Cromosoma cromosoma, double probMutacion) {
        CromosomaDrones c = (CromosomaDrones) cromosoma;
        int[] genes = c.getGenes();

        if (genes.length < N) return;

        if (rng.nextDouble() < probMutacion) {
            int[] positions = selectDistinctPositions(genes.length);
            int[] values = new int[N];
            for (int i = 0; i < N; i++) values[i] = genes[positions[i]];

            List<int[]> permutations = new ArrayList<>();
            permute(values, 0, permutations);

            int[] bestPerm = null;
            double bestFitness = Double.MAX_VALUE;

            for (int[] perm : permutations) {
                for (int i = 0; i < N; i++) genes[positions[i]] = perm[i];
                ResEvaluacion res = c.evaluar();
                if (res.getFitness() < bestFitness) {
                    bestFitness = res.getFitness();
                    bestPerm = perm.clone();
                }
            }

            for (int i = 0; i < N; i++) genes[positions[i]] = bestPerm[i];
        }
    }

    private int[] selectDistinctPositions(int length) {
        List<Integer> all = new ArrayList<>();
        for (int i = 0; i < length; i++) all.add(i);
        Collections.shuffle(all, rng);
        int[] positions = new int[N];
        for (int i = 0; i < N; i++) positions[i] = all.get(i);
        return positions;
    }

    private void permute(int[] arr, int start, List<int[]> result) {
        if (start == arr.length) {
            result.add(arr.clone());
            return;
        }
        for (int i = start; i < arr.length; i++) {
            int tmp = arr[start];
            arr[start] = arr[i];
            arr[i] = tmp;
            permute(arr, start + 1, result);
            tmp = arr[start];
            arr[start] = arr[i];
            arr[i] = tmp;
        }
    }
}
