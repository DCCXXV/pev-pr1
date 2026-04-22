package G12P3.ui;

import G12P3.ag.Simulator;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ag.mutacion.MutacionAleatoria;
import G12P3.ag.mutacion.MutacionFuncional;
import G12P3.ag.mutacion.MutacionHoist;
import G12P3.ag.mutacion.MutacionSubarbol;
import G12P3.ag.mutacion.MutacionTerminal;
import G12P3.evaluacion.MapaLunar;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class Configuracion extends JPanel {

    private JSpinner semilla;
    private JSpinner poblacion;
    private JSpinner generaciones;
    private JSpinner porcentajeCruces;
    private JSpinner porcentajeMutaciones;
    private JSpinner profundidadMaxima;
    private JSpinner coefBloat;
    private JSpinner elitismo;
    private JComboBox<String> mutacion;
    private JButton ejecutar;
    private JButton cancelar;

    private final Tablero tablero;
    private final Grafica grafica;
    private final PanelFenotipo fenotipo;

    private volatile Thread hilo;

    private record Datos(
        long semilla,
        int poblacion,
        int generaciones,
        double probCruce,
        double probMutacion,
        int profundidadMax,
        double coefBloat,
        double elitismo,
        String mutacion
    ) {}

    public Configuracion(
        Tablero tablero,
        Grafica grafica,
        PanelFenotipo fenotipo
    ) {
        this.tablero = tablero;
        this.grafica = grafica;
        this.fenotipo = fenotipo;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        // Semilla
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Semilla:"), gbc);
        gbc.gridx = 1;
        this.semilla = new JSpinner(
            new SpinnerNumberModel(3000, 1, Integer.MAX_VALUE, 1)
        );
        add(semilla, gbc);
        y++;
        this.semilla.addChangeListener(e -> generarTablero());

        // Poblacion
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tamaño de población:"), gbc);
        gbc.gridx = 1;
        this.poblacion = new JSpinner(new SpinnerNumberModel(300, 1, 10000, 1));
        add(poblacion, gbc);
        y++;

        // Generaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Número de generaciones:"), gbc);
        gbc.gridx = 1;
        this.generaciones = new JSpinner(
            new SpinnerNumberModel(300, 1, 10000, 1)
        );
        add(generaciones, gbc);
        y++;

        // % cruces
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de cruces (%):"), gbc);
        gbc.gridx = 1;
        this.porcentajeCruces = new JSpinner(
            new SpinnerNumberModel(70, 0, 100, 1)
        );
        add(porcentajeCruces, gbc);
        y++;

        // % mutaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de mutaciones (%):"), gbc);
        gbc.gridx = 1;
        this.porcentajeMutaciones = new JSpinner(
            new SpinnerNumberModel(10, 0, 100, 1)
        );
        add(porcentajeMutaciones, gbc);
        y++;

        // Profundidad maxima
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Profundidad máxima inicial:"), gbc);
        gbc.gridx = 1;
        this.profundidadMaxima = new JSpinner(
            new SpinnerNumberModel(5, 2, 10, 1)
        );
        add(profundidadMaxima, gbc);
        y++;

        // Coef bloat
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Coeficiente de bloating:"), gbc);
        gbc.gridx = 1;
        this.coefBloat = new JSpinner(
            new SpinnerNumberModel(0.5, 0.0, 100.0, 0.1)
        );
        add(coefBloat, gbc);
        y++;

        // Elitismo
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Elitismo (%):"), gbc);
        gbc.gridx = 1;
        this.elitismo = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        add(elitismo, gbc);
        y++;

        // Tipo de mutacion
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tipo de mutación:"), gbc);
        gbc.gridx = 1;
        this.mutacion = new JComboBox<>(
            new String[] {
                "Aleatoria",
                "Sub-árbol",
                "Funcional",
                "Terminal",
                "Hoist",
            }
        );
        add(mutacion, gbc);
        y++;

        // Ejecutar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.ejecutar = new JButton("Ejecutar");
        this.ejecutar.addActionListener(e -> iniciarSimulacion());
        add(ejecutar, gbc);
        y++;

        // Cancelar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        this.cancelar = new JButton("Cancelar");
        this.cancelar.setVisible(false);
        this.cancelar.addActionListener(e -> {
            if (hilo != null) hilo.interrupt();
        });
        add(cancelar, gbc);

        generarTablero();
    }

    private void generarTablero() {
        long s = ((Number) semilla.getValue()).longValue();
        int[][] mapa = MapaLunar.generar(s);
        tablero.setMapaBase(mapa);
        grafica.setGeneraciones(10);
        fenotipo.limpiar();
    }

    private Mutacion crearMutacion(Random rnd, int profMax, String tipo) {
        return switch (tipo) {
            case "Sub-árbol" -> new MutacionSubarbol(rnd, profMax);
            case "Funcional" -> new MutacionFuncional(rnd);
            case "Terminal" -> new MutacionTerminal(rnd);
            case "Hoist" -> new MutacionHoist(rnd);
            default -> new MutacionAleatoria(rnd, profMax);
        };
    }

    private Datos recogerDatos() {
        return new Datos(
            ((Number) semilla.getValue()).longValue(),
            (int) poblacion.getValue(),
            (int) generaciones.getValue(),
            ((int) porcentajeCruces.getValue()) / 100.0,
            ((int) porcentajeMutaciones.getValue()) / 100.0,
            (int) profundidadMaxima.getValue(),
            ((Number) coefBloat.getValue()).doubleValue(),
            ((int) elitismo.getValue()) / 100.0,
            (String) mutacion.getSelectedItem()
        );
    }

    private void iniciarSimulacion() {
        Datos datos = recogerDatos();
        estadoComponentes(false);
        grafica.setGeneraciones(datos.generaciones);
        fenotipo.limpiar();

        this.hilo = new Thread(() -> {
            try {
                Random rnd = new Random(datos.semilla);
                Mutacion mut = crearMutacion(
                    rnd,
                    datos.profundidadMax,
                    datos.mutacion
                );

                new Simulator(
                    datos.generaciones,
                    datos.poblacion,
                    datos.probCruce,
                    datos.probMutacion,
                    datos.elitismo,
                    datos.profundidadMax,
                    datos.coefBloat,
                    datos.semilla,
                    mut,
                    tablero,
                    grafica,
                    fenotipo
                );
            } finally {
                SwingUtilities.invokeLater(() -> estadoComponentes(true));
            }
        });

        this.hilo.start();
    }

    private void estadoComponentes(boolean estado) {
        semilla.setEnabled(estado);
        poblacion.setEnabled(estado);
        generaciones.setEnabled(estado);
        porcentajeCruces.setEnabled(estado);
        porcentajeMutaciones.setEnabled(estado);
        profundidadMaxima.setEnabled(estado);
        coefBloat.setEnabled(estado);
        elitismo.setEnabled(estado);
        mutacion.setEnabled(estado);
        ejecutar.setVisible(estado);
        cancelar.setVisible(!estado);
    }
}
