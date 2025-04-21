/* Initial beliefs and rules */

/* Initial goals */
!count(1, 10).

/* Plans */

+!count(N, N) <-
    .print(count, " ", N, ", end").

+!count(N, M) : N < M <-
    .print("count ", N);
    .wait(1000);
    if (N = 7) { !!count(1, M) };
    !count(N + 1, M).