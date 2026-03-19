package G12P2.cromosomas;

import G12P2.Scene;
import G12P2.evaluacion.ResEvaluacion;

public interface Cromosoma {
    ResEvaluacion evaluar();
    Cromosoma copia();
    int[][] generarMapa();
    Scene getScene();
}
