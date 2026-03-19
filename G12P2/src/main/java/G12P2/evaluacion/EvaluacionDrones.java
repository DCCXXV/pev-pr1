package G12P2.evaluacion;

import G12P2.Scene;
import G12P2.cromosomas.CromosomaDrones;

import java.util.*;

public class EvaluacionDrones {

    //records
    record Nodo(int X, int Y, int coste, int heuristica){};
    record Pos(int X, int Y){};
    record resAestrella(int coste, List<int[]> path){};

    //mapa
    private static int mapa[][];
    private static int[][] camaras;

    public static resEvaluacion evaluar(CromosomaDrones cromosoma) {

        //escena
        Scene scene = cromosoma.getScene();

        //inicio, desde donde salen los drones
        int[] hangar = scene.getInicio();

        //se saca el numero de camaras y drones
        int numCamaras = cromosoma.getNumCamaras();
        int numDrones = cromosoma.getNumDrones();

        //saca el mapa y las posiciones de las camaras
        mapa = scene.getGrid();
        camaras = scene.getPosCamaras();

        //saca los genes de este cromosoma
        int[] genes = cromosoma.getGenes();

        //===== empieza la evaluacion =====

        //distancia desde el inicio a la primera camara
        int current = numCamaras+1;
        int objective = genes[0];
        Pos currentPos = new Pos(hangar[0], hangar[1]);
        Pos objectivePos = new Pos(camaras[objective - 1][1], camaras[objective - 1][0]);

        //lo que se devolvera al final
        int costeAcumulado = 0;
        //camino total
        List<int[]> path = new ArrayList<>();
        path.add(new int[]{hangar[0], hangar[1]});

        int i = 0;
        while (i < numCamaras + numDrones - 1) {
            //posiciones de las camaras
            //importante primero viene la Y y luego la X en el array de posiciones de las camara
            //TODO no solo hay que ir desde el inicio al principio sino cada vez que se cambie de dron
            //TODO falta dividir por la velocidad del dron

            //si se cumple esto se comienza el recorrido de un dron
            if (current > numCamaras)
                currentPos = new Pos(hangar[0], hangar[1]);
            else
                currentPos = new Pos(camaras[current - 1][1], camaras[current - 1][0]);

            //si se cumple esto se termina el recorrido del dron
            if (objective > numCamaras)
                objectivePos = new Pos(hangar[0], hangar[1]);
            else
                objectivePos = new Pos(camaras[objective - 1][1], camaras[objective - 1][0]);

            //se suma el coste y se suma registra el camino
            resAestrella res = aEstrella(currentPos, objectivePos);
            costeAcumulado += res.coste;
            path.addAll(res.path);

            //camara actual y camara objetivo actual
            current = genes[i];
            if (i+1 != genes.length)
                objective = genes[i + 1];
            else
                objective = numCamaras+1;

            i++;
        }
        resAestrella res = aEstrella(objectivePos, new Pos(hangar[0], hangar[1]));
        costeAcumulado += res.coste;
        path.addAll(res.path);

        List<List<int[]>> aux = new ArrayList<>();
        aux.add(path);

        return new resEvaluacion(
                costeAcumulado,
                aux
        );
    }

    private static resAestrella aEstrella(Pos ini, Pos fin) {

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
        Nodo nodoIni = new Nodo(ini.X,ini.Y,0,0);
        queue.add(nodoIni);
        costes.put(new Pos(ini.X, ini.Y), 0);

        while (!queue.isEmpty() && !encontrado) {
            //nodo actual
            Nodo nodo = queue.poll();

            //se mira cada uno de sus adyacentes
            for (Nodo vecino : adyacentes(nodo, fin)) {
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
                int heuristica = heuristicaManhattan(vecino.X, vecino.Y, fin);
                int total = vecino.coste + vecino.heuristica;
                if (costes.containsKey(new Pos(vecino.X, vecino.Y)) && costes.get(new Pos(vecino.X, vecino.Y)) < total)
                    continue;

                queue.add(vecino);
                parent.put(new Pos(vecino.X, vecino.Y), new Pos(nodo.X, nodo.Y));
                costes.put(new Pos(vecino.X, vecino.Y), vecino.coste + vecino.heuristica);
            }

            visited[nodo.Y][nodo.X] = true;
        }

        //voy desde el objetivo hasta el inicio e invierto para reconstruir el objetivo
        List<int[]> path = new ArrayList<>();
        Pos current = new Pos(objetivo.X, objetivo.Y);
        while (current.X != ini.X || current.Y != ini.Y) {
            path.add(new int[]{current.X,current.Y});
            current = parent.get(new Pos(current.X,current.Y));
        }
        Collections.reverse(path);

        return new resAestrella(objetivo.coste, path);
    }

    private static Set<Nodo> adyacentes(Nodo nodo, Pos fin) {
        Set<Nodo> vecinos = new HashSet<>();

        //ARRIBA
        if (posValida(mapa, nodo.X, nodo.Y - 1)) {
            int coste = costeCasilla(nodo.X, nodo.Y - 1, fin);
            int heuristica = heuristicaManhattan(nodo.X, nodo.Y - 1, fin);
            Nodo vecino = new Nodo(nodo.X, nodo.Y - 1, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //DERECHA
        if (posValida(mapa, nodo.X + 1, nodo.Y)) {
            int coste = costeCasilla(nodo.X + 1, nodo.Y, fin);
            int heuristica = heuristicaManhattan(nodo.X + 1, nodo.Y, fin);
            Nodo vecino = new Nodo(nodo.X + 1, nodo.Y, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //ABAJO
        if (posValida(mapa, nodo.X, nodo.Y + 1)) {
            int coste = costeCasilla(nodo.X, nodo.Y + 1, fin);
            int heuristica = heuristicaManhattan(nodo.X, nodo.Y + 1, fin);
            Nodo vecino = new Nodo(nodo.X, nodo.Y + 1, nodo.coste + coste, heuristica);
            vecinos.add(vecino);
        }

        //IZQUIERDA
        if (posValida(mapa, nodo.X - 1, nodo.Y)) {
            int coste = costeCasilla(nodo.X - 1, nodo.Y, fin);
            int heuristica = heuristicaManhattan(nodo.X - 1, nodo.Y, fin);
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

    private static int costeCasilla(int X, int Y, Pos fin) {
        //si es la camara objetivo avanzar solo cuesta 1
        if (fin.X == X && fin.Y == Y)
            return 1;

        //si es una camara que no es el objetivo avanzar a esa casilla es penalizado con 500 de coste
        if (mapa[Y][X] == -1)
            return 500;

        //de otra manera se devuelve el valor que esta presenten en la matriz del mapa
        return mapa[Y][X];
    }

    private static int heuristicaManhattan(int X, int Y, Pos fin) {
        return Math.abs(X - fin.X) + Math.abs(Y - fin.Y);
    }
}
