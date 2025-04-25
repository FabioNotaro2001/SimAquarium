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
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import env.model.AquariumModel;
import env.model.AquariumModelImpl;
import env.model.Speed;
import env.view.FishSimulationApp;


/**
 * Any Jason environment "entry point" should extend
 * jason.environment.Environment class to override methods init(),
 * updatePercepts() and executeAction().
 */
public class SimAquariumEnvironment extends Environment {

    private static final Random RAND = new Random();

    // Action literals.
    public static final Literal moveTowards = Literal.parseLiteral("move_towards(X, Y, Speed)");
    public static final Literal eat = Literal.parseLiteral("eat(Food)");

    private AquariumModel model;
    FishSimulationApp view;

    @Override
    public void init(final String[] args) {
        this.view = new FishSimulationApp(this.model);
        view.setVisible(true);
        this.model = new AquariumModelImpl(this.view.getWidth(), this.view.getHeight());
    }

    private void notifyModelChangedToView() {
        view.notifyModelChanged();
    }

    // private void initializeAgentIfNeeded(String agentName) {
    //     if (!model.containsAgent(agentName)) {
    //         model.setAgentPoseRandomly(agentName);
    //         view.notifyModelChanged();
    //     }
    // }

    @Override
    public Collection<Literal> getPercepts(String agName) {
        // Percept cibo in range, percept per il cibo abbastanza vicino, percept per gli ostacoli in range
        return Stream.of(
                foodInRangePercepts(agName),
                closestFoodPercept(agName)
        ).flatMap(Collection::stream)
        .collect(Collectors.toList());
    }

    private Collection<Literal> foodInRangePercepts(String agent) {
        var coordinates = model.getNearbyFood(agent)
                .stream()
                .map(f -> Literal.parseLiteral(String.format("food_elem(%f,%f,%s)", f.getPosition().getX(), f.getPosition().getY(), f.getId())))
                .collect(ListTermImpl::new, ListTerm::add, ListTerm::addAll);
        return Stream.of(Literal.parseLiteral("food(" + coordinates + ")")).collect(Collectors.toList());
    }

    private Collection<Literal> closestFoodPercept(String agent) {
        return null;
        // return model.getAgentNeighbours(agent).stream()
        //         .map(it -> String.format("neighbour(%s)", it))
        //         .map(Literal::parseLiteral)
        //         .collect(Collectors.toList());
    }

    private Collection<Literal> obstaclePercepts(String agent) {
        // return model.getAgentNeighbours(agent).stream()
        //         .map(it -> String.format("neighbour(%s)", it))
        //         .map(Literal::parseLiteral)
        //         .collect(Collectors.toList());
        return null;
    }

    /**
     * The <code>boolean</code> returned represents the action "move"
     * (success/failure)
     */
    @Override
    public boolean executeAction(final String ag, final Structure action) {
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
                return true;

            } catch (NoValueException e) {
                e.printStackTrace();
                return false;
            }

        } else if(un.unifies(eat, action)){
            String foodId;
            try {
                foodId = termToString(un.get("Food"));
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
        return true; // Placeholder, replace with actual action logic
    }
}
