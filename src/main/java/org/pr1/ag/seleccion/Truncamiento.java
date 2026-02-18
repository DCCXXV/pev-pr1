package org.pr1.ag.seleccion;

import org.pr1.cromosomas.Cromosoma;

import java.util.Arrays;

public class Truncamiento implements Seleccion {

    private record datos(Cromosoma cromosoma, int fitness) {};
    @Override
    public Cromosoma[] seleccionar(Cromosoma[] poblacion, int[] fitness) {
        //enlaza cada cromosoma a su fitness
        datos cromosomasOrdenados[] = new datos[poblacion.length];
        for  (int i = 0; i < poblacion.length; i++)
            cromosomasOrdenados[i] = new datos(poblacion[i], fitness[i]);

        //ordena de mayor a menor (si la resta da negativa pone primero a p1)
        Arrays.sort(cromosomasOrdenados, (dato1, dato2) -> dato2.fitness - dato1.fitness);

        //itero hasta tener la poblacion necesaria
        Cromosoma nuevaPoblacion[] = new Cromosoma[poblacion.length];
        int num = 0;
        int aux = poblacion.length * 30 / 100;
        while (num < poblacion.length) {
            int i = 0;
            //itero por el 30% mejor
            while (num < poblacion.length && i < aux) {
                nuevaPoblacion[num] = cromosomasOrdenados[i].cromosoma.copia();
                num++;
                i++;
            }
        }

        return nuevaPoblacion;
    }
}
