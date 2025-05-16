package launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import env.SimAquariumEnvironment;
import jason.JasonException;
import jason.infra.local.RunLocalMAS;

/**
 * The `SimulationLauncher` class is responsible for launching and managing the simulation.
 * It extends the `RunLocalMAS` class to provide additional functionality for initializing
 * and configuring the Multi-Agent System (MAS) environment.
 */
public class SimulationLauncher extends RunLocalMAS{
    static final String NEW_FILE_NAME = Path.of(".", "tmp", "sim.mas2j").toString();
    static final String MAS_CONTENT = "MAS robots {\r\n" + //
                "\tinfrastructure: Centralised \r\n" + //
                "\tenvironment: env.SimAquariumEnvironment(%s, %s) \r\n" + //
                "\tagents: fish #%d;\r\n" + //
                "\taslSourcePath: \"src/main/asl\";\r\n" + //
                "}";
    static SimulationLauncher mas;

    /**
     * Default constructor for the `SimulationLauncher` class.
     * Calls the superclass constructor to initialize the base functionality.
     */
    public SimulationLauncher(){
        super();
    }

    /**
     * Retrieves the simulation environment.
     * 
     * @return The `SimAquariumEnvironment` instance associated with the simulation.
     */
    public SimAquariumEnvironment getEnvironment(){
        return (SimAquariumEnvironment)(this.getEnvironmentInfraTier().getUserEnvironment());
    }

    /**
     * Retrieves the current instance of the `SimulationLauncher`.
     * 
     * @return The `RunLocalMAS` instance representing the current simulation launcher.
     */
    public static RunLocalMAS getLocalMAS(){
        return mas;
    }

    /**
     * Launches a new simulation with the specified parameters.
     * 
     * @param numberOfAgents The number of agents (fish) to include in the simulation.
     * @param foodQuantity The quantity of food in the simulation (e.g., "LITTLE", "NORMAL", "MANY").
     * @param numberOfObstacles The number of obstacles in the simulation.
     * @return A new instance of the `SimulationLauncher` representing the launched simulation.
     * @throws IOException If an error occurs while creating or writing the MAS configuration file.
     * @throws JasonException If an error occurs while initializing the MAS environment.
     */
    public static SimulationLauncher launchNew(int numberOfAgents, String foodQuantity, String numberOfObstacles) throws IOException, JasonException{
        String result = String.format(MAS_CONTENT, foodQuantity, numberOfObstacles, numberOfAgents);
        File f = new File(NEW_FILE_NAME);
        f.getParentFile().mkdirs();
        f.createNewFile();

        try (FileWriter writer = new FileWriter(f)) {
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        mas = new SimulationLauncher();
        mas.init(new String[]{NEW_FILE_NAME});
        mas.registerMBean();
        mas.registerInRMI();
        mas.registerWebMindInspector();
        mas.create();
        mas.start();
        return mas;
    }
}
