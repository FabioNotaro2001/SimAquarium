// Beliefs.
// TODO: ricordati i test.
// TODO: ricordati di togliere il codice dei robot quando non serviranno più.
speed(normal).
steps(1).
direction(1, 0).

// Goals.
!init.

+!init <- 
    utils.agent_init;
    .belief(weight(W));
    .belief(energy(E, ME));
    init(W, E, ME);
    !step.

// Percepts.
+food(Coordinates) <- 
    utils.find_nearest(Coordinates).    // Eventualmente aggiunge la belief target_food(X, Y)

+close_to_food(F) : energy(E, _) & E > 0 <-
    !eat(F).

-has_target(_, _) <- 
    -+steps(30).

// Plans.
+!step : energy(E, _) & E <= 0 <-
    .wait(not(paused));
    .drop_all_intentions;
    .drop_all_desires;
    die;
    utils.stop_agent.
    
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

+!move(Speed) : direction(X, Y) <-
    -+speed(Speed);
    utils.move_towards(Speed);
    move_towards(X, Y, Speed).

+!eat(F) : energy(E, ME) & food_energy(FE) <- 
    .wait(not(digesting));
    eat(F);
    -+energy(E + FE, ME);
    -close_to_food(F);
    +digesting.

-!eat(F) <- .print("I was not able to eat ", F).