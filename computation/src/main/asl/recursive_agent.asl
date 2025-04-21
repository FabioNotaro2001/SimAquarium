/* Initial beliefs and rules */

/* Initial goals */

!start(0).

/* Plans */

+!start(N) <- 
    .print("hello world ", N);
    .wait(1000);
    !start(N + 1).