package org.pr1.ag.seleccion;

import org.pr1.cromosomas.Cromosoma;

public interface Seleccion {
    Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness);
}
