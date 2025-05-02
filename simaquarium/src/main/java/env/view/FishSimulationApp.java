package env.view;

import javax.swing.*;

import env.model.AquariumModel;
import env.model.Fish;
import env.model.Food;
import env.model.Obstacle;
import env.model.Position;
import utils.Utils;

import java.awt.*;
import java.util.Set;

public class FishSimulationApp extends JFrame {
    private DrawPanel drawPanel;
    private JTextArea statsArea;
    private JTextArea eventsArea;
    private Timer timer;
    private Set<Fish> fishList;
    private Set<Obstacle> rockList;
    private Set<Food> foodList;
    private AquariumModel model;
    private JPanel leftPanel;

    private static final int FPS = 30;

    public FishSimulationApp(AquariumModel model) {
        this.model = model;
        this.fishList = this.model.getAllAgents();
        this.foodList = this.model.getAllFood();
        this.rockList = this.model.getAllObstacles();
        setTitle("Fish Simulation");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // --- Panel di disegno ---
        drawPanel = new DrawPanel();
        drawPanel.setBackground(Color.CYAN);

        // --- Panel bottoni ---
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton feeder = new JButton("Drop Food");

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(feeder);

        this.leftPanel = new JPanel(new BorderLayout());
        this.leftPanel.add(drawPanel, BorderLayout.CENTER);
        this.leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- Sidebar ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        statsArea = new JTextArea();
        statsArea.setEditable(false);
        statsArea.setPreferredSize(new Dimension(200, 200));

        eventsArea = new JTextArea();
        eventsArea.setEditable(false);

        rightPanel.add(new JLabel("Stats"), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(statsArea), BorderLayout.CENTER);
        rightPanel.add(new JLabel("Events"), BorderLayout.SOUTH);
        rightPanel.add(new JScrollPane(eventsArea), BorderLayout.SOUTH);

        // Uso un BoxLayout verticale più carino
        Box rightBox = Box.createVerticalBox();
        rightBox.add(new JLabel("Stats"));
        rightBox.add(new JScrollPane(statsArea));
        rightBox.add(new JLabel("Events"));
        rightBox.add(new JScrollPane(eventsArea));
        rightPanel.add(rightBox);

        // --- Split ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.leftPanel, rightPanel);
        splitPane.setDividerLocation(600);
        splitPane.setEnabled(false);

        add(splitPane);

        // --- Eventi Bottoni ---
        startButton.addActionListener(e -> {
            openStartDialog();
            drawPanel.repaint();
            drawPanel.revalidate();
            timer.start();
            logEvent("Simulation started");
        });

        stopButton.addActionListener(e -> {
            timer.stop();
            logEvent("Simulation stopped");
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
        statsArea.setText("Number of Fish: " + fishList.size() + "\n"
                + "Survival Rate: " + (fishList.isEmpty() ? 0 : 100) + "%");
    }

    private void logEvent(String event) {
        eventsArea.append(event + "\n");
    }

    // class Fish {
    // Random random = new Random();
    // int x, y;
    // int dx, dy;
    // int size = 5 + random.nextInt(30);

    // Fish(int x, int y) {
    // this.x = x;
    // this.y = y;
    // this.dx = random.nextInt(5) + 1;
    // this.dy = random.nextInt(5) + 1;
    // }

    // void move() {
    // x += dx;
    // y += dy;
    // if (x < 0 || x > drawPanel.getWidth() - size) dx = -dx;
    // if (y < 0 || y > drawPanel.getHeight() - size) dy = -dy;
    // }

    // void draw(Graphics g) {
    // g.setColor(Color.ORANGE);
    // g.fillOval(x, y, size, size/2);
    // }
    // }

    // class Rock {
    // int x, y;
    // int size = 30;

    // Rock(int x, int y) {
    // this.x = x;
    // this.y = y;
    // }

    // void draw(Graphics g) {
    // g.setColor(Color.DARK_GRAY);
    // g.fillOval(x, y, size, size);
    // }
    // }

    // class Food {
    // int x, y;
    // int size = 8;
    // int speed = 3;

    // Food(int x) {
    // this.x = x;
    // this.y = 0; // inizia in alto
    // }

    // void move() {
    // y += speed;
    // }

    // boolean isOutOfBounds(int panelHeight) {
    // return y > panelHeight;
    // }

    // void draw(Graphics g) {
    // g.setColor(Color.PINK);
    // g.fillOval(x, y, size, size);
    // }
    // }

    class DrawPanel extends JPanel {
        // --- Panel che disegna i pesci ---
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Disegna rocce prima dei pesci
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

    private void openStartDialog() {
        JDialog dialog = new JDialog(this, "Configure Simulation", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel fishLabel = new JLabel("Number of Fish:");
        JTextField fishField = new JTextField("10");

        JLabel rockLabel = new JLabel("Number of Rocks:");
        JTextField rockField = new JTextField("5");

        JButton startSimButton = new JButton("Start Simulation");

        dialog.add(fishLabel);
        dialog.add(fishField);
        dialog.add(rockLabel);
        dialog.add(rockField);
        dialog.add(new JLabel()); // placeholder
        dialog.add(startSimButton);

        startSimButton.addActionListener(ev -> {
            try {
                int fishCount = Integer.parseInt(fishField.getText());
                int rockCount = Integer.parseInt(rockField.getText());

                logEvent("Simulation started with " + fishCount + " fish and " + rockCount + " rocks.");
                updateStats();
                drawPanel.repaint();
                timer.start();

                dialog.dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers.");
            }
        });

        dialog.setVisible(true);
    }

    long lastRepaint = System.currentTimeMillis();

    public void notifyModelChanged() {
        this.fishList = this.model.getAllAgents();
        this.foodList = this.model.getAllFood();
        this.rockList = this.model.getAllObstacles();

        long now = System.currentTimeMillis();
        if (now - lastRepaint >= 16) {
            lastRepaint = now;
            // this.drawPanel.repaint();
        }
        // this.invalidate();
    }

    public int getPanelWidth() {
        return this.drawPanel.getWidth();
    }

    public int getPanelHeight() {
        return this.drawPanel.getHeight();
    }
}
