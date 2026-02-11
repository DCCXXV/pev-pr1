package org.pr1.evaluacion;

import org.pr1.Scene;
import org.pr1.cromosomas.CromosomaReal;

public class EvaluacionConica implements Evaluacion {

    private final int PENALIZACION = -100;

    private Scene s;
    private CromosomaReal c;

    public EvaluacionConica(Scene s, CromosomaReal c) {
        this.s = s;
        this.c = c;
    }

    @Override
    public int evaluar() {
        double[] genes = c.getGenes();
        int numCamaras = c.getNumCamaras();
        double rango = c.getRangoVision();
        double apertura = c.getAnguloApertura();
        boolean ponderado = c.isPonderado();

        int rows = s.getRows();
        int cols = s.getCols();
        int[][] grid = s.getGrid();

        int[][] cobertura = new int[rows][cols];

        for (int k = 0; k < numCamaras; k++) {
            int base = k * 3;
            double camX = genes[base];
            double camY = genes[base + 1];
            double theta = genes[base + 2];

            int celdaCamX = (int) camX;
            int celdaCamY = (int) camY;
            if (
                (celdaCamX >= 0 &&
                    celdaCamX < cols &&
                    celdaCamY >= 0 &&
                    celdaCamY < rows &&
                    (ponderado && grid[celdaCamY][celdaCamX] == 0)) ||
                (!ponderado && grid[celdaCamY][celdaCamX] == 1)
            ) {
                return PENALIZACION;
            }

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (ponderado && grid[row][col] == 0) continue;
                    if (!ponderado && grid[row][col] == 1) continue;

                    double celdaX = col + 0.5;
                    double celdaY = row + 0.5;

                    double dist = Math.sqrt(
                        Math.pow(celdaX - camX, 2) + Math.pow(celdaY - camY, 2)
                    );
                    if (dist > rango) continue;

                    double angulo = Math.toDegrees(
                        Math.atan2(celdaY - camY, celdaX - camX)
                    );
                    if (angulo < 0) angulo += 360;

                    double diff = Math.abs(angulo - theta);
                    if (diff > 180) diff = 360 - diff;
                    if (diff > apertura / 2.0) continue;

                    if (!lineaLibre(camX, camY, celdaX, celdaY)) continue;

                    cobertura[row][col]++;
                }
            }
        }

        int fitness = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cobertura[row][col] > 0) {
                    fitness++;
                }
            }
        }
        return fitness;
    }

    /**
     * ALGORITMO DE RAYCASTING (VISIBILIDAD)
     * -------------------------------------
     * Verifica si hay línea de visión directa entre dos puntos (x1,y1) y (x2,y2).
     * Características:
     * 1. Precisión: 20 pasos por unidad de distancia (evita saltos).
     * 2. Anticlipping: Bloquea la visión si el rayo intenta pasar entre dos muros diagonales.
     */
    private boolean lineaLibre(double x1, double y1, double x2, double y2) {
        // 1. Calcular distancia total
        double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        // Optimización: Si está muy cerca, asumimos que se ve
        if (dist < 0.1) return true;
        // 2. Definir la resolución del paso (20 pasos por celda = 0.05 de avance)
        int pasos = (int) (dist * 20.0);
        double dx = (x2 - x1) / pasos;
        double dy = (y2 - y1) / pasos;

        double px = x1;
        double py = y1;

        // Guardamos la celda anterior para detectar cambios de diagonal
        int prevX = (int) x1;
        int prevY = (int) y1;
        // 3. Recorrer el rayo paso a paso
        for (int i = 0; i < pasos; i++) {
            px += dx;
            py += dy;

            int cx = (int) px; // Columna actual
            int cy = (int) py; // Fila actual

            // A. Chequeo de límites del mapa
            if (
                cx < 0 || cx >= s.getCols() || cy < 0 || cy >= s.getRows()
            ) return false;

            // B. Chequeo de Muro Directo (0 es muro en tus mapas PDF)
            if (s.getGrid()[cy][cx] == 0) return false;
            // C. LÓGICA ANTICLIPPING (Bloqueo de esquinas)
            // Si hemos cambiado de columna Y de fila a la vez...
            if (cx != prevX && cy != prevY) {
                // Verificamos los dos vecinos que forman la esquina
                // Si ambos son muros (0), entonces la esquina está cerrada.
                if (
                    s.getGrid()[prevY][cx] == 0 && s.getGrid()[cy][prevX] == 0
                ) {
                    return false;
                }
            }
            // Actualizamos la celda previa
            prevX = cx;
            prevY = cy;
        }

        // Si llegamos aquí, el camino está despejado
        return true;
    }
}
