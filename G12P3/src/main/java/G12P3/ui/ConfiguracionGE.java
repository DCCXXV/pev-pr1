package G12P3.ui;

import G12P3.ag.Cromosoma;
import G12P3.ag.cruce.Cruce;
import G12P3.ag.mutacion.Mutacion;
import G12P3.ag.seleccion.Estocastico;
import G12P3.ag.seleccion.Ranking;
import G12P3.ag.seleccion.Restos;
import G12P3.ag.seleccion.Ruleta;
import G12P3.ag.seleccion.Seleccion;
import G12P3.ag.seleccion.Torneo;
import G12P3.ag.seleccion.Truncamiento;
import G12P3.evaluacion.MapaLunar;
import G12P3.ge.Gramatica;
import G12P3.ge.SimulatorGE;
import G12P3.ge.cruce.CruceAritmeticoGE;
import G12P3.ge.cruce.CruceBlxAlphaGE;
import G12P3.ge.cruce.CruceDosPuntosGE;
import G12P3.ge.cruce.CruceMonopuntoGE;
import G12P3.ge.cruce.CruceUniformeGE;
import G12P3.ge.mutacion.MutacionBasicaGE;
import G12P3.ge.mutacion.MutacionGaussianaGE;
import G12P3.ge.mutacion.MutacionInsercionGE;
import G12P3.ge.mutacion.MutacionIntercambioGE;
import G12P3.ge.mutacion.MutacionInversionGE;
import G12P3.ge.mutacion.MutacionScrambleGE;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.*;

public class ConfiguracionGE extends JPanel {

