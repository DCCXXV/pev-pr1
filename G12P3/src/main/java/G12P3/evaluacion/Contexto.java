package G12P3.evaluacion;

public class Contexto {
    // El agente
    public int x, y, direccion; // dirección: 0=Norte,1=Este,2=Sur,3=Oeste
    public double energia = 100;
    public boolean vivo = true;
    public boolean accionTomada = false;

    // El entorno
    public int[][] mapa;          // tipo de celda en cada posición
    public boolean[][] visitado;

    // Las estadísticas
    public int ticks = 0, muestras = 0, colisiones = 0;
    public int girosConsecutivos = 0;

    public double leerSensor(TipoSensor tipo) {
        return switch (tipo) {
            case DIST_MUESTRA -> raycast(TipoCelda.MUESTRA);
            case DIST_ARENA -> raycast(TipoCelda.ARENA);
            case DIST_OBSTACULO -> raycast(TipoCelda.MURO);
            case NIVEL_ENERGIA -> energia;
        };
    }
}
