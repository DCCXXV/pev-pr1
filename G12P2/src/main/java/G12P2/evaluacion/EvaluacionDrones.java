package G12P2.evaluacion;

import G12P2.Scene;
import G12P2.cromosomas.CromosomaDrones;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EvaluacionDrones {

    //records
    record Nodo(int X, int Y, int coste, int heuristica) {}

    record Pos(int X, int Y) {}

    record resAestrella(int coste, List<int[]> path) {}

    // caché de rutas A*, se invalida si cambia la escena
    private static final ConcurrentHashMap<Long, resAestrella> cacheAEstrella =
        new ConcurrentHashMap<>();
    private static volatile Scene escenaCached = null;

    public static ResEvaluacion evaluar(CromosomaDrones cromosoma, boolean multiObjetivo) {
        Scene scene = cromosoma.getScene();
        int[] hangar = scene.getInicio();
        int numCamaras = cromosoma.getNumCamaras();
        int numDrones = cromosoma.getNumDrones();

        // capturar como variables locales para el thread safety
        int[][] mapa = scene.getGrid();
        int[][] camaras = scene.getPosCamaras();
        boolean[][] gridCamaras = scene.getGridCamaras();

        // invalidar cache si cambia la escena
        if (escenaCached != scene) {
            synchronized (EvaluacionDrones.class) {
                if (escenaCached != scene) {
                    cacheAEstrella.clear();
                    escenaCached = scene;
                }
            }
        }

        int[] genes = cromosoma.getGenes();

        List<List<Integer>> recorridosDrones = new ArrayList<>();
        List<Integer> recorrido = new ArrayList<>();
        for (int i = 0; i < genes.length; i++) {
            if (genes[i] > numCamaras) {
                recorridosDrones.add(recorrido);
                recorrido = new ArrayList<>();
            } else {
                recorrido.add(genes[i]);
            }
        }
        recorridosDrones.add(recorrido);

        List<List<int[]>> caminos = new ArrayList<>();
        int[] costeAcumulado = new int[numDrones];
        for (int i = 0; i < recorridosDrones.size(); i++) {
            if (recorridosDrones.get(i).size() == 0) {
                caminos.add(new ArrayList<>());
                costeAcumulado[i] = 0;
                continue;
            }
            resAestrella res = procesarRecorridoDron(
                recorridosDrones.get(i),
                hangar,
                mapa,
                camaras,
                gridCamaras
            );
            caminos.add(res.path());
            costeAcumulado[i] = res.coste();
        }

        List<Double> velocidades = cromosoma.getVelocidades();
        double[] costesReales = new double[numDrones];
        double[] eficienciasDrones = new double[numDrones];
        double eficiencias = 0;
        double max = -1;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < numDrones; i++) {
            double costeReal = costeAcumulado[i] / velocidades.get(i);
            double eficiencia = costeAcumulado[i] * velocidades.get(i);
            costesReales[i] = costeReal;
            eficienciasDrones[i] = eficiencia;
            eficiencias += eficiencia;
            if (costeReal > max) max = costeReal;
            if (costeReal < min) min = costeReal;
        }


        double fitnessFinal = 0;
        if (multiObjetivo)
            fitnessFinal = max + ((max - min) * 0.5) + eficiencias;
        else
            fitnessFinal = max + ((max - min) * 0.5);

        return new ResEvaluacion(
            fitnessFinal,
            eficiencias,
            costesReales,
            eficienciasDrones,
            caminos,
            cromosoma
        );
    }

    private static resAestrella procesarRecorridoDron(
        List<Integer> recorrido,
        int[] hangar,
        int[][] mapa,
        int[][] camaras,
        boolean[][] gridCamaras
    ) {
        int costeAcumulado = 0;
        List<int[]> camino = new ArrayList<>();

        Pos currentPos = new Pos(hangar[0], hangar[1]);
        Pos objectivePos = new Pos(
            camaras[recorrido.get(0) - 1][1],
            camaras[recorrido.get(0) - 1][0]
        );

        resAestrella res = aEstrella(
            currentPos,
            objectivePos,
            mapa,
            camaras,
            gridCamaras
        );
        costeAcumulado += res.coste();
        camino.addAll(res.path());

        for (int i = 1; i < recorrido.size(); i++) {
            currentPos = new Pos(
                camaras[recorrido.get(i - 1) - 1][1],
                camaras[recorrido.get(i - 1) - 1][0]
            );
            objectivePos = new Pos(
                camaras[recorrido.get(i) - 1][1],
                camaras[recorrido.get(i) - 1][0]
            );
            res = aEstrella(
                currentPos,
                objectivePos,
                mapa,
                camaras,
                gridCamaras
            );
            costeAcumulado += res.coste();
            camino.addAll(res.path());
        }

        int aux = recorrido.get(recorrido.size() - 1) - 1;
        currentPos = new Pos(camaras[aux][1], camaras[aux][0]);
        objectivePos = new Pos(hangar[0], hangar[1]);
        res = aEstrella(currentPos, objectivePos, mapa, camaras, gridCamaras);
        costeAcumulado += res.coste();
        camino.addAll(res.path());

        return new resAestrella(costeAcumulado, camino);
    }

    private static resAestrella aEstrella(
        Pos ini,
        Pos fin,
        int[][] mapa,
        int[][] camaras,
        boolean[][] gridCamaras
    ) {
        // clave única para este par de posiciones en esta escena
        long key =
            ((long) (ini.X & 0xFFFF) << 48) |
            ((long) (ini.Y & 0xFFFF) << 32) |
            ((long) (fin.X & 0xFFFF) << 16) |
            (long) (fin.Y & 0xFFFF);

        resAestrella cached = cacheAEstrella.get(key);
        if (cached != null) return cached;

        boolean encontrado = false;
        Nodo objetivo = null;

        boolean[][] visited = new boolean[mapa.length][mapa[0].length];
        HashMap<Pos, Pos> parent = new HashMap<>();
        PriorityQueue<Nodo> queue = new PriorityQueue<>(
            Comparator.comparingInt(
                current -> current.coste() + current.heuristica()
            )
        );
        HashMap<Pos, Integer> costes = new HashMap<>();

        Nodo nodoIni = new Nodo(ini.X, ini.Y, 0, 0);
        queue.add(nodoIni);
        costes.put(new Pos(ini.X, ini.Y), 0);

        while (!queue.isEmpty() && !encontrado) {
            Nodo nodo = queue.poll();
            for (Nodo vecino : adyacentes(nodo, fin, mapa, gridCamaras)) {
                if (vecino.X() == fin.X && vecino.Y() == fin.Y) {
                    encontrado = true;
                    objetivo = vecino;
                    parent.put(
                        new Pos(vecino.X(), vecino.Y()),
                        new Pos(nodo.X(), nodo.Y())
                    );
                    break;
                }
                if (visited[vecino.Y()][vecino.X()]) continue;
                int heuristica = heuristicaManhattan(
                    vecino.X(),
                    vecino.Y(),
                    fin
                );
                int total = vecino.coste() + vecino.heuristica();
                if (
                    costes.containsKey(new Pos(vecino.X(), vecino.Y())) &&
                    costes.get(new Pos(vecino.X(), vecino.Y())) < total
                ) continue;
                queue.add(vecino);
                parent.put(
                    new Pos(vecino.X(), vecino.Y()),
                    new Pos(nodo.X(), nodo.Y())
                );
                costes.put(
                    new Pos(vecino.X(), vecino.Y()),
                    vecino.coste() + vecino.heuristica()
                );
            }
            visited[nodo.Y()][nodo.X()] = true;
        }

        List<int[]> path = new ArrayList<>();
        Pos current = new Pos(objetivo.X(), objetivo.Y());
        while (current.X != ini.X || current.Y != ini.Y) {
            path.add(new int[] { current.X, current.Y });
            current = parent.get(new Pos(current.X, current.Y));
        }
        path.add(new int[] { ini.X, ini.Y });
        Collections.reverse(path);

        resAestrella resultado = new resAestrella(objetivo.coste(), path);
        cacheAEstrella.put(key, resultado);
        return resultado;
    }

    private static Set<Nodo> adyacentes(
        Nodo nodo,
        Pos fin,
        int[][] mapa,
        boolean[][] gridCamaras
    ) {
        Set<Nodo> vecinos = new HashSet<>();

        int[][] dirs = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };
        for (int[] d : dirs) {
            int nx = nodo.X() + d[0];
            int ny = nodo.Y() + d[1];
            if (!posValida(mapa, nx, ny)) continue;
            int coste = costeCasilla(nx, ny, fin, mapa, gridCamaras);
            int heuristica = heuristicaManhattan(nx, ny, fin);
            vecinos.add(new Nodo(nx, ny, nodo.coste() + coste, heuristica));
        }
        return vecinos;
    }

    private static boolean posValida(int[][] mapa, int X, int Y) {
        if (
            Y < 0 || X < 0 || Y >= mapa.length || X >= mapa[0].length
        ) return false;
        return mapa[Y][X] != 0;
    }

    private static int costeCasilla(
        int X,
        int Y,
        Pos fin,
        int[][] mapa,
        boolean[][] gridCamaras
    ) {
        if (fin.X == X && fin.Y == Y) return mapa[Y][X];
        if (gridCamaras[Y][X]) return 500;
        return mapa[Y][X];
    }

    private static int heuristicaManhattan(int X, int Y, Pos fin) {
        return Math.abs(X - fin.X) + Math.abs(Y - fin.Y);
    }
}
