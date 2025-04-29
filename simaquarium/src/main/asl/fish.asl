// Beliefs.
energy(1000).
speed(normal).
steps(1).
direction(1, 0).

// Goals.

// TODO: l'aggiramento degli ostacoli deve tenere in considerazione i bordi, il fatto che il cibo cade e più ostacoli ravvicinati.
// TODO: scomporre eat per fare come ci ha suggerito il prof.
!init.

+!init <- 
    utils.agent_init;
    !step.

// Percepts.
+food(Coordinates) <- utils.find_nearest(Coordinates).    // Eventualmente aggiunge la belief target_food(X, Y)

+close_to_food(F) <- eat(Food).

+obstacles(Coordinates) <- .print("OBSTACLE!!! ", Coordinates).

+borders(L) <- .print("BOOOOORDER!!! ", L).

+eaten : energy(E) <- 
    -has_target;
    utils.find_nearest(coordinates);
    -+energy(.max(100, E + 10)).

-has_target <- 
    utils.set_random_dir;
    -+steps(30).

// Plans.
+!step : energy(E) & steps(S) <-
    if(E <= 0){
        .fail;
    }
    utils.check_aquarium_borders;
    if(obstacles(O)){
        utils.avoid_obstacles(O);
    }
    if(not(has_target)){
        if(E >= 75){
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
        if(E >= 75){
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

-!step <- .print("I'm dead!").