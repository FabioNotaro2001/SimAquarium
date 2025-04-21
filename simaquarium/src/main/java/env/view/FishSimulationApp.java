package env.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class FishSimulationApp extends JFrame {
    private DrawPanel drawPanel;
    private JTextArea statsArea;
    private JTextArea eventsArea;
    private Timer timer;
    private ArrayList<Fish> fishList = new ArrayList<>();
    private ArrayList<Rock> rockList = new ArrayList<>();
    private ArrayList<Food> foodList = new ArrayList<>();
    private Random random = new Random();

    public FishSimulationApp() {
        setTitle("Fish Simulation");
        setSize(800, 600);
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

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(drawPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

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
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(600);

        add(splitPane);

        // --- Timer per movimento pesci ---
        timer = new Timer(30, e -> {
            for (Fish fish : fishList) {
                fish.move();
            }

        Iterator<Food> it = foodList.iterator();
        while (it.hasNext()) {
            Food food = it.next();
            food.move();
            if (food.isOutOfBounds(drawPanel.getHeight())) {
                it.remove(); // rimuove il cibo uscito fuori
            }
        }
            drawPanel.repaint();
            updateStats();
        });

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
            int x = random.nextInt(drawPanel.getWidth() - 10); // un po' di margine
            foodList.add(new Food(x));
        
            logEvent("Food dropped");
            updateStats();
            drawPanel.repaint(); 
            drawPanel.revalidate();
        });        
    }

    private void updateStats() {
        statsArea.setText("Number of Fish: " + fishList.size() + "\n"
                        + "Survival Rate: " + (fishList.isEmpty() ? 0 : 100) + "%");
    }

    private void logEvent(String event) {
        eventsArea.append(event + "\n");
    }

    class Fish {
        Random random = new Random();
        int x, y;
        int dx, dy;
        int size = 5 + random.nextInt(30);

        Fish(int x, int y) {
            this.x = x;
            this.y = y;
            this.dx = random.nextInt(5) + 1;
            this.dy = random.nextInt(5) + 1;
        }

        void move() {
            x += dx;
            y += dy;
            if (x < 0 || x > drawPanel.getWidth() - size) dx = -dx;
            if (y < 0 || y > drawPanel.getHeight() - size) dy = -dy;
        }

        void draw(Graphics g) {
            g.setColor(Color.ORANGE);
            g.fillOval(x, y, size, size/2);
        }
    }

    class Rock {
        int x, y;
        int size = 30;
    
        Rock(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        void draw(Graphics g) {
            g.setColor(Color.DARK_GRAY);
            g.fillOval(x, y, size, size);
        }
    }    

    class Food {
        int x, y;
        int size = 8;
        int speed = 3;
    
        Food(int x) {
            this.x = x;
            this.y = 0; // inizia in alto
        }
    
        void move() {
            y += speed;
        }
    
        boolean isOutOfBounds(int panelHeight) {
            return y > panelHeight;
        }
    
        void draw(Graphics g) {
            g.setColor(Color.PINK);
            g.fillOval(x, y, size, size);
        }
    }    

    class DrawPanel extends JPanel {
        // --- Panel che disegna i pesci ---
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Disegna rocce prima dei pesci
            for (Rock rock : rockList) {
                rock.draw(g);
            }

            for (Fish fish : fishList) {
                fish.draw(g);
            }

            for (Food food : foodList) {
                food.draw(g);
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
    
                // Aggiungiamo pesci
                for (int i = 0; i < fishCount; i++) {
                    fishList.add(new Fish(random.nextInt(drawPanel.getWidth()), random.nextInt(drawPanel.getHeight())));
                }
    
                // Aggiungiamo pietre
                for (int i = 0; i < rockCount; i++) {
                    rockList.add(new Rock(random.nextInt(drawPanel.getWidth()), random.nextInt(drawPanel.getHeight())));
                }
    
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
}
