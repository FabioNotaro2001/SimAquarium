/* Initial beliefs and rules */

/* Initial goals */
!start(0, 10).

/* Plans */
+!start(N, N) <-
    .print("hello world ", N).

+!start(N, M) : N < M <- 
    .print("hello world ", N);
    !start(N + 1, M).

