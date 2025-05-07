// Beliefs.
// TODO: incapsulare il peso del pesce come diceva ikl prof Omicini
// TODO: mettere nella GUI, nelle statistiche, anche un fairness index da noi scelto (ad esempio tra 0 e 1 calcolato ad esempio come media dei cibi mangiati da ogni pesce / (cibi totali mangiati / numero pesci))
// TODO: cambiare direzione come suggerito da Bertu
// TODO: sostituire energy con status(normal/hungry/dead) e gestire l'energia solo lato model. Ma prima chiedere al prof.
energy(500).
speed(normal).
steps(1).
direction(1, 0).

// Goals.

!init.

+!init <- 
    utils.agent_init;
    !step.

// Percepts.
+food(Coordinates) <- 
    utils.find_nearest(Coordinates).    // Eventualmente aggiunge la belief target_food(X, Y)

+close_to_food(F) : energy(E) & E > 0 <-
    !eat(F).

-has_target(_, _) <- 
    utils.set_random_dir;
    -+steps(30).

// Plans.
+!step : energy(E) & E <= 0 <-
    .drop_all_intentions;
    .drop_all_desires;
    die;
    utils.stop_agent.
    
+!step : energy(E) & steps(S) <-
    if (digesting) {
        .wait(1000);
        -digesting;
    }
    if(has_target(X, Y)){
        -+direction(X, Y);
    }
    utils.check_aquarium_borders;
    if(obstacles(O)){
        utils.avoid_obstacles(O);
    }
    if(not(has_target(_, _))){
        if(E >= 200){
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
        if(E >= 200){
            !move(fast);
        } else {
            !move(faster);
        }
    }
    !step. 

+!move(Speed) : direction(X, Y) <-
    -+speed(Speed);
    utils.move_towards(X, Y, Speed);
    move_towards(X, Y, Speed).

+!eat(F) : energy(E) <- 
    .wait(not(digesting));
    eat(F);
    -+energy(E + 30);
    -close_to_food(F);
    +digesting.

-!eat(F) <- .print("I was not able to eat ", F).