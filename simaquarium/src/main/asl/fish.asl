// Beliefs.
// The agent's initial beliefs:
// - `speed(normal)`: The agent starts with a normal speed.
// - `steps(1)`: The agent has 1 step remaining before changing direction.
// - `direction(1, 0)`: The agent's initial direction is along the x-axis.
// - `-has_target(_, _) <- -+steps(30)`: If the agent does not have a target, it resets the steps to 30.
speed(normal).
steps(1).
direction(1, 0).
-has_target(_, _) <- 
    -+steps(30).

// Goals.
// The initial goal of the agent, which sets up its state and starts the main behavior loop.
!init.

// Percepts.
//   When the agent perceives food (`FS`), it uses the utility function `find_nearest` to locate the nearest food.
+food(FS) <- 
    utils.find_nearest(FS).

//   When the agent perceives it is close to food (`F`) and has energy greater than 0, it triggers the goal to eat the food (`!eat(F)`).
+close_to_food(F) : energy(E, _) & E > 0 <-
    !eat(F).

// Plans.
//   The initialization plan:
//   - Calls `utils.agent_init` to initialize the agent.
//   - Retrieves the agent's weight and energy using `.belief`.
//   - Calls `init(W, E, ME)` to set up the agent's state.
//   - Starts the main behavior loop with `!step`.
+!init <- 
    utils.agent_init;
    .belief(weight(W));
    .belief(energy(E, ME));
    init(W, E, ME);
    !step.

//   If the agent's energy is 0 or less:
//   - Waits until the simulation is not paused.
//   - Drops all intentions and desires.
//   - Executes the `die` action and stops the agent using `utils.stop_agent`.
+!step : energy(E, _) & E <= 0 <-
    .wait(not(paused));
    .drop_all_intentions;
    .drop_all_desires;
    die;
    utils.stop_agent.

//   The main behavior loop when the agent has energy:
//   - If the agent is digesting, it waits for 1000ms and stops digesting.
//   - Waits until the simulation is not paused.
//   - If the agent has a target, it rotates its direction using `utils.rotate_dir`.
//   - Checks if the agent is near the aquarium borders using `utils.check_aquarium_borders`.
//   - If there are obstacles, it avoids them using `utils.avoid_obstacles(O)`.
//   - If the agent does not have a target:
//     - Moves at normal or slow speed based on its energy level.
//     - If steps reach 0, it sets a random direction and resets steps to 30; otherwise, it decrements the steps.
//   - If the agent has a target:
//     - Moves at fast or faster speed based on its energy level.
//   - Recursively calls `!step` to continue the behavior loop.
+!step : energy(E, ME) & steps(S) <-
    if (digesting) {
        .wait(1000);
        -digesting;
    }
    .wait(not(paused));
    if(has_target(_, _)){
        utils.rotate_dir;
    }
    utils.check_aquarium_borders;
    if(obstacles(O)){
        utils.avoid_obstacles(O);
    }
    if(not(has_target(_, _))){
        if(E >= ME / 2){
            !move(normal);
        } else {
            !move(slow);
        }
        if (S - 1 = 0) {
            utils.set_random_dir;
            -+steps(30);
        } else {
            -+steps(S - 1);
        }
    } else {
        if(E >= ME / 2){
            !move(fast);
        } else {
            !move(faster);
        }
    }
    !step. 

//   Moves the agent in the specified direction (`X`, `Y`) at the given speed (`Speed`):
//   - Updates the agent's speed belief.
//   - Calls `utils.move_towards(Speed)` to move the agent.
//   - Executes the `move_towards` action with the specified parameters.
+!move(Speed) : direction(X, Y) <-
    -+speed(Speed);
    utils.move_towards(Speed);
    move_towards(X, Y, Speed).

//   The plan to eat food (`F`):
//   - Waits until the agent is not digesting.
//   - Executes the `eat(F)` action.
//   - Updates the agent's energy by adding the food's energy (`FE`).
//   - Removes the belief that the agent is close to the food.
//   - Sets the agent to a digesting state.
+!eat(F) : energy(E, ME) & food_energy(FE) <- 
    .wait(not(digesting));
    eat(F);
    -+energy(E + FE, ME);
    -close_to_food(F);
    +digesting.

//   If the agent fails to eat the food (`F`), it prints a message indicating the failure.
-!eat(F) <- .print("I was not able to eat ", F).