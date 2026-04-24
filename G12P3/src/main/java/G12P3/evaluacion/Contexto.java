package G12P3.evaluacion;

import G12P3.arbol.NodoAst;

public class Contexto {

    public int x, y; // posicion (fila, columna)
    public int direccion; // 0=Norte, 1=Este, 2=Sur, 3=Oeste
    public double energia;
    public boolean vivo;
    public boolean accionTomada;

    public int[][] mapa; // 0=suelo, 1=muro, 2=muestra, 3=arena
    public boolean[][] visitado;

    public int ticks;
    public int muestrasRecogidas;
    public int casillasExploradas;
    public int recompensaVisual;
    public int pisadasArena;
    public int colisiones;
    public int girosConsecutivos;

    public final int MAX_TICKS = 150;
    private static final int ENERGIA_INICIAL = 100;

    // Norte, Este, Sur, Oeste
    private static final int[] DY = { -1, 0, 1, 0 };
    private static final int[] DX = { 0, 1, 0, -1 };

    // el rover empieza en (1,1) orientado al Este
    public Contexto(int[][] mapa) {
        this.mapa = copiarMapa(mapa); // copia para no mutar el original
        this.y = 1;
        this.x = 1;
        this.direccion = 1;
        this.energia = ENERGIA_INICIAL;
        this.vivo = true;
        this.accionTomada = false;

        this.visitado = new boolean[mapa.length][mapa[0].length];
        this.visitado[y][x] = true;
        this.casillasExploradas = 1;

        this.ticks = 0;
        this.muestrasRecogidas = 0;
        this.recompensaVisual = 0;
        this.pisadasArena = 0;
        this.colisiones = 0;
        this.girosConsecutivos = 0;
    }

    public ResultadoSimulacion simular(NodoAst arbol) {
        while (ticks < MAX_TICKS && vivo) {
            accionTomada = false;

            // reward shaping (+1 si el rover apunta a una muestra)
            if (
                raycast(2) < 100 // 2 = MUESTRA
            ) recompensaVisual++;

            arbol.ejecutar(this);
        }

        ResultadoSimulacion res = new ResultadoSimulacion();
        res.muestrasRecogidas = this.muestrasRecogidas;
        res.casillasExploradas = this.casillasExploradas;
        res.recompensaVisual = this.recompensaVisual;
        res.pisadasArena = this.pisadasArena;
        res.colisiones = this.colisiones;
        res.energiaRestante = this.energia;
        return res;
    }

    public double leerSensor(TipoSensor tipo) {
        return switch (tipo) {
            case DIST_MUESTRA -> raycast(2); // muestra
            case DIST_ARENA -> raycast(3); // arena
            case DIST_OBSTACULO -> raycast(1); // muro
            case NIVEL_ENERGIA -> energia;
        };
    }

    // devuelve distancia al objetivo o 100 si hay muro antes o no se encuentra
    private int raycast(int tipoBuscado) {
        int dy = DY[direccion];
        int dx = DX[direccion];
        int fy = y + dy;
        int fx = x + dx;
        int dist = 1;

        while (fy >= 0 && fy < mapa.length && fx >= 0 && fx < mapa[0].length) {
            int celda = mapa[fy][fx];

            if (tipoBuscado == 1) {
                if (celda == 1) return dist;
            } else {
                if (celda == 1) return 100;
                if (celda == tipoBuscado) return dist;
            }

            fy += dy;
            fx += dx;
            dist++;
        }

        return 100; // no encontrado o fuera de limites
    }

    public void avanzar() {
        if (accionTomada || !vivo) return;
        accionTomada = true;
        girosConsecutivos = 0;

        int ny = y + DY[direccion];
        int nx = x + DX[direccion];

        // fuera de limites (no deberia pasar con bordes de muro, pero por seguridad)
        if (ny < 0 || ny >= mapa.length || nx < 0 || nx >= mapa[0].length) {
            colisiones++;
            gastarEnergia(2);
            return;
        }

        int celda = mapa[ny][nx];

        switch (celda) {
            case 1: // muro
                colisiones++;
                gastarEnergia(2);
                break;
            case 2: // muestra - la recoge
                y = ny;
                x = nx;
                muestrasRecogidas++;
                mapa[ny][nx] = 0; // la casilla queda libre
                gastarEnergia(1);
                registrarVisita();
                break;
            case 3: // arena
                y = ny;
                x = nx;
                pisadasArena++;
                gastarEnergia(10);
                registrarVisita();
                break;
            default: // suelo normal
                y = ny;
                x = nx;
                gastarEnergia(1);
                registrarVisita();
                break;
        }
    }

    public void girarIzquierda() {
        if (accionTomada || !vivo) return;
        accionTomada = true;

        direccion = (direccion + 3) % 4; // +3 equiv a -1 mod 4
        gastarEnergia(1);
        girosConsecutivos++;
        comprobarMareo();
    }

    public void girarDerecha() {
        if (accionTomada || !vivo) return;
        accionTomada = true;

        direccion = (direccion + 1) % 4;
        gastarEnergia(1);
        girosConsecutivos++;
        comprobarMareo();
    }

    private void gastarEnergia(double coste) {
        energia -= coste;
        if (energia <= 0) {
            energia = 0;
            vivo = false;
        }
    }

    private void registrarVisita() {
        if (!visitado[y][x]) {
            visitado[y][x] = true;
            casillasExploradas++;
        }
    }

    // si 4+ giros consecutivos sin avanzar: -20 de energia
    private void comprobarMareo() {
        if (girosConsecutivos >= 4) {
            gastarEnergia(20);
            girosConsecutivos = 0;
        }
    }

    private int[][] copiarMapa(int[][] original) {
        int[][] copia = new int[original.length][];
        for (int i = 0; i < original.length; i++) copia[i] =
            original[i].clone();
        return copia;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDireccion() {
        return direccion;
    }

    public boolean[][] getVisitado() {
        return visitado;
    }
}
