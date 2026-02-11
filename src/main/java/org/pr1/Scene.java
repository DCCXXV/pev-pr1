package org.pr1;

public class Scene {

    private final int[][] grid;
    private boolean ponderado;

    Scene(int[][] grid, boolean ponderado) {
        this.grid = grid;
        this.ponderado = ponderado;
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

    public boolean isPonderado() {
        return ponderado;
    }
}
