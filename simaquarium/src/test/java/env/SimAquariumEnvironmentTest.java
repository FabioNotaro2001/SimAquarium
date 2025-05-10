package env;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class SimAquariumEnvironmentTest {

    private SimAquariumEnvironment environment;

    @Before
    public void setUp() {
        environment = new SimAquariumEnvironment();
        environment.init(new String[]{"NORMAL", "NORMAL"});
    }

    @Test
    public void testExecuteAction() {
        Literal initAction = Literal.parseLiteral("init(50, 100, 200)");
        boolean result = environment.executeAction("Fish1", (Structure) initAction);
        assertTrue("Expected the environment to handle 'init' action successfully", result);
        Literal eatAction = Literal.parseLiteral("eat(food1)");
        result = environment.executeAction("Fish1", (Structure) eatAction);
        assertFalse("Expected 'eat(food1)' to fail when no food is present", result);
    }

    @Test
    public void testGetPercepts() {
        Structure initAction = (Structure) Literal.parseLiteral("init(50, 100, 200)");
        environment.executeAction("Fish1", initAction);
        Collection<Literal> percepts = environment.getPercepts("Fish1");
        assertNotNull("Percepts should not be null", percepts);
        assertFalse("Expected some percepts to be present", percepts.isEmpty());
    }

    @Test
    public void testInit() {
        assertNotNull("Environment should be initialized", environment);
        Collection<Literal> defaultPercepts = environment.getPercepts("Fish1");
        assertNotNull("Expected getPercepts() to not return null", defaultPercepts);
    }
}