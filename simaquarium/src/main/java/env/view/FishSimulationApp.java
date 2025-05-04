package env.view;

import javax.swing.*;

import env.model.AquariumModel;
import env.model.Fish;
import env.model.Food;
import env.model.Obstacle;
import env.model.Position;
import launcher.SimulationLauncher;
import utils.Utils;

import java.awt.*;
import java.util.Set;

public class FishSimulationApp extends JFrame {
    // TODO: pensare a altre statistiche interessanti (forse mettere per ogni pesce i suoi attributi) e a quali eventi mettere nella GUI (forse pesce ha mangiato, pesce è morto).
    private DrawPanel drawPanel;
    private JTextArea statsArea;
    private JTextArea eventsArea;
    private Set<Fish> fishList;
    private Set<Obstacle> rockList;
    private Set<Food> foodList;
    private AquariumModel model;
    private JPanel leftPanel;
    private int nFishAlive;
    private int nMaxFish;
    private int lastKnownNumberOfFoodEaten;

    private static final int FPS = 30;

    public FishSimulationApp(AquariumModel model) {
        this.model = model;
        this.fishList = this.model.getAllAgents();
        this.nFishAlive = -1;
        this.nMaxFish = 1;
        this.lastKnownNumberOfFoodEaten = 0;
        this.foodList = this.model.getAllFood();
        this.rockList = this.model.getAllObstacles();
        setTitle("Fish Simulation");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.CYAN);

        JPanel buttonPanel = new JPanel();
        JButton stopButton = new JButton("STOP");
        JButton feeder = new JButton("DROP FOOD");

        buttonPanel.add(stopButton);
        buttonPanel.add(feeder);

        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(drawPanel, BorderLayout.CENTER);
        this.leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 1));

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setPreferredSize(new Dimension(200, 200));

        eventsArea = new JTextArea();
        eventsArea.setEditable(false);


        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BorderLayout());
        statsPanel.add(new JLabel("STATS"), BorderLayout.NORTH);
        statsPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        rightPanel.add(statsPanel);
        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BorderLayout());
        eventsPanel.add(new JLabel("EVENTS"), BorderLayout.NORTH);
        eventsPanel.add(new JScrollPane(eventsArea), BorderLayout.CENTER);
        rightPanel.add(eventsPanel);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setEnabled(false);

        add(splitPane);

        stopButton.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                null,
                "The simulation will be stopped: are you sure?",
                "SIMULATION STOP",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                SimulationLauncher.getLocalMAS().finish(0, true, 0);
            }
        });

        feeder.addActionListener(e -> {
            for (int i = 0; i < 5; i++) {
                model.addFood(new Position((Utils.RAND.nextDouble() * 0.8 + 0.1) * drawPanel.getWidth(), 0));
            }

            logEvent("Food dropped");
            updateStats();
            drawPanel.repaint();
            drawPanel.revalidate();
        });

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    long loopStartTime = System.currentTimeMillis();

                    SwingUtilities.invokeLater(() -> drawPanel.repaint());
                    sync(loopStartTime);
                }
            }

            private void sync(long loopStartTime) {
                double loopSlot = 1.0 / FPS;
                double endTime = loopStartTime + loopSlot; 
                while(System.currentTimeMillis() < endTime) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ie) {}
                }
             }
        });
        t.start();
    }

    private void updateStats() {
        SwingUtilities.invokeLater(() -> {
            statsArea.setText("Number of fish: " + this.nFishAlive + "\n"
                        + "Survival rate: " + Math.floor(1.0 * this.nFishAlive / this.nMaxFish * 100)+ "%\n"
                        + "Food pieces eaten:" + this.lastKnownNumberOfFoodEaten);
                statsArea.update(statsArea.getGraphics());
        });
    }

    private void logEvent(String event) {
        eventsArea.append(event + "\n");
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (Obstacle rock : rockList) {
                g.setColor(Color.DARK_GRAY);
                g.fillOval((int) (rock.getX() - rock.getRadius()), (int) (rock.getY() - rock.getRadius()),
                        (int) rock.getRadius() * 2, (int) rock.getRadius() * 2);
            }

            for (Fish fish : fishList) {
                g.setColor(Color.ORANGE);
                // g.fillOval((int)fish.getX(), (int)fish.getY(), (int)fish.getWeight(),
                // (int)fish.getWeight()/2);
                g.fillOval((int) (fish.getX() - 5), (int) (fish.getY() - 5), 10, 10);

                g.setColor(Color.BLUE);
                g.drawLine((int) fish.getX(), (int) fish.getY(), (int) (fish.getX() + fish.getDirX() * 100),
                        (int) (fish.getY() + fish.getDirY() * 100));
            }

            for (Food food : foodList) {
                g.setColor(Color.RED);
                g.fillOval((int) food.getX() - 5, (int) food.getY() - 5, 10, 10);
            }
        }
    }

    public void notifyModelChanged() {
        this.fishList = this.model.getAllAgents();
        this.foodList = this.model.getAllFood();
        this.rockList = this.model.getAllObstacles();
        boolean needToUpdate = false;
        if(this.nFishAlive != this.fishList.size()){
            needToUpdate = true;
            this.nFishAlive = this.fishList.size();
            if(this.nFishAlive > this.nMaxFish){
                this.nMaxFish = this.nFishAlive;
            }
        }
        if(this.lastKnownNumberOfFoodEaten != this.model.getNumberOfFoodEaten()){
            this.lastKnownNumberOfFoodEaten = this.model.getNumberOfFoodEaten();
            needToUpdate = true;
        }

        if(needToUpdate){
            updateStats();
        }
    }

    public int getPanelWidth() {
        return this.drawPanel.getWidth();
    }

    public int getPanelHeight() {
        return this.drawPanel.getHeight();
    }
}
