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
import G12P2.evaluacion.ResEvaluacion;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.*;

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
    private JComboBox<String> mutacion;
    private JSpinner elitismo;
    private JCheckBox memetico;
    private JCheckBox memeticoElite;
    private JSpinner porcentajeMemetico;
    private JButton ejecutar;
    private JButton cancelar;
    private JLabel labelOptimos;
    private JComboBox<String> optimos;

    //referencia al tablero y la grafica para actualizarlos
    Tablero tablero;
    Grafica grafica;
    List<ResEvaluacion> resOptimos;

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
            double porcentajeElitismo,
            boolean memetico,
            boolean memeticoElite,
            double porcentajeMemetico
    ){};

    public Configuracion(Tablero tablero, Grafica grafica) {

        //me guardo las referencias
        this.tablero = tablero;
        this.grafica = grafica;
        this.resOptimos = new ArrayList<>();

        //layout del panel
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
        this.mapa.setSelectedIndex(2);
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
        this.numDrones = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
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

        //Elitismo
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Elitismo (%):"), gbc);

        gbc.gridx = 1;
        this.elitismo = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        add(elitismo, gbc);
        y++;

        this.elitismo.addChangeListener(e -> {
            if ((int)elitismo.getValue() == 0 || !this.memetico.isSelected()){
                this.memeticoElite.setEnabled(false);
                this.porcentajeMemetico.setEnabled(false);
                this.memeticoElite.setSelected(false);
            }
        });

        //Memetico
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Memético:"), gbc);

        gbc.gridx = 1;
        this.memetico = new JCheckBox();
        add(memetico, gbc);
        y++;

        this.memetico.addChangeListener(e -> {
            if ((int)this.elitismo.getValue() != 0) {
                this.memeticoElite.setEnabled(memetico.isSelected());
                this.porcentajeMemetico.setEnabled(memetico.isSelected());
                if (!memetico.isSelected()) {
                    this.memeticoElite.setSelected(false);
                }
            }
        });

        //Memetico elite
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Memético Élite:"), gbc);

        gbc.gridx = 1;
        this.memeticoElite = new JCheckBox();
        this.memeticoElite.setEnabled(false);
        add(memeticoElite, gbc);
        y++;

        //Porcentaje memetico
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Memetico (%):"), gbc);

        gbc.gridx = 1;
        this.porcentajeMemetico = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));
        this.porcentajeMemetico.setEnabled(false);
        add(porcentajeMemetico, gbc);
        y++;

        // Boton de ejecutar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        this.ejecutar = new JButton("Ejecutar");
        this.ejecutar.addActionListener(e -> {
            iniciarSimulacion();
        });

        add(ejecutar, gbc);
        y++;

        //boton para cancelar la ejecucion
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        this.cancelar = new JButton("Cancelar");
        add(cancelar, gbc);
        this.cancelar.setVisible(false);
        y += 2;

        //Optimos de pareto
        gbc.gridx = 0;
        gbc.gridy = y;
        this.labelOptimos = new JLabel("Optimos de pareto:");
        add(labelOptimos, gbc);
        this.labelOptimos.setVisible(false);

        gbc.gridx = 1;
        this.optimos = new JComboBox<>();
        add(optimos, gbc);
        this.optimos.setVisible(false);
        y++;

        this.optimos.addActionListener(e -> {
            int i = this.optimos.getSelectedIndex();
            ResEvaluacion resAux = this.resOptimos.get(i);
            tablero.setMejor(
                    resAux.getFitness(),
                    resAux.getEnergia(),
                    resAux.getTiemposDrones(),
                    resAux.getEnergiaDrones(),
                    resAux.getCaminos(),
                    resAux.getCromosoma().getGenes()
            );
        });

        //genera el tablero al terminar
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
        grafica.setGeneraciones(10);

        this.resOptimos = new ArrayList<>();
        this.labelOptimos.setVisible(false);
        this.optimos.setVisible(false);
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

        //SE HACE LA SIGUIENTE SIMULACION EN UN HILO APARTE PARA NO BLOQUEAR LA UI
        Thread hilo = new Thread(() -> {
            //desactiva los componentes
            estadoComponentes(false);
            this.grafica.setGeneraciones(datos.generaciones);
            this.resOptimos = new ArrayList<>();
            this.labelOptimos.setVisible(false);
            this.optimos.setVisible(false);

            Simulator simulator = new Simulator(
                    datos.generaciones,
                    datos.poblacion,
                    datos.porcentajeCruces,
                    datos.porcentajeMutaciones,
                    datos.porcentajeElitismo,
                    datos.seleccion,
                    datos.cruce,
                    datos.mutacion,
                    supplier,
                    datos.memetico,
                    datos.memeticoElite,
                    datos.porcentajeMemetico,
                    tablero,
                    grafica
            );

            List<ResEvaluacion> datosOptimos = simulator.getOptimos();
            if (datosOptimos != null) {
                String[] nombresOptimos =  new String[datosOptimos.size()];
//                for (int i = 0; i < datosOptimos.size(); i++) {
//                    nombresOptimos[i] = "Optimo T: " + String.format("%-8.2f", datosOptimos.get(i).getFitness())
//                            + " E:  " + String.format("%.2f", datosOptimos.get(i).getEnergia());
//                    this.resOptimos.add(datosOptimos.get(i));
//                }

                //pongo los datos en la estructura
                for (int i = 0; i < datosOptimos.size(); i++)
                    this.resOptimos.add(datosOptimos.get(i));

                //pongo los nombres al combobox
                nombresOptimos[0] = "Ruta Emergencia Rápida";
                int rapidoIdx = 1;
                int ecoIdx = 1;
                for (double i = 1; i < datosOptimos.size() - 1; i++) {
                    if (i < (datosOptimos.size()-1) / 2.0) {
                        nombresOptimos[(int)i] = "Equilibrado Rápido " + rapidoIdx;
                        rapidoIdx++;
                    }
                    else if (i > (datosOptimos.size()-1) / 2.0) {
                        nombresOptimos[(int)i] = "Equilibrado Eco " + ecoIdx;
                        ecoIdx++;
                    }
                    else
                        nombresOptimos[(int)i] = "Equilibrado";
                }
                nombresOptimos[datosOptimos.size()-1] = "Ruta Eco";

                this.optimos.setModel(new DefaultComboBoxModel<>(nombresOptimos));
                this.optimos.setVisible(true);
                this.labelOptimos.setVisible(true);
            }

            //los vuelve a activar
            estadoComponentes(true);
        });

        this.cancelar.addActionListener(e -> {
           hilo.interrupt();
           estadoComponentes(true);
        });
        hilo.start();
    }

    private void estadoComponentes(boolean estado) {
        mapa.setEnabled(estado);
        numDrones.setEnabled(estado);
        numCamaras.setEnabled(estado);
        semilla.setEnabled(estado);
        poblacion.setEnabled(estado);
        generaciones.setEnabled(estado);
        porcentajeCruces.setEnabled(estado);
        porcentajeMutaciones.setEnabled(estado);
        seleccion.setEnabled(estado);
        cruce.setEnabled(estado);
        mutacion.setEnabled(estado);
        elitismo.setEnabled(estado);
        this.memetico.setEnabled(estado);
        this.memeticoElite.setEnabled(estado);
        this.porcentajeMemetico.setEnabled(estado);
        this.ejecutar.setVisible(estado);
        this.cancelar.setVisible(!estado);
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
        double porCrucesValue = ((int)porcentajeCruces.getValue()) / 100.0;

        //porcentaje de mutaciones
        double porMutaciones = ((int)porcentajeMutaciones.getValue()) / 100.0;

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
        double elitismoValue = ((int) elitismo.getValue()) / 100.0;

        boolean memetico = this.memetico.isSelected();

        boolean memeticoElite = this.memeticoElite.isSelected();

        //porcentaje de elitismo
        double porcentajeMemetico = ((int) this.porcentajeMemetico.getValue()) / 100.0;

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
                elitismoValue,
                memetico,
                memeticoElite,
                porcentajeMemetico
        );
    }
}
