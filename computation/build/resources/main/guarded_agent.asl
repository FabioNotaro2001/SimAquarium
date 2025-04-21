/* TODO */


/* Initial beliefs and rules */

/* Initial goals */
!start(1, 10).

/* Plans */

+!start(N, M) : N < M <-
	!on_step(N).

+!on_step(N) : N mod 2 = 0 <-
    .print("hello world ", N, " even");
    !start(N + 1, M).

+!on_step(N) : N mod 2 = 1 <-
    .print("hello world ", N, " odd");
    !start(N + 1, M).

