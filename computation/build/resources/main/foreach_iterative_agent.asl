/* Initial beliefs and rules */
nat(z).
nat(s(X)) :- nat(X).

// nat(1).
// nat(2).
// nat(3).

/* Initial goals */
!start.

/* Plans */
+!start <-
    // this only works in jason 2.5+
    for(nat(X)) {
        .print("value ", X);
        .wait(1000);
    }.
