package G12P2.ui;

import G12P2.Mapas;
import G12P2.Scene;
import G12P2.ag.Simulator;
import G12P2.ag.cruce.*;
import G12P2.ag.mutacion.Mutacion;
import G12P2.ag.mutacion.MutacionInsercion;
import G12P2.ag.mutacion.MutacionIntercambio;
import G12P2.ag.seleccion.*;
import G12P2.cromosomas.Cromosoma;
import G12P2.cromosomas.CromosomaDrones;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class Configuracion extends JPanel{

    //componentes de la configuracion
    private JComboBox<String> mapa;
    private JSpinner numDrones;
    private JSpinner numCamaras;
    private JSpinner semilla;
    private JSpinner poblacion;
    private JSpinner generaciones;
    private JSpinner porcentajeCruces;
    private JSpinner porcentajeMutaciones;
    private JComboBox<String> seleccion;
    private JComboBox<String> cruce;
    private JSpinner auxiliar;
    private JComboBox<String> mutacion;
    private JSpinner elitismo;

    //referencia al tabler para actualizarlo
    Tablero tablero;

    //record con los datos que has seleccionado
    private record Datos(
            String mapa,
            int numDrones,
            int numCamaras,
            int semilla,
            int poblacion,
            int generaciones,
            double porcentajeCruces,
            double porcentajeMutaciones,
            Seleccion seleccion,
            Cruce cruce,
            Mutacion mutacion,
            double porcentajeElitismo
    ){};

    public Configuracion(Tablero tablero, Grafica grafica) {

        this.tablero = tablero;

        setLayout(new GridBagLayout());

        /*
        Para colocar los elementos dentro del GridBag
        se va aumentando y para ir colocando los elementos debajo
         */
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        int y = 0;

        // Mapa
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Mapa:"), gbc);

        gbc.gridx = 1;
        this.mapa = new JComboBox<>(new String[]{"Museo", "Pasillo", "SuperMercado"});
        add(mapa, gbc);
        y++;

        mapa.addActionListener(e -> {
            generarTablero();
        });

        // Numero de drones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Numero de drones:"), gbc);

        gbc.gridx = 1;
        this.numDrones = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        add(numDrones, gbc);
        y++;

        // Numero de camaras
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Numero de camaras:"), gbc);

        gbc.gridx = 1;
        this.numCamaras = new JSpinner(new SpinnerNumberModel(40, 1, 50, 1));
        add(numCamaras, gbc);
        y++;

        this.numCamaras.addChangeListener(e -> {
            generarTablero();
        });

        // Semilla
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Semilla para la generacion de camaras:"), gbc);

        gbc.gridx = 1;
        this.semilla = new JSpinner(new SpinnerNumberModel(3000, 1, Integer.MAX_VALUE, 1));
        add(semilla, gbc);
        y++;

        this.semilla.addChangeListener(e -> {
            generarTablero();
        });

        // Poblacion
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Poblacion:"), gbc);

        gbc.gridx = 1;
        this.poblacion = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        add(poblacion, gbc);
        y++;

        // Numero de generaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Número de generaciones:"), gbc);

        gbc.gridx = 1;
        this.generaciones = new JSpinner(new SpinnerNumberModel(200, 1, 10000, 1));
        add(generaciones, gbc);
        y++;

        // Porcentaje de cruces
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de cruces (%):"), gbc);

        gbc.gridx = 1;
        this.porcentajeCruces = new JSpinner(new SpinnerNumberModel(60, 0, 100, 1));
        add(porcentajeCruces, gbc);
        y++;

        // Porcentaje de mutaciones
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Porcentaje de mutaciones (%):"), gbc);

        gbc.gridx = 1;
        this.porcentajeMutaciones = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        add(porcentajeMutaciones, gbc);
        y++;

        // Manera de selección
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Metodo de selección:"), gbc);

        gbc.gridx = 1;
        this.seleccion = new JComboBox<>(
                new String[]{
                        "Ruleta",
                        "Torneo",
                        "Truncamiento",
                        "Estocastico",
                        "Restos",
                        "Ranking"
                });
        add(seleccion, gbc);
        y++;

        // Manera de cruce
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Metodo de cruce:"), gbc);

        gbc.gridx = 1;
        this.cruce = new JComboBox<>(
                new String[]{
                        "CruceA",
                        "CruceCO",
                        "CruceCX",
                        "CruceERX",
                        "CruceOX",
                        "CruceOXPP",
                        "CrucePMX"
                });
        add(cruce, gbc);
        y++;

        // Auxiliar Cruce
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Auxiliar (%):"), gbc);

        gbc.gridx = 1;
        this.auxiliar = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        add(auxiliar, gbc);
        y++;

        // Forma de mutación
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Método de mutación:"), gbc);

        gbc.gridx = 1;
        this.mutacion = new JComboBox<>(
                new String[]{
                        "Insercion",
                        "Intercambio"
                });
        add(mutacion, gbc);
        y++;

        // Elitismo
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Elitismo (%):"), gbc);

        gbc.gridx = 1;
        this.elitismo = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        add(elitismo, gbc);
        y++;

        // Boton de ejecutar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        //ACCION AL DARLA AL BOTON DE EJECUTAR
        JButton ejecutar = new JButton("Ejecutar");
        ejecutar.addActionListener(e -> {
            iniciarSimulacion();
        });

        add(ejecutar, gbc);

        generarTablero();
    }

    private void generarTablero() {
        String seleccionado = (String) this.mapa.getSelectedItem();
        int[][] mapa = Mapas.getMapa(seleccionado);
        int[] inicio = Mapas.getInicio(seleccionado);

        int semilla = (int) this.semilla.getValue();
        int numCamaras = (int) this.numCamaras.getValue();
        Scene scene = new Scene(mapa, inicio, numCamaras, semilla);

        int[][] camaras = scene.getPosCamaras();
        tablero.setTablero(Mapas.getMapa(seleccionado), camaras, 0, null);
    }

    private void iniciarSimulacion() {

        //recoge los datos
        Datos datos = recogerDatos();

        //genera la escena
        int[][] mapa = Mapas.getMapa(datos.mapa);
        int[] inicio = Mapas.getInicio(datos.mapa);
        Scene scene = new Scene(mapa, inicio, datos.numCamaras, datos.semilla);

        //genero el supplier de cromosomas con el numero de drones y la escena ya creada
        Supplier<Cromosoma> supplier = () -> new CromosomaDrones(datos.numDrones, scene);

        //metodo de cruce
        String metodoC = (String) cruce.getSelectedItem();
        Cruce metodoCruce = null;
        switch (metodoC) {
            case "CruceA":
                metodoCruce = new CruceA();
                break;
            case "CruceCO":
                metodoCruce = new CruceCO();
                break;
            case "CruceCX":
                metodoCruce = new CruceCX();
                break;
            case "CruceERX":
                metodoCruce = new CruceERX();
                break;
            case "CruceOX":
                metodoCruce = new CruceOX();
                break;
            case "CruceOXPP":
                metodoCruce = new CruceOXPP();
                break;
            case "CrucePMX":
                metodoCruce = new CrucePMX();
                break;
        }

        String metodoM = (String) mutacion.getSelectedItem();
        Mutacion metodoMutacion = null;
        switch (metodoM) {
            case "Insercion":
                metodoMutacion = new MutacionInsercion();
                break;
            case "Intercambio":
                metodoMutacion = new MutacionIntercambio();
                break;
        }

        //SE HACE LA SIGUIENTE SIMULACION
        Simulator simulator = new Simulator(
                datos.generaciones,
                datos.poblacion,
                datos.porcentajeCruces,
                datos.porcentajeMutaciones,
                (int) datos.porcentajeElitismo,
                datos.seleccion,
                datos.cruce,
                datos.mutacion,
                supplier,
                false, //TODO poner opcion para memetico
                tablero
        );

        //SE MUESTRAN LOS DATOS
        /*
        SimulatorResult result = simulator.getResultado();
        if (!modoPonderado)
            tablero.setTablero(result.getMapa(), null, result.getMejorFitness());
        else
            tablero.setTablero(result.getMapa(), Mapas.getMapa(mapaAux+"Ponderado"), result.getMejorFitness());
        tablero.revalidate();
        tablero.repaint();

        grafica.actualizarGrafica(
                result.getMejoresPorGeneracion(),
                result.getMejoresAbsolutos(),
                result.getMediaPorGeneracion()
        );
        grafica.revalidate();
        grafica.repaint();
        */
    }

    private Datos recogerDatos() {

        //mapa
        String mapaValue = (String) mapa.getSelectedItem();

        //numero de drones
        int numDronesValue = (int) numDrones.getValue();

        //numero de camaras
        int numCamarasValue = (int) numCamaras.getValue();

        //semilla
        int semillaValue = (int) semilla.getValue();

        //numero de individuos en la poblacion
        int poblacionValue = (int) poblacion.getValue();

        //numero de generaciones
        int generacionesValue = (int) generaciones.getValue();

        //porcentaje de cruces
        double porCrucesValue = ((int)porcentajeCruces.getValue()) / 100;

        //porcentaje de mutaciones
        int porMutaciones = ((int)porcentajeMutaciones.getValue()) / 100;

        //metodo de seleccion
        String seleccionAux = (String) seleccion.getSelectedItem();
        Seleccion seleccionValue = null;
        switch (seleccionAux) {
            case "Ruleta":
                seleccionValue = new Ruleta();
                break;
            case "Torneo":
                seleccionValue = new Torneo();
                break;
            case "Truncamiento":
                seleccionValue = new Truncamiento();
                break;
            case "Estocastico":
                seleccionValue = new Estocastico();
                break;
            case "Restos":
                seleccionValue = new Restos();
                break;
            case "Ranking":
                seleccionValue = new Ranking();
                break;
        }

        //metodo de cruce
        String cruceAux = (String) cruce.getSelectedItem();
        Cruce cruceValue = null;
        //TODO poner los cruces de esta practica
        switch (cruceAux) {
            case "CruceA":
                cruceValue = new CruceA();
                break;
            case "CruceCO":
                cruceValue = new CruceCO();
                break;
            case "CruceCX":
                cruceValue = new CruceCX();
                break;
            case "CruceERX":
                cruceValue = new CruceERX();
                break;
            case "CruceOX":
                cruceValue = new CruceOX();
                break;
            case "CruceOXPP":
                cruceValue = new CruceOXPP();
                break;
            case "CrucePMX":
                cruceValue = new CrucePMX();
                break;
        }

        //metodo de mutacion
        String mutacionAux = (String) mutacion.getSelectedItem();
        Mutacion mutacionValue = null;
        switch (mutacionAux) {
            case "Insercion":
                mutacionValue = new MutacionInsercion();
                break;
            case "Intercambio":
                mutacionValue = new MutacionIntercambio();
                break;
        }

        //porcentaje de elitismo
        double elitismoValue = ((int) elitismo.getValue());

        return new Datos(
                mapaValue,
                numDronesValue,
                numCamarasValue,
                semillaValue,
                poblacionValue,
                generacionesValue,
                porCrucesValue,
                porMutaciones,
                seleccionValue,
                cruceValue,
                mutacionValue,
                elitismoValue
        );
    }
}
