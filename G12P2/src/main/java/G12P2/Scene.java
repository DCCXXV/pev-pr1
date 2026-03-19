package G12P2;

import java.util.ArrayList;
import java.util.Random;

public class Scene {

    private final int[][] grid; //mapa con los muros los huecos y las zonas criticas
    private final int[] inicio; //desde donde salen los drones en formato [X,Y]
    private boolean[][] gridCamaras; //mapa con un 1 donde haya una camara
    private int[][] posCamaras; //muchas pares row col con la posicion en el mapa de la camara

    public Scene(int[][] grid, int[] inicio, int numCamaras, int semilla) {
        this.grid = grid;
        this.inicio = inicio;
        generarCamaras(numCamaras, semilla);
    }

    private void generarCamaras(int numCamaras, int semilla) {
        //donde se han colocado las camaras
        this.gridCamaras = new boolean[grid.length][grid[0].length];
        this.posCamaras = new int[numCamaras][2];

        //random con semmilla
        Random rand = new Random(semilla);

        int i = 0;
        while (i < numCamaras) {
            int col = rand.nextInt(grid[0].length);
            int row =  rand.nextInt(grid.length);

            //si esta dentro de un muro
            if (grid[row][col] == 0)
                continue;

            //si ya hay una camara ahi
            else if (this.gridCamaras[row][col])
                continue;

            //se marca la posicion de la camara
            this.gridCamaras[row][col] = true;
            posCamaras[i][0] = row;
            posCamaras[i][1] = col;
            i++;
        }
    }

    public int getCols() {
        return grid.length > 0 ? grid[0].length : 0;
    }

    public int getRows() {
        return grid.length;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[] getInicio() {
        return inicio;
    }

    public boolean[][] getGridCamaras() {
        return gridCamaras;
    }

    public int[][] getPosCamaras() {
        return posCamaras;
    }

    public int getNumCamaras() {
        return posCamaras.length;
    }
}
