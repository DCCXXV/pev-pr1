package org.pr1;

public class Scene {

    private final int[][] grid;

    Scene(int[][] grid) {
        this.grid = grid;
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
}
