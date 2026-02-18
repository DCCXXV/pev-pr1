package org.pr1.evaluacion;

import org.pr1.Scene;
import org.pr1.cromosomas.CromosomaBinario;

public class EvaluacionBinaria {

    //DATOS
    static boolean[][] visited;

    //Devuelve el numero de celdas visibles
    public static int evaluar(CromosomaBinario cromosoma) {
        //escena a la que se refiere el cromosoma
        Scene scene = cromosoma.getScene();

        //estructura de datos para no contar repetidas las baldosas
        visited = new boolean[scene.getRows()][scene.getCols()];

        //estructura de datos para guardar el total de baldosas
        int total = 0;

        //se itera por cada camara
        int numeroCamaras = cromosoma.getNumCamaras();
        boolean[][] datos = cromosoma.getCromosoma();
        for (int i = 0; i < numeroCamaras; i++) {
            //parsea la posicion en binario a entero, trunca al rango valido
            int posX = Math.min(
                parsearBinario(datos[i * 2]),
                scene.getCols() - 1
            );
            int posY = Math.min(
                parsearBinario(datos[i * 2 + 1]),
                scene.getRows() - 1
            );

            //posicion de la camara
            int[] pos = new int[] { posX, posY };

            //se mira si la camara esta en una columna
            int baldosa = scene.getGrid()[posY][posX];

            //si no es ponderado y hay un 1 significa columna
            if (!scene.isPonderado() && baldosa == 1) {
                total -= 100;
                continue;
            }
            //si es ponderado y ha un 0 significa columna
            else if (scene.isPonderado() && baldosa == 0) {
                total -= 100;
                continue;
            }

            //se suman las puntuaciones si no esta en una columna
            total += avanzar(scene, pos, 0, -1);
            total += avanzar(scene, pos, 0, 1);
            total += avanzar(scene, pos, 1, -1);
            total += avanzar(scene, pos, 1, 1);
        }

        return total;
    }

    private static int parsearBinario(boolean[] bits) {
        int resultado = 0;

        for (int i = 0; i < bits.length; i++) {
            //se desplaza a la izquierda el resultado actual
            resultado <<= 1;
            if (bits[i]) {
                //se pone el bit actual a 1
                resultado |= 1;
            }
        }

        return resultado;
    }

    /*
    Recibe:
    ini coordenada desde la que se empieza X o Y dependiendo de cual sea el eje
    eje con 0 para X y 1 para Y
    avance con -1 para hacia atras y 1 hacia delante
     */
    private static int avanzar(Scene scene, int[] pos, int eje, int avance) {
        //variable auxiliar para no modificar la posicion original
        int baldosa[] = new int[] { pos[0], pos[1] };
        baldosa[eje] += avance;

        //limte al avanzar hacia delante
        int limiteHaciaDelante = 0;

        //valor dependiendo del eje
        if (eje == 0) limiteHaciaDelante = scene.getCols();
        else if (eje == 1) limiteHaciaDelante = scene.getRows();

        //baldosas iluminadas que no estaban ya iluminadas
        int puntuacion = 0;

        //si se ha salido de los limites para
        while (baldosa[eje] >= 0 && baldosa[eje] < limiteHaciaDelante) {
            //si no es ponderado y hay un 1 es una columna
            if (
                !scene.isPonderado() &&
                scene.getGrid()[baldosa[1]][baldosa[0]] == 1
            ) break;
            //si es ponderado y hay un 0 es una columna
            if (
                scene.isPonderado() &&
                scene.getGrid()[baldosa[1]][baldosa[0]] == 0
            ) break;

            //si no se ha visitado ya la balsdosa se suma la puntuacion
            if (!visited[baldosa[1]][baldosa[0]]) {
                //si no es ponderado se suma 1
                if (!scene.isPonderado()) puntuacion++;
                else puntuacion += scene.getGrid()[baldosa[0]][baldosa[1]];
            }
            visited[baldosa[1]][baldosa[0]] = true;

            //se coge la siguiente baldosa
            baldosa[eje] += avance;
        }

        return puntuacion;
    }
}
