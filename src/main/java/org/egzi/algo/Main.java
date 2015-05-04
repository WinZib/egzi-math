package org.egzi.algo;

import org.egzi.algo.funcs.Functions;
import org.egzi.math.DenseVector;
import org.egzi.model.CustomTableModel;
import org.egzi.ui.MainWindow;
import org.egzi.ui.PlotContainer;
import org.egzi.ui.PlotContainerImpl;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;


public class Main {
    PlotContainer plot = new PlotContainerImpl("Result", "Iteration", "Error");

    final JTextField wVector = new JTextField(30);
    final JTextField yVector = new JTextField(30);
    final JTextField inputEpsilon = new JTextField("0.02", 50);
    final JTextField kaczmarzGamma = new JTextField("1.0", 50);
    final JTextField kaczmarzMu = new JTextField("0.0", 50);
    final JComboBox<GenType> outputNoize = new JComboBox<GenType>(new GenType[]{GenType.NO_GEN, GenType.GAUSIAN, GenType.UNIFORM});
    final JTextField outputDownEdge = new JTextField("-1.0");
    final JTextField outputUpEdge = new JTextField("1.0");
    final JComboBox<Functions> funcList = new JComboBox<Functions>(new Functions[]{Functions.NO, Functions.ONE, Functions.TWO});
    final JTextField maxIteration = new JTextField("10000", 50);
    final CustomTableModel tableModel = new CustomTableModel();
    final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(5));
    volatile IterationMethod currentMethod = null;


    protected void frameInitialization() {
        final MainWindow main = new MainWindow();

        main.addPlot(plot);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JMenuItem saveMenuItem = new JMenuItem("Save Configuration");
        saveMenuItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();

            fc.showSaveDialog(main);
            try {
                Context.getInstance().createMarshaller().marshal(buildConfiguration(), fc.getSelectedFile());
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        });
        main.addMenuItem(saveMenuItem);


        JMenuItem loadMenuItem = new JMenuItem("Load Configuration");
        loadMenuItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(main);

            try {
                Configuration config = (Configuration) Context.getInstance().createUnmarshaller().unmarshal(fc.getSelectedFile());
                applyConfiguration(config);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });
        main.addMenuItem(loadMenuItem);

        JMenuItem loadFromCSV = new JMenuItem("Load From CSV");
        loadFromCSV.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.showOpenDialog(main);

            try {
                Configuration config = Context.getInstance().csvLoader().load(fc.getSelectedFile());
                applyConfiguration(config);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        main.addMenuItem(loadFromCSV);

        JPanel root = new JPanel(new GridLayout(1, 2));
        JPanel active = new JPanel();
        active.setLayout(new GridLayout(12, 2));

        final JLabel inputVectorLabel = new JLabel("Insert w_(for example: 1.0 1.1");
        active.add(inputVectorLabel);
        active.add(wVector);


        final JLabel inputYLabel = new JLabel("Insert y_(for example: 1.0 1.1");
        active.add(inputYLabel);
        active.add(yVector);

        final JLabel inputEpsilonLabel = new JLabel("Insert epsilon value: ");
        active.add(inputEpsilonLabel);
        active.add(inputEpsilon);

        final JLabel kaczmarzGammaLabel = new JLabel("Insert Gamma coefficient: ");
        active.add(kaczmarzGammaLabel);
        active.add(kaczmarzGamma);

        final JLabel kaczmarzMuLabel = new JLabel("Insert Mu coefficient: ");
        active.add(kaczmarzMuLabel);
        active.add(kaczmarzMu);

        final JLabel outputNoizeLabel = new JLabel("Output Noize Type:");
        active.add(outputNoizeLabel);
        active.add(outputNoize);

        final JLabel outputDownEdgeLabel = new JLabel("Output Low Edge:");
        active.add(outputDownEdgeLabel);
        active.add(outputDownEdge);

        final JLabel outputUpEdgeLabel = new JLabel("Output Up Edge:");
        active.add(outputUpEdgeLabel);
        active.add(outputUpEdge);

        final JLabel funcType = new JLabel("f(n)");
        active.add(funcType);
        active.add(funcList);

        final JLabel maxIterationLabel = new JLabel("Max Iteration Count:");
        active.add(maxIterationLabel);
        active.add(maxIteration);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> executor.submit(() -> {
            try {
                currentMethod = new Kaczmarz(plot, buildConfiguration());
                currentMethod.bulkCalculate();
            } catch (Exception exc) {
                exc.printStackTrace();
                JOptionPane.showMessageDialog(main, exc.getMessage());
            }
        }));
        active.add(startButton);

        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(e -> currentMethod.interrupt());
        active.add(stopButton);

        root.add(active);

        //add table
        JPanel tablePane = new JPanel(new GridLayout(3, 1));
        final JTable table = new JTable(tableModel);

        final JComboBox<GenType> distrType = new JComboBox<>();
        distrType.addItem(GenType.NO_GEN);
        distrType.addItem(GenType.GAUSIAN);
        distrType.addItem(GenType.UNIFORM);

        table.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(distrType));
        table.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(distrType));
        tablePane.add(table.getTableHeader());

        JScrollPane scrollPane = new JScrollPane(table);
        tablePane.add(scrollPane);

        final JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> tableModel.newRow());
        tablePane.add(addButton);

        final JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> tableModel.removeRow(table.getSelectedRow()));
        tablePane.add(deleteButton);

        root.add(tablePane);

        main.add(root);

        main.pack();
        main.setVisible(true);
    }

    public Configuration buildConfiguration() {
        Configuration config = new Configuration();
        if (!wVector.getText().isEmpty()) {
            config.setClearW(DenseVector.parseDenseVector(wVector.getText()));
        }

        if (!yVector.getText().isEmpty()) {
            config.setClearY(DenseVector.parseDenseVector(yVector.getText()));
        }

        config.setGamma(Double.valueOf(kaczmarzGamma.getText()));
        config.setOutputNoize((GenType) outputNoize.getSelectedItem());
        config.setOutputNoizeLowEdge(Double.valueOf(outputDownEdge.getText()));
        config.setOutputNoizeUpEdge(Double.valueOf(outputUpEdge.getText()));
        config.setFuncType((Functions) funcList.getSelectedItem());
        config.setMu(Double.valueOf(kaczmarzMu.getText()));
        config.setEpsilon(Double.valueOf(inputEpsilon.getText()));
        config.setInputVectors(tableModel.getVectorConfigs());
        config.setMaxIterationCount(Integer.parseInt(maxIteration.getText()));

        return config;
    }

    private void applyConfiguration(Configuration configuration) {
        if (configuration.getClearW() != null)
            wVector.setText(configuration.getClearW().toString());
        if (configuration.getClearY() != null)
            yVector.setText(configuration.getClearY().toString());
        kaczmarzGamma.setText(Double.toString(configuration.getGamma()));
        outputNoize.setSelectedItem(configuration.getOutputNoize());
        outputDownEdge.setText(Double.toString(configuration.getOutputNoizeLowEdge()));
        outputUpEdge.setText(Double.toString(configuration.getOutputNoizeUpEdge()));
        funcList.setSelectedItem(configuration.getFuncType());
        kaczmarzMu.setText(Double.toString(configuration.getMu()));
        inputEpsilon.setText(Double.toString(configuration.getEpsilon()));
        tableModel.setVectorConfigs(configuration.getInputVectors());
    }

    public static void main(String[] args) throws Exception {
        new Main().frameInitialization();
    }
}
