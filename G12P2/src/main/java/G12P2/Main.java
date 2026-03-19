package G12P2;

import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;
import G12P2.evaluacion.EvaluacionDrones;
import G12P2.evaluacion.resEvaluacion;
import G12P2.ui.Interfaz;
import G12P2.ui.Tablero;

import javax.swing.*;
import java.util.List;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame ventana = new Interfaz();
//            ventana.setVisible(true);
//        });

        //TODO quitar esto que es solo pruebas
        Scene scene = new Scene(Mapas.getMapa("SuperMercado"), Mapas.getInicio("SuperMercado"), 5, 3000);
        int[][] grid = Mapas.getMapa("SuperMercado");
        int[][] camaras = scene.getPosCamaras(); // las que sean

        Tablero tablero = new Tablero(grid, camaras);

        JFrame frame = new JFrame("Tablero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tablero);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        int[][] mapa = Mapas.getMapa("SuperMercado");
        int[] inicio = Mapas.getInicio("SuperMercado");
        Supplier<CromosomaDrones> supplier = () -> new CromosomaDrones(1, scene);
        CromosomaDrones cromosomaDrones = supplier.get();
        cromosomaDrones.setGenes(new int[]{1,2,3,4,5});
        resEvaluacion res = EvaluacionDrones.evaluar(cromosomaDrones);
        tablero.setRutas(res.getCaminos());
        tablero.repaint();
    }
}
