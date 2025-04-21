/* Initial beliefs and rules */
max_failures(3).
failures(0).

/* Initial goals */
!gamble.

/* Plans */
+!gamble <-
    .println("Head or Cross?");
    .random(Coin);
    if (Coin >= 0.5) {
        .print("Head! I won :D");
        !gamble
    } else {
        .print("Cross! I lost :C");
        .fail
    }.

/*
-!gamble : failures(N) & max_failures(M) <-
    -+failures(N + 1);
    if (N < M) {
        .print("Let's retry one more time U.U");
        !gamble
    } else {
        .print("I'm done with this shit.");
    }.
*/