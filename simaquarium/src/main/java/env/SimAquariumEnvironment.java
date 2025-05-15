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
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import env.model.Amount;
import env.model.AquariumModel;
import env.model.AquariumModelImpl;
import env.model.DomainEvent;
import env.model.Fish;
import env.model.Pair;
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
    private boolean paused;
    private int foodQuantity;
    private int numberOfObstacles;

    // Action literals.
    public static final Literal moveTowards = Literal.parseLiteral("move_towards(X, Y, Speed)");
    public static final Literal eat = Literal.parseLiteral("eat(Food)");
    public static final Literal die = Literal.parseLiteral("die");
    public static final Literal init = Literal.parseLiteral("init(Weight, Energy, MaxEnergy)");

    private AquariumModel model;
    FishSimulationApp view;

    private Position getRandomPositionInsideAquarium(){
        return new Position(RAND.nextDouble() * this.model.getWidth(), RAND.nextDouble() * this.model.getHeight());
    }

    @Override
    public void init(final String[] args) {
        switch (Amount.valueOf(args[0])) {
            case LITTLE:
                this.foodQuantity = 3;
                break;
            case MANY:
                this.foodQuantity = 10;
                break;
            default:
                this.foodQuantity = 5;
                break;
        }
        
        switch (Amount.valueOf(args[1])) {
            case LITTLE:
                this.numberOfObstacles = 5;
                break;
            case MANY:
                this.numberOfObstacles = 20;
                break;
            default:
                this.numberOfObstacles = 10;
                break;
        }
        Locale.setDefault(Locale.UK);
        this.model = new AquariumModelImpl();
        this.model.setFoodQuantity(foodQuantity);
        this.view = new FishSimulationApp(this.model, this);
        this.view.setVisible(true);
        this.model.setAquariumDimensions(this.view.getPanelWidth(), this.view.getPanelHeight());
        this.paused = false;
        for (int i = 0; i < this.numberOfObstacles; i++){
            this.model.addObstacle(this.getRandomPositionInsideAquarium(), (RAND.nextDouble() * 0.1 + 0.02) * this.model.getHeight());
        }
        this.foodSimulationThread = new Thread(new Runnable(){
            @Override
            public void run() {
                long untilNextFoodDrop = 5000;
                long lastCycle = System.currentTimeMillis();
                while (true) { 
                    long time = System.currentTimeMillis();
                    if(!paused) {
                        untilNextFoodDrop -= time-lastCycle;
                        lastCycle = time;
                        if (untilNextFoodDrop <= 0) {
                            untilNextFoodDrop += 20000000;
                            for(int i = 0; i < foodQuantity; i++){
                                model.addFood(new Position((RAND.nextDouble() * 0.8 + 0.1) * model.getWidth(), 0));
                            }
                            notifyModelChangedToView(Optional.of(DomainEvent.of("Food dropped")));
                        } 
                        model.sinkStep();
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.foodSimulationThread.start();
    }

    private void notifyModelChangedToView(Optional<DomainEvent> event) {
        view.notifyModelChanged(event);
    }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        // Percept cibo in range, percept per il cibo abbastanza vicino, percept per gli ostacoli in range
        if(!this.model.containsAgent(agName)){
            return List.of();
        }
        return Stream.of(
                pausePercept(),
                foodInRangePercept(agName),
                closestFoodPercept(agName),
                obstaclePercept(agName),
                borderPercept(agName)
        ).flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private Collection<Literal> borderPercept(String agent) {
        Fish fish = model.getAgent(agent);
        var borders = model.getNearbyBorders(agent)
            .stream()
            .map(b -> {
                double distance;
                switch (b) {
                    case TOP:
                        distance = fish.getY();
                        break;
                    case BOTTOM:
                        distance = model.getHeight() - fish.getY();
                        break;
                    case LEFT:
                        distance = fish.getX();
                        break;
                    default:
                        distance = model.getWidth() - fish.getX();
                        break;
                }
                return Literal.parseLiteral(String.format("border(%s,%f)", b.toString().toLowerCase(), distance));
            })
            .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        return Stream.of(Literal.parseLiteral(String.format("borders(%s)", borders))).collect(Collectors.toList());
    }

    private Collection<Literal> foodInRangePercept(String agent) {
        Fish fish = this.model.getAgent(agent);

        var coordinates = model.getNearbyFood(agent)
            .stream()
            .map(f -> Literal.parseLiteral(String.format("food_elem(%f,%f,%s)", f.getPosition().getX() - fish.getX(), f.getPosition().getY() - fish.getY(), f.getId())))
            .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        return Stream.of(Literal.parseLiteral(String.format("food(%s)", coordinates))).collect(Collectors.toList());
    }

    private Collection<Literal> pausePercept() {
        Literal paused = Literal.parseLiteral("paused");
        return this.paused ? List.of(paused) : List.of();
    }

    private Collection<Literal> closestFoodPercept(String agent) {
        var foodOpt = this.model.getClosestFoodThatCanBeEaten(agent);
        model.addEventToList(new Pair<String,String>(agent, "food_percept"));
        return foodOpt.isPresent() ? List.of(Literal.parseLiteral(String.format("close_to_food(%s)", foodOpt.get().getId()))) : List.of();
    }

    private Collection<Literal> obstaclePercept(String agent) {
        Fish fish = this.model.getAgent(agent);

        var obstacleStream = this.model.getNearbyObstacles(agent)
            .stream()
            .sorted((o1, o2) -> Double.compare(Vector2D.of(o1.getX() - fish.getX(), o1.getY() - fish.getY()).getLength(), Vector2D.of(o2.getX() - fish.getX(), o2.getY() - fish.getY()).getLength()))
            .map(o -> Literal.parseLiteral(String.format("obstacle(%f,%f,%f)", o.getX() - fish.getX(), o.getY() - fish.getY(), o.getRadius())));

        var fishStream = this.model.getNearbyFish(agent)
            .stream()
            .sorted((f1, f2) -> Double.compare(Vector2D.of(f1.getX() - fish.getX(), f1.getY() - fish.getY()).getLength(), Vector2D.of(f2.getX() - fish.getX(), f2.getY() - fish.getY()).getLength()))
            .map(f -> Literal.parseLiteral(String.format("obstacle(%f,%f,%f)", f.getX() - fish.getX(), f.getY() - fish.getY(), f.getSize() / 2)));

        var coords = Stream.concat(obstacleStream, fishStream).collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        
        return Stream.of(Literal.parseLiteral(String.format("obstacles(%s)", coords))).collect(Collectors.toList());
    }

    /**
     * The <code>boolean</code> returned represents the action "move"
     * (success/failure)
     */
    @Override
    public boolean executeAction(final String ag, final Structure action) {
        Unifier un = new Unifier();
        if(un.unifies(init, action)){
            if (model.containsAgent(ag)) {
                return false;
            }
            try {
                double weight = termToDouble(un.get("Weight"));
                double energy = termToDouble(un.get("Energy"));
                double maxEnergy = termToDouble(un.get("MaxEnergy"));
                this.model.addFish(ag,  weight, energy, maxEnergy, this.getRandomPositionInsideAquarium());
                notifyModelChangedToView(Optional.empty());
                return true;
            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }
        }
        else if(un.unifies(moveTowards, action)){
            double x;
            double y;
            Speed s;
            try {
                x = termToDouble(un.get("X"));
                y = termToDouble(un.get("Y"));
                s = termToSpeed(un.get("Speed"));
                this.model.moveTowards(ag, x, y, s);
                notifyModelChangedToView(Optional.empty());
                return true;

            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }
        } else if(un.unifies(eat, action)){
            String foodId;
            try {
                foodId = termToString(un.get("Food"));
                boolean eaten = this.model.eat(ag, foodId);
                if(eaten){
                    notifyModelChangedToView(Optional.of(DomainEvent.of(String.format("%s has eaten (%d pcs)", ag, this.model.getAgent(ag).getNumberOfFoodEaten()))));
                }
                return eaten;
            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }
        } else if(un.unifies(die, action)){
            this.model.removeAgent(ag);
            notifyModelChangedToView(Optional.of(DomainEvent.of(String.format("%s is dead", ag))));
            return true;
        }
        notifyModelChangedToView(Optional.empty());
        return false; // Placeholder, replace with actual action logic
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
