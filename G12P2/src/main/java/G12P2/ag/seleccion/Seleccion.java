package G12P2.ag.seleccion;

import G12P2.cromosomas.Cromosoma;

public interface Seleccion {
    Cromosoma[] seleccionar(Cromosoma[] poblacion, double[] fitness);
}