    private JSpinner semilla;
    private JSpinner poblacion;
    private JSpinner generaciones;
    private JSpinner porcentajeCruces;
    private JSpinner porcentajeMutaciones;
    private JSpinner longCromosoma;
    private JSpinner profundidadDecoder;
    private JSpinner coefBloat;
    private JSpinner elitismo;
    private JSpinner numMapas;
    private JSpinner numSimulaciones;
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
        int longCromosoma,
        int profDecoder,
        double coefBloat,
        double elitismo,
        int numMapas,
        int numSimulaciones,
        String seleccion,
        String cruce,
        String mutacion
    ) {}

    public ConfiguracionGE(
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
            new SpinnerNumberModel(85, 0, 100, 1)
        );
        add(porcentajeCruces, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de mutaciones (%):"), gbc);
        gbc.gridx = 1;
        this.porcentajeMutaciones = new JSpinner(
            new SpinnerNumberModel(25, 0, 100, 1)
        );
        add(porcentajeMutaciones, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Longitud cromosoma (codones):"), gbc);
        gbc.gridx = 1;
        this.longCromosoma = new JSpinner(
            new SpinnerNumberModel(80, 10, 1000, 1)
        );
        add(longCromosoma, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Profundidad máxima decoder:"), gbc);
        gbc.gridx = 1;
        this.profundidadDecoder = new JSpinner(
            new SpinnerNumberModel(6, 2, 10, 1)
        );
        add(profundidadDecoder, gbc);
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
        add(new JLabel("Número de mapas:"), gbc);
        gbc.gridx = 1;
        this.numMapas = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        add(numMapas, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Número de simulaciones:"), gbc);
        gbc.gridx = 1;
        this.numSimulaciones = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        add(numSimulaciones, gbc);
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
                "Monopunto",
                "Dos puntos",
                "Uniforme",
                "Aritmético",
                "BLX-α",
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
                "Básica",
                "Inversión",
                "Intercambio",
                "Inserción",
                "Scramble",
                "Gaussiana",
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
        this.cancelar = new JButton("Cancelar");
        this.cancelar.setVisible(false);
        this.cancelar.addActionListener(e -> {
            if (hilo != null) hilo.interrupt();
        });
        add(cancelar, gbc);
        y++;

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        this.labelVisualizarMapa = new JLabel("Visualizar mapa:");
        this.labelVisualizarMapa.setVisible(false);
        add(labelVisualizarMapa, gbc);
        gbc.gridx = 1;
        this.visualizarMapa = new JComboBox<>(
            new String[] { "Mapa 1", "Mapa 2", "Mapa 3" }
        );
        this.visualizarMapa.setVisible(false);
        this.visualizarMapa.addActionListener(e -> {
            int idx = visualizarMapa.getSelectedIndex();
            if (idx >= 0) tablero.setIndiceMapa(idx);
        });
        add(visualizarMapa, gbc);
        y++;

        // panel con la BNF para que el usuario vea las reglas activas
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        JTextArea bnf = new JTextArea(Gramatica.BNF);
        bnf.setEditable(false);
        bnf.setFont(new Font("Monospaced", Font.PLAIN, 11));
        bnf.setBackground(new Color(248, 248, 240));
        JScrollPane scrollBnf = new JScrollPane(bnf);
        scrollBnf.setBorder(BorderFactory.createTitledBorder("Gramática BNF"));
        add(scrollBnf, gbc);
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
        return switch (tipo) {
            case "Dos puntos" -> new CruceDosPuntosGE(rnd);
            case "Uniforme" -> new CruceUniformeGE(rnd);
            case "Aritmético" -> new CruceAritmeticoGE();
            case "BLX-α" -> new CruceBlxAlphaGE(rnd);
            default -> new CruceMonopuntoGE(rnd);
        };
    }

    private Mutacion crearMutacion(Random rnd, String tipo) {
        return switch (tipo) {
            case "Inversión" -> new MutacionInversionGE(rnd);
            case "Intercambio" -> new MutacionIntercambioGE(rnd);
            case "Inserción" -> new MutacionInsercionGE(rnd);
            case "Scramble" -> new MutacionScrambleGE(rnd);
            case "Gaussiana" -> new MutacionGaussianaGE(rnd);
            default -> new MutacionBasicaGE(rnd);
        };
    }

    private Datos recogerDatos() {
        return new Datos(
            ((Number) semilla.getValue()).longValue(),
            (int) poblacion.getValue(),
            (int) generaciones.getValue(),
            ((int) porcentajeCruces.getValue()) / 100.0,
            ((int) porcentajeMutaciones.getValue()) / 100.0,
            (int) longCromosoma.getValue(),
            (int) profundidadDecoder.getValue(),
            ((Number) coefBloat.getValue()).doubleValue(),
            ((int) elitismo.getValue()) / 100.0,
            (int) numMapas.getValue(),
            (int) numSimulaciones.getValue(),
            (String) seleccion.getSelectedItem(),
            (String) cruce.getSelectedItem(),
            (String) mutacion.getSelectedItem()
        );
    }

    private void rellenarComboMapas(int n) {
        visualizarMapa.removeAllItems();
        for (int i = 1; i <= n; i++) visualizarMapa.addItem("Mapa " + i);
    }

    private void iniciarSimulacion() {
        Datos datos = recogerDatos();
        estadoComponentes(false);
        labelVisualizarMapa.setVisible(false);
        visualizarMapa.setVisible(false);
        rellenarComboMapas(datos.numMapas);
        tablero.setIndiceMapa(0);
        grafica.setGeneraciones(datos.generaciones);
        fenotipo.limpiar();

        this.hilo = new Thread(() -> {
            try {
                if (datos.numSimulaciones == 1) {
                    Random rnd = new Random(datos.semilla);
                    Seleccion sel = crearSeleccion(rnd, datos.seleccion);
                    Cruce cru = crearCruce(rnd, datos.cruce);
                    Mutacion mut = crearMutacion(rnd, datos.mutacion);
                    new SimulatorGE(
                        datos.generaciones, datos.poblacion, datos.probCruce,
                        datos.probMutacion, datos.elitismo, datos.longCromosoma,
                        datos.profDecoder, datos.coefBloat, datos.semilla, datos.semilla,
                        datos.numMapas, sel, cru, mut, tablero, grafica, fenotipo
                    );
                } else {
                    double[] sumaMejorGen = new double[datos.generaciones];
                    double[] sumaMejorAbs = new double[datos.generaciones];
                    double[] sumaMedia = new double[datos.generaciones];
                    AtomicInteger simsCompletadas = new AtomicInteger(0);
                    AtomicReference<Cromosoma> mejorGlobal = new AtomicReference<>(null);

                    int numHilos = Math.min(datos.numSimulaciones, Runtime.getRuntime().availableProcessors());
                    ExecutorService pool = Executors.newFixedThreadPool(numHilos);
                    List<Future<?>> futures = new ArrayList<>();

                    for (int sim = 0; sim < datos.numSimulaciones; sim++) {
                        final int simIdx = sim;
                        futures.add(pool.submit(() -> {
                            long semillaSim = datos.semilla + simIdx;
                            Random rnd = new Random(semillaSim);
                            Seleccion sel = crearSeleccion(rnd, datos.seleccion);
                            Cruce cru = crearCruce(rnd, datos.cruce);
                            Mutacion mut = crearMutacion(rnd, datos.mutacion);
                            System.out.println("GE Simulación " + (simIdx + 1) + "/" + datos.numSimulaciones);
                            SimulatorGE s = new SimulatorGE(
                                datos.generaciones, datos.poblacion, datos.probCruce,
                                datos.probMutacion, datos.elitismo, datos.longCromosoma,
                                datos.profDecoder, datos.coefBloat, semillaSim, datos.semilla,
                                datos.numMapas, sel, cru, mut, null, null, null
                            );
                            synchronized (sumaMejorGen) {
                                double[] mg = s.getDatosMejorGen();
                                double[] ma = s.getDatosMejorAbs();
                                double[] me = s.getDatosMedia();
                                int completadas = simsCompletadas.incrementAndGet();
                                for (int g = 0; g < datos.generaciones; g++) {
                                    sumaMejorGen[g] += mg[g];
                                    sumaMejorAbs[g] += ma[g];
                                    sumaMedia[g] += me[g];
                                }
                                Cromosoma actual = mejorGlobal.get();
                                Cromosoma candidato = s.getMejorAbsoluto();
                                if (actual == null || candidato.fitness > actual.fitness)
                                    mejorGlobal.set(candidato);
                                double[] avgMG = new double[datos.generaciones];
                                double[] avgMA = new double[datos.generaciones];
                                double[] avgMe = new double[datos.generaciones];
                                for (int g = 0; g < datos.generaciones; g++) {
                                    avgMG[g] = sumaMejorGen[g] / completadas;
                                    avgMA[g] = sumaMejorAbs[g] / completadas;
                                    avgMe[g] = sumaMedia[g] / completadas;
                                }
                                grafica.mostrarDatosFinales(avgMG, avgMA, avgMe);
                            }
                        }));
                    }

                    pool.shutdown();
                    try {
                        pool.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        pool.shutdownNow();
                        Thread.currentThread().interrupt();
                    }

                    Cromosoma mejor = mejorGlobal.get();
                    if (mejor != null) {
                        tablero.setMejor(mejor);
                        fenotipo.setMejor(mejor);
                    }
                }
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
        longCromosoma.setEnabled(estado);
        profundidadDecoder.setEnabled(estado);
        coefBloat.setEnabled(estado);
        elitismo.setEnabled(estado);
        numMapas.setEnabled(estado);
        numSimulaciones.setEnabled(estado);
        seleccion.setEnabled(estado);
        cruce.setEnabled(estado);
        mutacion.setEnabled(estado);
        ejecutar.setVisible(estado);
        cancelar.setVisible(!estado);
    }

    public void inicializarVista() {
        generarTablero();
    }
}
