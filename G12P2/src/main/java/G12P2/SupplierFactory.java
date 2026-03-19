package G12P2;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import java.util.Random;
import java.util.function.Supplier;

public class SupplierFactory {

    /*
    mapas:
    - Museo
    - Pasillos
    - Supermercado
    */
    public static Supplier<Cromosoma> getMapa(
        String nombre,
        long semilla,
        int numCamaras,
        int numDrones
    ) {
        int[][] grid = copiarGrid(Mapas.getMapa(nombre + "Ponderado"));
        insertarCamaras(grid, numCamaras, semilla);
        Scene scene = new Scene(grid, new int[]{0,0}, 0,0); //TODO esto con 0 y 0 habria que quitarlo
        return () -> new CromosomaDrones(numDrones, scene);
    }

    private static void insertarCamaras(
        int[][] grid,
        int numCamaras,
        long semilla
    ) {
        int rows = grid.length;
        int cols = grid[0].length;
        Random rand = new Random(semilla);
        int colocadas = 0;

        while (colocadas < numCamaras) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (grid[r][c] > 0) {
                grid[r][c] = -1;
                colocadas++;
            }
        }
    }

    private static int[][] copiarGrid(int[][] original) {
        int[][] copia = new int[original.length][];
        for (int i = 0; i < original.length; i++) copia[i] =
            original[i].clone();
        return copia;
    }
}
