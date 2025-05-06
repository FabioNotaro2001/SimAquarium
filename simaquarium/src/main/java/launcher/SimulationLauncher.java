package launcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import env.SimAquariumEnvironment;
import jason.JasonException;
import jason.infra.local.RunLocalMAS;

public class SimulationLauncher extends RunLocalMAS{
    static final String NEW_FILE_NAME = Path.of(".", "tmp", "sim.mas2j").toString();
    static final String MAS_CONTENT = "MAS robots {\r\n" + //
                "\tinfrastructure: Centralised \r\n" + //
                "\tenvironment: env.SimAquariumEnvironment(%s, %s) \r\n" + //
                "\tagents: fish #%d;\r\n" + //
                "\taslSourcePath: \"src/main/asl\";\r\n" + //
                "}";
    static SimulationLauncher mas;
    

    public SimulationLauncher(){
        super();
    }

    public SimAquariumEnvironment getEnvironment(){
        return (SimAquariumEnvironment)(this.getEnvironmentInfraTier().getUserEnvironment());
    }

    public static RunLocalMAS getLocalMAS(){
        return mas;
    }

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
