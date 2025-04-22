// Beliefs.
energy(100).
position(0,0).
speed(normal).
// Nel model probabilmente ci andranno weigth e range.

// Percepts.
+food(coordinates) <- utils.find_nearest(coordinates).    // Eventualmente aggiunge la belief target_food(X, Y)

// Goals.
+!target_food(X, Y) : energy(A) & A < 75 <- 
    -+speed(fast);
    move_towards(X, Y, fast);
    utils.move_towards(X, Y, fast).
