package G12P2;

import G12P2.cromosomas.CromosomaDrones;
import G12P2.evaluacion.EvaluacionDrones;
import G12P2.evaluacion.ResEvaluacion;
import G12P2.ui.Interfaz;
import G12P2.ui.Tablero;

import javax.swing.*;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new Interfaz();
            ventana.setVisible(true);
        });

//        //TODO quitar esto que es solo pruebas
//        int numCamaras = 8;
//        Scene scene = new Scene(Mapas.getMapa("SuperMercado"), Mapas.getInicio("SuperMercado"), numCamaras, 3000);
//        int[][] grid = Mapas.getMapa("SuperMercado");
//        int[][] camaras = scene.getPosCamaras(); // las que sean
//
//        Tablero tablero = new Tablero(grid, camaras);
//
//        JFrame frame = new JFrame("Tablero");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.add(tablero);
//        frame.setSize(600, 600);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//
//        int[][] mapa = Mapas.getMapa("SuperMercado");
//        int[] inicio = Mapas.getInicio("SuperMercado");
//        Supplier<CromosomaDrones> supplier = () -> new CromosomaDrones(5, scene);
//        CromosomaDrones cromosomaDrones = supplier.get();
//        cromosomaDrones.setGenes(new int[]{9,1,2,10,3,11,4,12,5,6,7,8});
//        ResEvaluacion res = EvaluacionDrones.evaluar(cromosomaDrones);
//        tablero.setMejor(res.getFitness(), res.getTiemposDrones(), res.getCaminos(), res.getCromosoma().getGenes());
//        tablero.repaint();
    }
}
