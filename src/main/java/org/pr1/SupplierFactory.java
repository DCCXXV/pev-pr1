package org.pr1;

import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;
import org.pr1.cromosomas.CromosomaReal;

import java.util.function.Supplier;

public class SupplierFactory {

    /*
    mapas:
    - Museo
    - Pasillos
    - Supermercado
    */
    public static Supplier<Cromosoma> getMapa(String nombre, boolean ponderado, boolean real) {
        //Recibe el nombre del mapa si es ponderado y si es real
        //Y devuelve la factoria de cromosomas con los datos correspondientes
        if (!ponderado)
            return mapasSinPonderar(nombre, real);
        else
            return mapasPonderados(nombre, real);
    }

    private static Supplier<Cromosoma> mapasSinPonderar (String nombre, boolean real) {
        switch (nombre) {
            case "Museo":
                //escena
                Scene museo = new Scene(
                    new int[][]{
                        {0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
                        {0, 1, 0, 0, 0, 0, 0, 0, 1, 0},
                        {0, 0, 0, 1, 0, 0, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
                    },
                    false
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(4, museo, 3, 60);
                else
                    return () -> new CromosomaBinario(4, 3,museo);

            case "Pasillo":
                //escena
                Scene pasillos = new Scene(
                    new int[][] {
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                        {1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1},
                        {1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                        {1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1},
                        {1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1},
                        {1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
                    },
                    false
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(7, pasillos, 5, 90);
                else
                    return () -> new CromosomaBinario(7, 5, pasillos);

            case "SuperMercado":
                //escena
                Scene supermercado = new Scene(
                    new int[][] {
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
                    },
                    false
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(8, supermercado, 7, 70);
                else
                    return () -> new CromosomaBinario(8, 7, supermercado);
        }
        return null;
    }

    private static Supplier<Cromosoma> mapasPonderados(String nombre, boolean real) {
        switch (nombre) {
            case "Museo":
                //escena
                Scene museo = new Scene(
                    new int[][]{
                        {1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                        {1, 0, 5, 5, 5, 1, 1, 1, 0, 1},
                        {1, 1, 1, 0, 1, 1, 0, 1, 1, 1},
                        {1, 1, 1, 1, 5, 5, 5, 1, 1, 1},
                        {0, 1, 1, 1, 0, 10, 1, 1, 1, 1},
                        {1, 1, 0, 1, 1, 5, 1, 0, 1, 1},
                        {1, 1, 1, 1, 1, 5, 1, 1, 1, 1},
                        {1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                        {1, 1, 5, 5, 5, 0, 5, 5, 5, 1},
                        {1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
                    },
                    true
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(4, museo, 3, 60);
                else
                    return () -> new CromosomaBinario(4, 3, museo);

            case "Pasillo":
                //escena
                Scene pasillos = new Scene(
                    new int[][]{
                        {1, 1, 1, 1, 1, 0, 1, 1, 1, 1},
                        {1, 0, 5, 5, 5, 1, 1, 1, 0, 1},
                        {1, 1, 1, 0, 1, 1, 0, 1, 1, 1},
                        {1, 1, 1, 1, 5, 5, 5, 1, 1, 1},
                        {0, 1, 1, 1, 0, 10, 1, 1, 1, 1},
                        {1, 1, 0, 1, 1, 5, 1, 0, 1, 1},
                        {1, 1, 1, 1, 1, 5, 1, 1, 1, 1},
                        {1, 1, 0, 1, 1, 1, 1, 1, 1, 1},
                        {1, 1, 5, 5, 5, 0, 5, 5, 5, 1},
                        {1, 1, 1, 1, 1, 1, 1, 0, 1, 1}
                    },
                    true
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(7, pasillos, 5, 90);
                else
                    return () -> new CromosomaBinario(7, 5, pasillos);

            case "SuperMercado":
                //escena
                Scene supermercado = new Scene(
                    new int[][]{
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 0},
                        {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0,15, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 5, 5, 5, 5, 5, 5, 1, 1, 5, 5, 5, 5, 5, 5, 5, 1, 1, 0},
                        {0, 1, 5, 5, 5, 5, 5, 5, 1, 1, 5, 5, 5, 5, 5, 5, 5, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0,15, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0},
                        {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0},
                        {0, 1,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20,20, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
                    },
                    true
                );

                //si es real o no
                if (real)
                    return () -> new CromosomaReal(8, supermercado, 7, 70);
                else
                    return () -> new CromosomaBinario(8, 7, supermercado);
        }
        return null;
    }
}
