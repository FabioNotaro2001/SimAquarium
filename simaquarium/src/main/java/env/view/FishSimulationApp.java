package env.view;

import javax.swing.*;

import env.SimAquariumEnvironment;
import env.model.AquariumModel;
import env.model.DomainEvent;
import env.model.Fish;
import env.model.Food;
import env.model.Obstacle;
import env.model.Position;
import jason.runtime.MASConsoleGUI;
import launcher.SimulationLauncher;
import utils.Utils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.Set;

public class FishSimulationApp extends JFrame {
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
    private boolean showRanges = false;
    private boolean paused = false;
    private final SimAquariumEnvironment env;
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm:ss");

    private static final int FPS = 30;

    public FishSimulationApp(AquariumModel model, SimAquariumEnvironment env) {
        this.model = model;
        this.fishList = this.model.getAllAgents();
        this.nFishAlive = -1;
        this.nMaxFish = 1;
        this.lastKnownNumberOfFoodEaten = 0;
        this.foodList = this.model.getAllFood();
        this.rockList = this.model.getAllObstacles();
        this.env = env;
        setTitle("Fish Simulation");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.CYAN);

        JPanel buttonPanel = new JPanel();
        JButton stopButton = new JButton("STOP");
        JButton feeder = new JButton("DROP FOOD");
        JButton viewRange = new JButton("SHOW RANGE");
        JButton pause = new JButton("PAUSE");

        buttonPanel.add(stopButton);
        buttonPanel.add(feeder);
        buttonPanel.add(viewRange);
        buttonPanel.add(pause);

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
            for (int i = 0; i < this.model.getFoodQuantity(); i++) {
                model.addFood(new Position((Utils.RAND.nextDouble() * 0.8 + 0.1) * drawPanel.getWidth(), 0));
            }

            this.notifyModelChanged(Optional.of(DomainEvent.of("Food dropped")));
        });

        viewRange.addActionListener(e -> {
            this.showRanges = !this.showRanges;
            if (this.showRanges) {
                viewRange.setText("HIDE RANGE");
            } else {
                viewRange.setText("SHOW RANGE");
            }
            drawPanel.repaint();
        });

        pause.addActionListener(e -> {
            this.paused = !this.paused;
            if (this.paused) {
                pause.setText("RESUME");
            } else {
                pause.setText("PAUSE");
            }
            this.env.setPaused(this.paused);
            MASConsoleGUI.get().setPause(this.paused);
            
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
                        + "Survival rate: " + Math.floor(1.0 * this.nFishAlive / this.nMaxFish * 100) + "%\n"
                        + "Food pieces eaten: " + this.lastKnownNumberOfFoodEaten + "\n"
                        + "Fairness index: " + String.format("%.2f", this.model.getFairnessIndex()) + "\n");
            statsArea.update(statsArea.getGraphics());
        });
    }

    class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Stroke basicStroke = new BasicStroke(2);
            Stroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke(basicStroke);

            for (Obstacle rock : rockList) {
                g2.setColor(Color.DARK_GRAY);
                g2.fillOval((int) (rock.getX() - rock.getRadius()), (int) (rock.getY() - rock.getRadius()),
                        (int) rock.getRadius() * 2, (int) rock.getRadius() * 2);
            }

            for (Fish fish : fishList) {
                g2.setColor(Color.ORANGE);
                // g.fillOval((int)fish.getX(), (int)fish.getY(), (int)fish.getWeight(),
                // (int)fish.getWeight()/2);
                g2.fillOval((int) (fish.getX() - fish.getSize() / 2), (int) (fish.getY() - fish.getSize() / 4), (int)fish.getSize(), (int)(fish.getSize()/2));

                g2.setStroke(dashedStroke);

                if(showRanges){
                    double fishRange = fish.getRange() + fish.getSize() / 2;
                    g2.setColor(new Color(0x006600));
                    g2.drawOval((int) (fish.getX() - fishRange), (int) (fish.getY() - fishRange), (int)(fishRange * 2), (int)(fishRange * 2));
                    
                    g2.setStroke(basicStroke);
                    
                    g2.setColor(Color.BLUE);
                    g2.drawLine((int) fish.getX(), (int) fish.getY(), (int) (fish.getX() + fish.getDirX() * fish.getRange() * 0.8),
                    (int) (fish.getY() + fish.getDirY() * fish.getRange() * 0.8));
                }

            }

            for (Food food : foodList) {
                g2.setColor(Color.RED);
                g2.fillOval((int) food.getX() - 5, (int) food.getY() - 5, 10, 10);
            }
        }
    }

    public void notifyModelChanged(Optional<DomainEvent> event) {
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
        if(event.isPresent()){
            eventsArea.append(String.format("[%s]: %s\n", TIME_FORMATTER.format(event.get().getTime()), event.get().getDescription()));
        }
    }

    public int getPanelWidth() {
        return this.drawPanel.getWidth();
    }

    public int getPanelHeight() {
        return this.drawPanel.getHeight();
    }
}
