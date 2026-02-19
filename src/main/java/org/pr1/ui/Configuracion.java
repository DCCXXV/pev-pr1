package org.pr1.ui;

import javax.swing.*;
import java.awt.*;

public class Configuracion extends JPanel{

    public Configuracion() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Tipo de problema
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tipo de problema:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> tipoProblema = new JComboBox<>(new String[]{"Binario", "Real"});
        add(tipoProblema, gbc);
        y++;

        // Tamaño de población
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tamaño de la población:"), gbc);

        gbc.gridx = 1;
        JSpinner poblacion = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        add(poblacion, gbc);
        y++;

        // Número de generaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Número de generaciones:"), gbc);

        gbc.gridx = 1;
        JSpinner generaciones = new JSpinner(new SpinnerNumberModel(200, 1, 10000, 1));
        add(generaciones, gbc);
        y++;

        // Porcentaje de cruces
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de cruces (%):"), gbc);

        gbc.gridx = 1;
        JSpinner cruces = new JSpinner(new SpinnerNumberModel(60, 0, 100, 1));
        add(cruces, gbc);
        y++;

        // Porcentaje de mutaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de mutaciones (%):"), gbc);

        gbc.gridx = 1;
        JSpinner mutaciones = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        add(mutaciones, gbc);
        y++;

        // Método de selección
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Método de selección:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> seleccion = new JComboBox<>(
                new String[]{"Ruleta", "Torneo", "Ranking"});
        add(seleccion, gbc);
        y++;

        // Método de cruce
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Método de cruce:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> cruce = new JComboBox<>(
                new String[]{"Un punto", "Dos puntos", "Uniforme"});
        add(cruce, gbc);
        y++;

        // Método de mutación
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Método de mutación:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> mutacion = new JComboBox<>(
                new String[]{"Simple", "Inversión", "Intercambio"});
        add(mutacion, gbc);
        y++;

        // Elitismo
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Elitismo (%):"), gbc);

        gbc.gridx = 1;
        JSpinner elitismo = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        add(elitismo, gbc);
        y++;

        // Modo ponderado
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Modo ponderado:"), gbc);

        gbc.gridx = 1;
        JCheckBox ponderado = new JCheckBox("Activar");
        add(ponderado, gbc);
        y++;

        // Botón ejecutar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton ejecutar = new JButton("Ejecutar");
        add(ejecutar, gbc);
    }
}
