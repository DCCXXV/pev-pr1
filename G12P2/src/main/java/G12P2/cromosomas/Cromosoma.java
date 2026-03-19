package G12P2.cromosomas;

import G12P2.Scene;
import G12P2.evaluacion.resEvaluacion;

public interface Cromosoma {
    resEvaluacion evaluar();
    Cromosoma copia();
    int[][] generarMapa();
    Scene getScene();
}
