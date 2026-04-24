package G12P3.evaluacion;

import java.util.Random;

public class MapaLunar {

    public static final int ANCHO = 15;
    public static final int ALTO = 15;

    // 0=suelo, 1=muro, 2=muestra, 3=arena; bordes muro, (1,1) siempre libre
    public static int[][] generar(long semilla) {
        int[][] mapa = new int[ALTO][ANCHO];
        Random rand = new Random(semilla);

        for (int i = 0; i < ALTO; i++) {
            for (int j = 0; j < ANCHO; j++) {
                if (
                    i == 0 || i == ALTO - 1 || j == 0 || j == ANCHO - 1
                ) mapa[i][j] = 1;
                else if (
                    rand.nextDouble() < 0.15 && (i != 1 || j != 1)
                ) mapa[i][j] = 1;
                else if (
                    rand.nextDouble() < 0.15 && (i != 1 || j != 1)
                ) mapa[i][j] = 2;
                else if (
                    rand.nextDouble() < 0.08 && (i != 1 || j != 1)
                ) mapa[i][j] = 3;
            }
        }
        return mapa;
    }
}
