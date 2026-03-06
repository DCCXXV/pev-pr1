package G12P2.evaluacion;

import G12P2.Scene;
import G12P2.cromosomas.CromosomaDrones;

import java.util.*;

public class EvaluacionDrones {

    record Nodo(int X, int Y, int coste, int heuristica){}
    record Pos(int X, int Y){};
    private static int mapa[][];
    private static Pos fin;

    public static int evaluar(Scene scene, CromosomaDrones cromosoma) {

        //se saca el numero de camaras

        //se va iterando y se saca el coste de ir de camara a camara
        //los drones salen y vuelven a la casilla arriba a la izquierda

        //A* con heurística manhatan?

        //=======================================================
        mapa = scene.getGrid();
        //fin = new Pos(mapa[0].length - 2, mapa.length - 2);
        fin = new Pos(4, 6);
        return aEstrella(new Nodo(1, 1, 0, 0));
    }

    private static int aEstrella(Nodo ini) {

        //para parar la busqueda cuando se encuentre el objetivo
        boolean encontrado = false;
        Nodo objetivo = null;

        //para marcar los nodos ya contemplado y la reconstruccion
        boolean visited[][] = new boolean[mapa.length][mapa[0].length];
        HashMap<Pos, Pos> parent = new HashMap<>();

        //se crea una cola de prioridad que se ordena por la heuristica del nodo
        PriorityQueue<Nodo> queue = new PriorityQueue<>(
                Comparator.comparingInt(current -> {
                    return current.coste + current.heuristica;
                })
        );
        HashMap<Pos, Integer> costes = new HashMap<Pos, Integer>();

        //se inicializa
        queue.add(ini);
        costes.put(new Pos(ini.X, ini.Y), 0);

        while (!queue.isEmpty() && !encontrado) {
            //nodo actual
            Nodo nodo = queue.poll();

            //se mira cada uno de sus adyacentes
            for (Nodo vecino : adyacentes(nodo)) {
                //se encuentra el objetivo y se para la busqueda
                if (vecino.X == fin.X && vecino.Y == fin.Y) {
                    encontrado = true;
                    objetivo = vecino;
                    parent.put(new Pos(vecino.X, vecino.Y), new Pos(nodo.X, nodo.Y));
                    break;
                }

                //si ya se ha vistado se omite este nodo
                if (visited[vecino.Y][vecino.X])
                    continue;

                //si ya hay un nodo con esta posicion y menor f en la cola se omite este nodo
                int heuristica = heuristicaManhattan(vecino.X, vecino.Y);
                int total = vecino.coste + vecino.heuristica;
                if (costes.containsKey(new Pos(vecino.X, vecino.Y)) && costes.get(new Pos(vecino.X, vecino.Y)) < total)
                    continue;

                queue.add(vecino);
                parent.put(new Pos(vecino.X, vecino.Y), new Pos(nodo.X, nodo.Y));
                costes.put(new Pos(vecino.X, vecino.Y), vecino.coste + vecino.heuristica);
            }

            visited[nodo.Y][nodo.X] = true;
        }

//        for (Map.Entry<Pos, Pos> entry : parent.entrySet()) {
//            System.out.println("nodo: " + entry.getKey().X + " " + entry.getKey().Y + ", previo: " +
//                    entry.getValue().X + " " + entry.getValue().Y);
//        }
//
//        System.out.println("\n");

        if (encontrado && objetivo != null) {
            List<Pos> camino = new ArrayList<>();
            Pos actual = new Pos(objetivo.X, objetivo.Y);

            while (actual != null) {
                camino.add(actual);
                actual = parent.get(actual);
            }

            Collections.reverse(camino);

            System.out.println("Camino encontrado (" + camino.size() + " pasos):");
            for (Pos p : camino) {
                System.out.println("  -> (" + p.X + ", " + p.Y + ")");
            }
        }

        System.out.println("\nCoste: " + objetivo.coste);
        return objetivo.coste;
    }

    private static Set<Nodo> adyacentes(Nodo nodo) {
        Set<Nodo> vecinos = new HashSet<>();

        //TODO sumar la penalizacion por pasar por encima de una camara

        //ARRIBA
        if (posValida(mapa, nodo.X, nodo.Y - 1)) {
            int coste = mapa[nodo.Y - 1][nodo.X];
            int heuristica = heuristicaManhattan(nodo.X, nodo.Y - 1);
            Nodo vecino = new Nodo(nodo.X, nodo.Y - 1, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //DERECHA
        if (posValida(mapa, nodo.X + 1, nodo.Y)) {
            int coste = mapa[nodo.Y][nodo.X + 1];
            int heuristica = heuristicaManhattan(nodo.X + 1, nodo.Y);
            Nodo vecino = new Nodo(nodo.X + 1, nodo.Y, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //ABAJO
        if (posValida(mapa, nodo.X, nodo.Y + 1)) {
            int coste = mapa[nodo.Y + 1][nodo.X];
            int heuristica = heuristicaManhattan(nodo.X, nodo.Y + 1);
            Nodo vecino = new Nodo(nodo.X, nodo.Y + 1, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //IZQUIERDA
        if (posValida(mapa, nodo.X - 1, nodo.Y)) {
            int coste = mapa[nodo.Y][nodo.X - 1];
            int heuristica = heuristicaManhattan(nodo.X - 1, nodo.Y);
            Nodo vecino = new Nodo(nodo.X - 1, nodo.Y, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        return vecinos;
    }

    private static boolean posValida(int[][] mapa, int X, int Y) {
        //se sale del mapa
        if (Y < 0)
            return false;
        if (X < 0)
            return false;
        if (Y >= mapa.length)
            return false;
        if (X >= mapa[0].length)
            return false;

        //si es un muro
        if (mapa[Y][X] == 0)
            return false;

        return true;
    }

    private static int heuristicaManhattan(int X, int Y) {
        return Math.abs(X - fin.X) + Math.abs(Y - fin.Y);
    }
}
