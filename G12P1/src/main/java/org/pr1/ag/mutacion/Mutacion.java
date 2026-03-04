package org.pr1.ag.mutacion;

import org.pr1.cromosomas.Cromosoma;

public interface Mutacion {
    void mutar(Cromosoma cromosoma, double probMutacion);
}
