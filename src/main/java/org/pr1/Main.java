package org.pr1;

import org.pr1.ag.seleccion.Seleccion;
import org.pr1.ag.seleccion.Truncamiento;
import org.pr1.cromosomas.Cromosoma;
import org.pr1.cromosomas.CromosomaBinario;
import org.pr1.evaluacion.EvaluacionBinaria;
import org.pr1.ui.Interfaz;

import javax.swing.*;

public class Main {

    private static final Scene museo = new Scene(
        new int[][] {
            { 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
            { 0, 1, 0, 0, 0, 0, 0, 0, 1, 0 },
            { 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 1, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 1, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 1, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 1, 0, 0 },
        },
        false
    );

    public static void main(String[] args) {
//        System.out.print("     ");
//        for (int i = 0; i < museo.getGrid()[0].length; i++)
//            System.out.print(i + "  ");
//        System.out.println("\n");
//        for (int y = 0; y < museo.getGrid().length; y++) {
//            System.out.print(y + "    ");
//            for (int x = 0; x < museo.getGrid()[y].length; x++) {
//                System.out.print(museo.getGrid()[y][x] + "  ");
//            }
//            System.out.println();
//        }
//        CromosomaBinario prueba = new CromosomaBinario(5, museo);
        CromosomaBinario cromosomas[] = new CromosomaBinario[10];
        int fitness[] = new int[10];
        for (int i = 0; i < 10; i++) {
            cromosomas[i] = new CromosomaBinario(5, museo);
            fitness[i] = cromosomas[i].evaluar();
        }
        Seleccion metodo = new Truncamiento();
        Cromosoma newCromosomas[] = metodo.seleccionar(cromosomas, fitness);
        for (int i = 0; i < 10; i++) {
            fitness[i] = newCromosomas[i].evaluar();
        }


        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new Interfaz();
            ventana.setVisible(true);
        });

//        CromosomaBinario cromosomas[] = new CromosomaBinario[10];
//        int fitness[] = new int[10];
//        for (int i = 0; i < 10; i++) {
//            cromosomas[i] = new CromosomaBinario(5, museo);
//            fitness[i] = cromosomas[i].evaluar();
//        }
//        Seleccion metodo = new Truncamiento();
//        Cromosoma newCromosomas[] = metodo.seleccionar(cromosomas, fitness);
//        for (int i = 0; i < 10; i++) {
//            fitness[i] = newCromosomas[i].evaluar();
//        }
    }
}
