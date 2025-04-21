/* Initial beliefs and rules */

/* Initial goals */

!count(0, 10, up).
!count(100, 90, down).

/* Plans */

+!count(N, N, Dir) <-
    .print(Dir, " ", N, ", end").

+!count(N, M, up) : N < M <-
    .print("up ", N);
    .wait(1000);
    !count(N + 1, M, up).

+!count(N, M, down) : N > M <-
    .print("down ", N);
    .wait(1000);
    !count(N - 1, M, down).