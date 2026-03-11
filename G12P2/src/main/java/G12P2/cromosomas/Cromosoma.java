package G12P2.cromosomas;

import G12P2.Scene;

public interface Cromosoma {
    int evaluar();
    Cromosoma copia();

    /**
     * genera un mapa visual de la solución que representa este cromosoma.
     *   0 = espacio vacío (no cubierto)
     *   1 = posición de cámara
     *   2 = celda visible por alguna cámara
     *   3 = pared
     */
    int[][] generarMapa();
    Scene getScene();
}
