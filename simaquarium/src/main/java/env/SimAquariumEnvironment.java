package env;

import jason.NoValueException;
import jason.asSemantics.Unifier;
import jason.asSyntax.ListTerm;
import jason.asSyntax.ListTermImpl;
import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;

import static utils.Utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import env.model.AquariumModel;
import env.model.AquariumModelImpl;
import env.model.Fish;
import env.model.Position;
import env.model.Speed;
import env.model.Vector2D;
import env.view.FishSimulationApp;


/**
 * Any Jason environment "entry point" should extend
 * jason.environment.Environment class to override methods init(),
 * updatePercepts() and executeAction().
 */
public class SimAquariumEnvironment extends Environment {
    private static final Random RAND = new Random();
    private Thread foodSimulationThread;
    private boolean stopRequested;


    // Action literals.
    public static final Literal moveTowards = Literal.parseLiteral("move_towards(X, Y, Speed)");
    public static final Literal eat = Literal.parseLiteral("eat(Food)");

    private AquariumModel model;
    FishSimulationApp view;

    private Position getRandomPositionInsideAquarium(){
        return new Position(RAND.nextDouble() * this.model.getWidth(), RAND.nextDouble() * this.model.getHeight());
    }

    @Override
    public void init(final String[] args) {
        Locale.setDefault(Locale.UK);
        this.model = new AquariumModelImpl();
        this.view = new FishSimulationApp(this.model);
        this.view.setVisible(true);
        this.model.setAquariumDimensions(this.view.getPanelWidth() - 50, this.view.getPanelHeight() - 50);
        this.stopRequested = false;
        for (int i = 0; i < 20; i++){
            //this.model.addObstacle(this.getRandomPositionInsideAquarium(), (RAND.nextDouble() * 0.1 + 0.02) * this.model.getHeight());
            this.model.addObstacle(this.getRandomPositionInsideAquarium(), 25);
        }
        this.foodSimulationThread = new Thread(new Runnable(){
            @Override
            public void run() {
                int stepsDone = 0;
                while (!stopRequested) {    // TODO: stopRequested dovrebbe essere settato premendo il bottone stop.
                    if (stepsDone == 10) {
                        stepsDone = 0;
                        for(int i = 0; i < 5; i++){
                            model.addFood(new Position(RAND.nextDouble() * model.getWidth(), 0));
                        }
                    } 
                    model.sinkStep();
                    notifyModelChangedToView();
                    try {
                        Thread.sleep(2000);
                        stepsDone++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.foodSimulationThread.start();  // TODO: questo andrebbe nel bottone start.
    }

    private void notifyModelChangedToView() {
        view.notifyModelChanged();
    }

    private void initializeAgentIfNeeded(String agentName) {
        if (!model.containsAgent(agentName)) {
            this.model.addFish(agentName, this.getRandomPositionInsideAquarium(), RAND.nextDouble() * 90 + 10); // TODO cambiare valori in costanti.
            notifyModelChangedToView();
        }
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        // Percept cibo in range, percept per il cibo abbastanza vicino, percept per gli ostacoli in range
        initializeAgentIfNeeded(agName);
        return Stream.of(
                foodInRangePercept(agName),
                closestFoodPercept(agName),
                obstaclePercept(agName),
                borderPercept(agName)
        ).flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private Collection<Literal> borderPercept(String agent) {
        var borders = model.getNearbyBorders(agent)
                .stream()
                .map(b -> Literal.parseLiteral(String.format("%s", b.toString().toLowerCase())))
                .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        return Stream.of(Literal.parseLiteral(String.format("borders(%s)", borders))).collect(Collectors.toList());
    }

    private Collection<Literal> foodInRangePercept(String agent) {
        var coordinates = model.getNearbyFood(agent)
                .stream()
                .map(f -> Literal.parseLiteral(String.format("food_elem(%f,%f,%s)", f.getPosition().getX(), f.getPosition().getY(), f.getId())))
                .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        return Stream.of(Literal.parseLiteral(String.format("food(%s)", coordinates))).collect(Collectors.toList());
    }

    private Collection<Literal> closestFoodPercept(String agent) {
        var foodOpt = this.model.getClosestFoodThatCanBeEaten(agent);
        return foodOpt.isPresent() ? List.of(Literal.parseLiteral(String.format("close_to_food(%s)", foodOpt.get().getId()))) : List.of();
    }

    private Collection<Literal> obstaclePercept(String agent) {
        Fish fish = this.model.getAgent(agent);

        var coordinates = this.model.getNearbyObstacles(agent)
                .stream()
                .sorted((o1, o2) -> Double.compare(Vector2D.of(o1.getX() - fish.getX(), o1.getY() - fish.getY()).getLength(), Vector2D.of(o2.getX() - fish.getX(), o2.getY() - fish.getY()).getLength()))
                .map(o -> Literal.parseLiteral(String.format("obstacle(%f,%f,%f)", o.getX() - fish.getX(), o.getY() - fish.getY(), o.getRadius())))
                .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);

        return Stream.of(Literal.parseLiteral(String.format("obstacles(%s)", coordinates))).collect(Collectors.toList());
    }

    /**
     * The <code>boolean</code> returned represents the action "move"
     * (success/failure)
     */
    @Override
    public boolean executeAction(final String ag, final Structure action) {
        initializeAgentIfNeeded(ag);

        Unifier un = new Unifier();
        if(un.unifies(moveTowards, action)){
            double x;
            double y;
            Speed s;
            try {
                x = termToDouble(un.get("X"));
                y = termToDouble(un.get("Y"));
                s = termToSpeed(un.get("Speed"));
                this.model.moveTowards(ag, x, y, s);
                notifyModelChangedToView();
                return true;

            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }

        } else if(un.unifies(eat, action)){
            String foodId;
            try {
                foodId = termToString(un.get("Food"));
                notifyModelChangedToView();
                return this.model.eat(ag, foodId);
            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }
        }
        // initializeAgentIfNeeded(ag);
        // final boolean result;
        // if (RAND.nextDouble() < model.getSlideProbability()) {
        //     result = false;
        // } else if (action.equals(moveForward)) {
        //     result = model.moveAgent(ag, 1, FORWARD);
        // } else if (action.equals(moveRight)) {
        //     result = model.moveAgent(ag, 1, RIGHT);
        // } else if (action.equals(moveBackward)) {
        //     result = model.moveAgent(ag, 1, BACKWARD);
        // } else if (action.equals(moveLeft)) {
        //     result = model.moveAgent(ag, 1, LEFT);
        // } else if (action.equals(moveRandom)) {
        //     Direction rd = Direction.random();
        //     result = model.moveAgent(ag, 1, rd);
        // } else {
        //     RuntimeException e = new IllegalArgumentException("Cannot handle action: " + action);
        //     logger.warning(e.getMessage());
        //     throw e;
        // }
        // try {
        //     Thread.sleep(1000L / model.getFPS());
        // } catch (InterruptedException ignored) { }
        // notifyModelChangedToView();
        // return result;
        notifyModelChangedToView();
        return true; // Placeholder, replace with actual action logic
    }
}
