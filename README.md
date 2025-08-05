# SimAquarium 🐟

SimAquarium is a multi-agent system that simulates an intelligent aquarium, developed using **Java**, **JADE** and **JASON**. The system leverages **BDI agent architecture** to model autonomous fish agents with their own beliefs, desires, and intentions.

This project was developed as part of an Intelligent Systems Engineering course.

---

## 🧠 Overview

Each fish in the aquarium is modeled as an intelligent agent capable of:

- Swimming independently in the environment
- Seeking food when hungry
- Avoiding obstacles
- Interacting with other agents
- Reacting to environmental changes

The environment is managed by an **Environment Agent**, which keeps track of all agents and updates the simulation state. Agents perceive the environment and act according to BDI reasoning.

---

## 🗃️ Technologies Used

- **Java**
- **JADE** – Java Agent DEvelopment Framework
- **JASON** – Agent-oriented programming language for BDI-based systems
- **Maven** – Build and dependency management

---

## 📁 Project Structure


SimAquarium/
├── src/
│ ├── env/ # Environment logic (Java)
│ ├── agents/ # Java agents integrating with JASON
│ └── jason/ # JASON agent code (.asl)
├── lib/ # External libraries (JADE, JASON)
├── beliefs/ # Belief base files for fish agents
├── desires/ # Goals for the agents
├── plans/ # Plans (.txt) used by agents
├── pom.xml # Maven build configuration
└── README.md

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/FabioNotaro2001/SimAquarium.git
cd SimAquarium

2. Requirements

    Java 8 or higher

    Maven

    JADE and JASON (included in lib/, or you can install via Maven if preferred)

3. Compile the project

mvn clean package

4. Run the simulation
mvn exec:java -Dexec.mainClass="env.MainEnvironment"

This will launch the environment and start the simulation with multiple fish agents.
🧬 Agent Logic

Each fish agent is defined via:

    Beliefs (.asl): internal state (e.g., hunger, location)

    Desires: goals like finding food, avoiding others

    Plans (.txt): actions the agent can take based on context

Agents are designed with autonomy, proactivity, and reactivity, consistent with the BDI paradigm.
🧪 Example Scenario

In a typical run:

    The MainEnvironment initializes the aquarium and registers all agents.

    Fish agents begin swimming randomly.

    When a fish becomes hungry, it searches for food.

    If food is perceived, it moves toward it.

    Agents avoid collisions and respond to other dynamic events.

📚 Notes

    All behavior rules are defined in the plans/ folder.

    You can modify agent behavior by editing .asl belief files and plan files.

    The simulation is console-based but can be extended with a GUI.
👨‍🎓 Author

Fabio Notaro
Developed as a final project for the Intelligent Systems Engineering course, 2025.
📄 License

This project is licensed for educational and non-commercial use. Feel free to fork and experiment!
