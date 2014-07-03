package fractaloscope;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class App extends JFrame {
    public Display display;
    public JSpinner jCReSpinner;
    public JSpinner jCImSpinner;

    public App() {
        super("Fractaloscope");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setSize(720, 533);
        setMinimumSize(new Dimension(250, 370));
        setResizable(true);
        setLayout(new BorderLayout());
        display = new Display(this);
        add(display, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setSize(196, 540);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        add(controlPanel, BorderLayout.EAST);

        controlPanel.add(Box.createRigidArea(new Dimension(0, 4)));

        final JComboBox<String> fractalTypeBox = new JComboBox<String>(new String[] {
                "Barnsley",
                "Cesaro",
                "Dragon",
                "Julia"
        });
        fractalTypeBox.setSelectedIndex(3);
        fractalTypeBox.setPreferredSize(new Dimension(196, 30));
        fractalTypeBox.setMaximumSize(new Dimension(196, 30));
        fractalTypeBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(fractalTypeBox);

        controlPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        final JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new CardLayout());
        cardPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.setMaximumSize(new Dimension(200, 180));
        cardPanel.setPreferredSize(new Dimension(200, 180));
        controlPanel.add(cardPanel);


        JPanel juliaCard = new JPanel();
        juliaCard.setLayout(new BoxLayout(juliaCard, BoxLayout.Y_AXIS));

        JPanel juliaSettingsPanel = new JPanel();
        juliaSettingsPanel.setLayout(new GridLayout(3, 2));
        juliaSettingsPanel.add(new JLabel("Степень:"));
        final JSpinner jPowerSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
        juliaSettingsPanel.add(jPowerSpinner);
        juliaSettingsPanel.add(new JLabel("Xc:"));
        jCReSpinner = new JSpinner(new SpinnerNumberModel(-0.8, -5, 5, 0.005));
        ((JSpinner.NumberEditor) jCReSpinner.getEditor()).getFormat().setMaximumFractionDigits(4);
        juliaSettingsPanel.add(jCReSpinner);
        juliaSettingsPanel.add(new JLabel("Yc:"));
        jCImSpinner = new JSpinner(new SpinnerNumberModel(0.156, -5, 5, 0.005));
        ((JSpinner.NumberEditor) jCImSpinner.getEditor()).getFormat().setMaximumFractionDigits(4);
        juliaSettingsPanel.add(jCImSpinner);
        juliaSettingsPanel.setMaximumSize(new Dimension(196, 75));
        juliaSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        juliaCard.add(juliaSettingsPanel);

        juliaCard.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel maxIterLabel = new JLabel("Чёткость:", SwingConstants.RIGHT);
        maxIterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        juliaCard.add(maxIterLabel);
        final JSlider jMaxIterSlider = new JSlider(0, 500, 40);
        jMaxIterSlider.setPreferredSize(new Dimension(196, 40));
        jMaxIterSlider.setMaximumSize(new Dimension(196, 40));
        jMaxIterSlider.setMajorTickSpacing(100);
        jMaxIterSlider.setPaintTicks(true);
        jMaxIterSlider.setPaintLabels(true);
        jMaxIterSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        juliaCard.add(jMaxIterSlider);

        cardPanel.add(juliaCard, "julia");


        JPanel cesaroCard = new JPanel();
        cesaroCard.setLayout(new BoxLayout(cesaroCard, BoxLayout.Y_AXIS));
        JPanel cesaroSettingsPanel = new JPanel();
        cesaroSettingsPanel.setLayout(new GridLayout(2, 2));
        cesaroSettingsPanel.setMaximumSize(new Dimension(196, 50));
        cesaroSettingsPanel.setMinimumSize(new Dimension(196, 50));
        cesaroSettingsPanel.setPreferredSize(new Dimension(196, 50));
        cesaroSettingsPanel.add(new JLabel("Итераций:"));
        final JSpinner cMaxIterSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 10, 1));
        cesaroSettingsPanel.add(cMaxIterSpinner);
        cesaroSettingsPanel.add(new JLabel("Вершин:"));
        final JSpinner cSidesSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 7, 1));
        cesaroSettingsPanel.add(cSidesSpinner);
        cesaroSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cesaroCard.add(cesaroSettingsPanel);
        
        cesaroCard.add(Box.createRigidArea(new Dimension(0, 10)));

        final JLabel cAngleLabel = new JLabel("Угол:");
        cAngleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        cesaroCard.add(cAngleLabel);
        final JSlider cAngleSlider = new JSlider(-1000, 1000, 375);
        cAngleSlider.setMaximumSize(new Dimension(196, 20));
        cAngleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        cesaroCard.add(cAngleSlider);

        cardPanel.add(cesaroCard, "cesaro");

        JPanel dragonCard = new JPanel();
        dragonCard.setLayout(new BoxLayout(dragonCard, BoxLayout.Y_AXIS));
        JPanel dragonSettingsPanel = new JPanel();
        dragonSettingsPanel.setLayout(new GridLayout(1, 2));
        dragonSettingsPanel.setMaximumSize(new Dimension(196, 25));
        dragonSettingsPanel.setMinimumSize(new Dimension(196, 25));
        dragonSettingsPanel.setPreferredSize(new Dimension(196, 25));
        dragonSettingsPanel.add(new JLabel("Итераций:"));
        final JSpinner dMaxIterSpinner = new JSpinner(new SpinnerNumberModel(12, 0, 20, 1));
        dragonSettingsPanel.add(dMaxIterSpinner);
        dragonSettingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dragonCard.add(dragonSettingsPanel);

        dragonCard.add(Box.createRigidArea(new Dimension(0, 10)));

        final JLabel dAngleLabel = new JLabel("Угол:");
        dAngleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dragonCard.add(dAngleLabel);
        final JSlider dAngleSlider = new JSlider(0, 1000, 1000);
        dAngleSlider.setMaximumSize(new Dimension(196, 20));
        dAngleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        dragonCard.add(dAngleSlider);

        cardPanel.add(dragonCard, "dragon");

        JPanel barnsleyCard = new JPanel();
        barnsleyCard.setLayout(new BoxLayout(barnsleyCard, BoxLayout.Y_AXIS));
        JLabel bIterLabel = new JLabel("Плотность:");
        bIterLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bIterLabel);
        final JSlider bIterSlider = new JSlider(1200, 2000, 1700);
        bIterSlider.setMaximumSize(new Dimension(196, 20));
        bIterSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bIterSlider);

        JLabel bCurlinessLabel = new JLabel("Наклон:");
        bCurlinessLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bCurlinessLabel);
        final JSlider bCurlinessSlider = new JSlider(-1000, 1000, 40);
        bCurlinessSlider.setMaximumSize(new Dimension(196, 20));
        bCurlinessSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bCurlinessSlider);

        JLabel bFuzzinessLabel = new JLabel("Рассеянность:");
        bFuzzinessLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bFuzzinessLabel);
        final JSlider bFuzzinessSlider = new JSlider(500, 1200, 850);
        bFuzzinessSlider.setMaximumSize(new Dimension(196, 20));
        bFuzzinessSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        barnsleyCard.add(bFuzzinessSlider);
        

        cardPanel.add(barnsleyCard, "barnsley");

        JLabel colorSchemeLabel = new JLabel("Цветовая схема:", SwingConstants.LEFT);
        colorSchemeLabel.setMinimumSize(new Dimension(200, 20));
        colorSchemeLabel.setPreferredSize(new Dimension(200, 20));
        colorSchemeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(colorSchemeLabel);
        final JComboBox<String> colorSchemeBox = new JComboBox<String>(new String[] {
                "256 оттенков серого",
                "The Night",
                "Summer '68",
                "Light My Fire",
                "Fixed-Gear Bike"
        });
        colorSchemeBox.setSelectedIndex(1);
        colorSchemeBox.setPreferredSize(new Dimension(196, 30));
        colorSchemeBox.setMaximumSize(new Dimension(196, 30));
        colorSchemeBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(colorSchemeBox);

        final JButton rotateButton = new JButton("Не нажимать");
        rotateButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        rotateButton.setPreferredSize(new Dimension(196, 30));
        rotateButton.setMinimumSize(new Dimension(196, 30));
        rotateButton.setMaximumSize(new Dimension(196, 30));
        controlPanel.add(rotateButton);
        rotateButton.setVisible(false);

        controlPanel.add(Box.createVerticalGlue());
        JButton fitButton = new JButton();
        fitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        fitButton.setIcon(new ImageIcon(getClass().getResource("img/" + "fit" + ".png")));
        controlPanel.add(fitButton);

        fractalTypeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = fractalTypeBox.getSelectedIndex();

                switch (index) {
                    case 0:
                        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "barnsley");
                        cardPanel.setMaximumSize(new Dimension(200, 125));
                        cardPanel.setPreferredSize(new Dimension(200, 125));
                        display.setFractal("barnsley");
                        bIterSlider.setValue(1700);
                        bCurlinessSlider.setValue(40);
                        bFuzzinessSlider.setValue(850);
                        break;
                    case 1:
                        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "cesaro");
                        cardPanel.setMaximumSize(new Dimension(200, 120));
                        cardPanel.setPreferredSize(new Dimension(200, 120));
                        display.setFractal("cesaro");
                        cAngleSlider.setValue(375);
                        cSidesSpinner.setValue(2);
                        cMaxIterSpinner.setValue(5);
                        break;
                    case 2:
                        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "dragon");
                        cardPanel.setMaximumSize(new Dimension(200, 90));
                        cardPanel.setPreferredSize(new Dimension(200, 90));
                        display.setFractal("dragon");
                        dAngleSlider.setValue(1000);
                        dMaxIterSpinner.setValue(12);
                        break;
                    case 3:
                        ((CardLayout) cardPanel.getLayout()).show(cardPanel, "julia");
                        cardPanel.setMaximumSize(new Dimension(200, 180));
                        cardPanel.setPreferredSize(new Dimension(200, 180));
                        display.setFractal("julia");
                        jPowerSpinner.setValue(2);
                        jCReSpinner.setValue(-0.8);
                        jCImSpinner.setValue(0.156);
                        jMaxIterSlider.setValue(40);
                        break;
                }
            }
        });

        jMaxIterSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("maxIter", ((JSlider) e.getSource()).getValue());
            }
        });

        jPowerSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("power", (Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        jCReSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("cRe", (Double) ((JSpinner) e.getSource()).getValue());
            }
        });

        jCImSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("cIm", (Double) ((JSpinner) e.getSource()).getValue());
            }
        });

        colorSchemeBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = colorSchemeBox.getSelectedIndex();
                if (index == 2) {
                    rotateButton.setVisible(true);
                } else {
                    rotateButton.setVisible(false);
                    display.stopRotation();
                }

                display.setColorScheme(index);
            }
        });

        rotateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.switchRotation();
            }
        });

        cAngleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double initialValue = ((JSlider) e.getSource()).getValue() / 1000.0;

                double exp = Math.exp(-7);
                double transformedValue = (2 / (1 + Math.pow(exp, initialValue)) - 1) * (1 + exp) / (1 - exp);

                display.setParam("angle", transformedValue);
            }
        });

        cSidesSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("sides", (Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        cMaxIterSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("iterations", (Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        dMaxIterSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("iterations", (Integer) ((JSpinner) e.getSource()).getValue());
            }
        });

        dAngleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double initialValue = 1000 - ((JSlider) e.getSource()).getValue();
                double cos = Math.cos(initialValue * (Math.PI / 4) / 1000.0 + Math.PI / 4);
                display.setParam("angle", cos);
            }
        });

        bIterSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double initialValue = ((JSlider) e.getSource()).getValue() * 6.5 / 2000.0;
                display.setParam("iterations", Math.pow(10, initialValue));
            }
        });

        bCurlinessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("curliness", ((JSlider) e.getSource()).getValue() / 1000.0);
            }
        });

        bFuzzinessSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                display.setParam("fuzziness", ((JSlider) e.getSource()).getValue() / 1000.0);
            }
        });


        fitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.resetViewports();
                display.refresh();
            }
        });


        setLocationRelativeTo(null);

        display.init();
    }

    public void setC(double newCRe, double newCIm) {
        jCReSpinner.setValue(newCRe);
        jCImSpinner.setValue(newCIm);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }

        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Display.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Display.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Display.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Display.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}
