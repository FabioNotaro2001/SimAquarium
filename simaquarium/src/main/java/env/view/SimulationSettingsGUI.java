package env.view;

import javax.swing.*;

import env.model.Amount;
import jason.JasonException;
import launcher.SimulationLauncher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.stream.Stream;

public class SimulationSettingsGUI {
    public static void main(String[] args) {
        JFrame frame = new JFrame("SIMULATION SETTINGS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel agentsLabel = new JLabel("NUMBER OF AGENTS:");
        JTextField agentsField = new JTextField();

        JLabel foodLabel = new JLabel("FOOD QUANTITY:");
        String[] values = Stream.of(Amount.values()).map(Amount::toString).toArray(String[]::new);
        JComboBox<String> foodComboBox = new JComboBox<String>(values);

        JLabel obstaclesLabel = new JLabel("OBSTACLES QUANTITY:");
        JComboBox<String> obstaclesComboBox = new JComboBox<String>(values);

        panel.add(agentsLabel);
        panel.add(agentsField);
        panel.add(foodLabel);
        panel.add(foodComboBox);
        panel.add(obstaclesLabel);
        panel.add(obstaclesComboBox);

        JButton startButton = new JButton("START SIMULATION");

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    var inputInserted = Integer.parseInt(agentsField.getText());
                    if(inputInserted < 1){
                        JOptionPane.showMessageDialog(null, "Please, insert a valid number of agents!","ERROR",JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    SimulationLauncher.launchNew(Integer.parseInt(agentsField.getText()), foodComboBox.getSelectedItem().toString(), obstaclesComboBox.getSelectedItem().toString());
                    frame.dispose();
                } catch (NumberFormatException | IOException | JasonException e1 ) {
                    JOptionPane.showMessageDialog(null, e1.getMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
                } 
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.add(startButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
