// Beliefs.
energy(100).
speed(normal).
steps(0).
// Nel model probabilmente ci andranno weigth e range.

// TODO: l'aggiramento degli ostacoli deve tenere in considerazione i bordi, il fatto che il cibo cade e più ostacoli ravvicinati.

!init.

+!init <- 
    utils.agent_init;
    !move.


// TODO: add delay in move_towards

// Percepts.
+food(coordinates) <- utils.find_nearest(coordinates).    // Eventualmente aggiunge la belief target_food(X, Y)

+close_to_food(F) <- eat(Food).

+obstacles(coordinates) <- utils.avoid_obstacles(coordinates).

+eaten : energy(E) <- 
    -has_target;
    utils.find_nearest(coordinates);
    -+energy(.max(100, E + 10)).

-food(_) <- 
    utils.set_random_dir();
    -+steps(10).

+!move : not(has_target) & (energy(E) & E < 75) & steps(S) & direction(X, Y) <-
    -+speed(slow);
    utils.move_towards(X, Y, slow);
    move_towards(X, Y, slow);
    if (S - 1 = 0) {
        utils.set_random_dir();
        -+steps(10);
    } else {
        -+steps(S - 1).
    }
    !move.

+!move : not(has_target) & steps(S) & direction(X, Y) <-
    -+speed(normal);
    utils.move_towards(X, Y, normal);
    move_towards(X, Y, normal);
    if (S - 1 = 0) {
        utils.set_random_dir();
        -+steps(10);
    } else {
        -+steps(S - 1);
    }
    !move.

+!move : has_target & (energy(E) & E < 75) & direction(X, Y) <-
    -+speed(faster);
    utils.move_towards(X, Y, faster);
    move_towards(X, Y, faster);
    !move.

+!move : has_target & direction(X, Y) <-
    -+speed(fast);
    utils.move_towards(X, Y, fast);
    move_towards(X, Y, fast);
    !move.
