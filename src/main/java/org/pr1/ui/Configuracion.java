package org.pr1.ui;

import org.pr1.SupplierFactory;
import org.pr1.ag.Simulator;
import org.pr1.ag.cruce.*;
import org.pr1.ag.mutacion.Mutacion;
import org.pr1.ag.mutacion.MutacionBit;
import org.pr1.ag.mutacion.MutacionGaussianaReal;
import org.pr1.ag.mutacion.MutacionGen;
import org.pr1.ag.seleccion.*;
import org.pr1.cromosomas.Cromosoma;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class Configuracion extends JPanel{

    public Configuracion() {
        setLayout(new GridBagLayout());

        /*
        Para colocar los elementos dentro del GridBag
        se va aumentando y para ir colocando los elementos debajo
         */
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Mapa
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Mapa:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> mapa = new JComboBox<>(new String[]{"Museo", "Pasillo", "SuperMercado"});
        add(mapa, gbc);
        y++;

        // Tipo de problema
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Tipo de problema:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> tipoProblema = new JComboBox<>(new String[]{"Binario", "Real"});
        add(tipoProblema, gbc);
        y++;

        // Poblacion
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Poblacion:"), gbc);

        gbc.gridx = 1;
        JSpinner poblacion = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
        add(poblacion, gbc);
        y++;

        // Numero de generaciones
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

        // Manera de selección
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Metodo de selección:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> seleccion = new JComboBox<>(
                new String[]{
                        "Ruleta",
                        "Torneo",
                        "Truncamiento",
                        "Estocastico",
                        "Restos"
                });
        add(seleccion, gbc);
        y++;

        // Manera de cruce
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Metodo de cruce:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> cruce = new JComboBox<>(
                new String[]{
                        "Aritmetico",
                        "BlxAlpha",
                        "MonoPuntoBin",
                        "MonoPuntoReal",
                        "UniformeBin",
                        "UniformeReal"
                });
        add(cruce, gbc);
        y++;

        // Forma de mutación
        gbc.gridx = 0;
        gbc.gridy = y;
        add(new JLabel("Método de mutación:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> mutacion = new JComboBox<>(
                new String[]{
                        "Bit",
                        "Gaussiana",
                        "Gen",
                });
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

        // Boton de ejecutar
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        //ACCION AL DARLA AL BOTON DE EJECUTAR
        JButton ejecutar = new JButton("Ejecutar");
        ejecutar.addActionListener(e -> {

            //si se ha seleccionado real o no
            String valor = (String) tipoProblema.getSelectedItem();
            boolean real = valor.equals("Real");

            //numero de individuos en la poblacion
            int individuos = (int) poblacion.getValue();

            //numero de generaciones
            int numGen = (int) generaciones.getValue();

            //porcentaje de cruces
            int porCruces = (int) cruces.getValue();

            //porcentaje de mutaciones
            int porMutaciones = (int) mutaciones.getValue();

            //metodo de seleccion
            String metodoS = (String) seleccion.getSelectedItem();
            Seleccion metodoSeleccion = null;
            switch (metodoS) {
                case "Ruleta":
                    metodoSeleccion = new Ruleta();
                    break;
                case "Torneo":
                    metodoSeleccion = new Torneo();
                    break;
                case "Truncamiento":
                    metodoSeleccion = new Truncamiento();
                    break;
                case "Estocastico":
                    metodoSeleccion = new Estocastico();
                    break;
                case "Restos":
                    metodoSeleccion = new Restos();
                    break;
            }

            //metodo de cruce
            String metodoC = (String) cruce.getSelectedItem();
            Cruce metodoCruce = null;
            switch (metodoC) {
                case "Aritmetico":
                    metodoCruce = new CruceAritmetico();
                    break;
                case "BlxAlpha":
                    metodoCruce = new CruceBlxAlpha(0); //TODO habria que poner aqui un selector para alpha?
                    break;
                case "MonoPuntoBin":
                    metodoCruce = new CruceMonopuntoBin();
                    break;
                case "MonoPuntoReal":
                    metodoCruce = new CruceMonopuntoReal();
                    break;
                case "UniformeBin":
                    metodoCruce = new CruceUniformeBin(0); //TODO habria que poner aqui un selector para prob?
                    break;
                case "UniformeReal":
                    metodoCruce = new CruceUniformeReal(0); //TODO habria que poner aqui un selector para prob?
            }

            String metodoM = (String) mutacion.getSelectedItem();
            Mutacion metodoMutacion = null;
            switch (metodoM) {
                case "Bit":
                    metodoMutacion = new MutacionBit();
                    break;
                case "Gaussiana":
                    metodoMutacion = new MutacionGaussianaReal();
                    break;
                case "Gen":
                    metodoMutacion = new MutacionGen();
                    break;
            }

            //porcentaje de elitismo
            int porElitismo = (int) elitismo.getValue();

            //modo ponderado
            boolean modoPonderado = ponderado.isSelected();

            //mapa
            String mapaAux = (String) mapa.getSelectedItem();

            //SUPPLIER
            Supplier<Cromosoma> supplier = SupplierFactory.getMapa(mapaAux, modoPonderado, real);

            //SE CREA EL NUEVO SIMULATOR
            Simulator simulator = new Simulator(
                    numGen,
                    individuos,
                    porCruces,
                    porMutaciones,
                    porElitismo,
                    metodoSeleccion,
                    metodoCruce,
                    metodoMutacion,
                    supplier
            );
            System.out.println("");
        });
        add(ejecutar, gbc);
    }
}
