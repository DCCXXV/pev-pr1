package G12P3.ui;

import G12P3.ag.Simulator;
import G12P3.ag.cruce.Cruce;
import G12P3.ag.cruce.CruceSubArboles;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ag.mutacion.MutacionAleatoria;
import G12P3.ag.mutacion.MutacionFuncional;
import G12P3.ag.mutacion.MutacionHoist;
import G12P3.ag.mutacion.MutacionSubarbol;
import G12P3.ag.mutacion.MutacionTerminal;
import G12P3.ag.seleccion.Estocastico;
import G12P3.ag.seleccion.Ranking;
import G12P3.ag.seleccion.Restos;
import G12P3.ag.seleccion.Ruleta;
import G12P3.ag.seleccion.Seleccion;
import G12P3.ag.seleccion.Torneo;
import G12P3.ag.seleccion.Truncamiento;
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
    private JComboBox<String> seleccion;
    private JComboBox<String> cruce;
    private JComboBox<String> mutacion;
    private JLabel labelVisualizarMapa;
    private JComboBox<String> visualizarMapa;
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
        String seleccion,
        String cruce,
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

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tamaño de población:"), gbc);
        gbc.gridx = 1;
        this.poblacion = new JSpinner(new SpinnerNumberModel(300, 1, 10000, 1));
        add(poblacion, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Número de generaciones:"), gbc);
        gbc.gridx = 1;
        this.generaciones = new JSpinner(
            new SpinnerNumberModel(400, 1, 10000, 1)
        );
        add(generaciones, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de cruces (%):"), gbc);
        gbc.gridx = 1;
        this.porcentajeCruces = new JSpinner(
            new SpinnerNumberModel(90, 0, 100, 1)
        );
        add(porcentajeCruces, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de mutaciones (%):"), gbc);
        gbc.gridx = 1;
        this.porcentajeMutaciones = new JSpinner(
            new SpinnerNumberModel(20, 0, 100, 1)
        );
        add(porcentajeMutaciones, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Profundidad máxima inicial:"), gbc);
        gbc.gridx = 1;
        this.profundidadMaxima = new JSpinner(
            new SpinnerNumberModel(6, 2, 10, 1)
        );
        add(profundidadMaxima, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Coeficiente de bloating:"), gbc);
        gbc.gridx = 1;
        this.coefBloat = new JSpinner(
            new SpinnerNumberModel(1.5, 0.0, 100.0, 0.1)
        );
        add(coefBloat, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Elitismo (%):"), gbc);
        gbc.gridx = 1;
        this.elitismo = new JSpinner(new SpinnerNumberModel(3, 0, 100, 1));
        add(elitismo, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tipo de selección:"), gbc);
        gbc.gridx = 1;
        this.seleccion = new JComboBox<>(
            new String[] {
                "Ruleta",
                "Torneo",
                "Ranking",
                "Estocástico",
                "Restos",
                "Truncamiento",
            }
        );
        add(seleccion, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tipo de cruce:"), gbc);
        gbc.gridx = 1;
        this.cruce = new JComboBox<>(
            new String[] {
                "Sub-árboles",
            }
        );
        add(cruce, gbc);
        y++;

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

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        this.ejecutar = new JButton("Ejecutar");
        this.ejecutar.addActionListener(e -> iniciarSimulacion());
        add(ejecutar, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        this.cancelar = new JButton("Cancelar");
        this.cancelar.setVisible(false);
        this.cancelar.addActionListener(e -> {
            if (hilo != null) hilo.interrupt();
        });
        add(cancelar, gbc);
        y++;

        // oculto hasta que termine la ejecucion
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.labelVisualizarMapa = new JLabel("Visualizar mapa:");
        this.labelVisualizarMapa.setVisible(false);
        add(labelVisualizarMapa, gbc);
        gbc.gridx = 1;
        this.visualizarMapa = new JComboBox<>(new String[] { "Mapa 1", "Mapa 2", "Mapa 3" });
        this.visualizarMapa.setVisible(false);
        this.visualizarMapa.addActionListener(e ->
            tablero.setIndiceMapa(visualizarMapa.getSelectedIndex())
        );
        add(visualizarMapa, gbc);

        generarTablero();
    }

    private void generarTablero() {
        long s = ((Number) semilla.getValue()).longValue();
        int[][] mapa = MapaLunar.generar(s);
        tablero.setMapaBase(mapa);
        grafica.setGeneraciones(10);
        fenotipo.limpiar();
    }

    private Seleccion crearSeleccion(Random rnd, String tipo) {
        return switch (tipo) {
            case "Torneo" -> new Torneo(rnd);
            case "Ranking" -> new Ranking(rnd);
            case "Estocástico" -> new Estocastico(rnd);
            case "Restos" -> new Restos(rnd);
            case "Truncamiento" -> new Truncamiento();
            default -> new Ruleta(rnd);
        };
    }

    private Cruce crearCruce(Random rnd, String tipo) {
        return new CruceSubArboles(rnd);
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
            (String) seleccion.getSelectedItem(),
            (String) cruce.getSelectedItem(),
            (String) mutacion.getSelectedItem()
        );
    }

    private void iniciarSimulacion() {
        Datos datos = recogerDatos();
        estadoComponentes(false);
        labelVisualizarMapa.setVisible(false);
        visualizarMapa.setVisible(false);
        visualizarMapa.setSelectedIndex(0);
        tablero.setIndiceMapa(0);
        grafica.setGeneraciones(datos.generaciones);
        fenotipo.limpiar();

        this.hilo = new Thread(() -> {
            try {
                Random rnd = new Random(datos.semilla);
                Seleccion sel = crearSeleccion(rnd, datos.seleccion);
                Cruce cru = crearCruce(rnd, datos.cruce);
                Mutacion mut = crearMutacion(rnd, datos.profundidadMax, datos.mutacion);

                new Simulator(
                    datos.generaciones,
                    datos.poblacion,
                    datos.probCruce,
                    datos.probMutacion,
                    datos.elitismo,
                    datos.profundidadMax,
                    datos.coefBloat,
                    datos.semilla,
                    sel,
                    cru,
                    mut,
                    tablero,
                    grafica,
                    fenotipo
                );
            } finally {
                SwingUtilities.invokeLater(() -> {
                    estadoComponentes(true);
                    labelVisualizarMapa.setVisible(true);
                    visualizarMapa.setVisible(true);
                });
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
        seleccion.setEnabled(estado);
        cruce.setEnabled(estado);
        mutacion.setEnabled(estado);
        ejecutar.setVisible(estado);
        cancelar.setVisible(!estado);
    }
}
