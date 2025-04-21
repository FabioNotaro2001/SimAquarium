/* Initial beliefs and rules */

/* Initial goals */

!start(1, 10).

/* Plans */

+!start(N, N) : N mod 2 = 0 <- 
    .print("hello world ", N, " even").

+!start(N, N) : N mod 2 = 1 <- 
    .print("hello world ", N, " odd").

+!start(N, M) : N < M & N mod 2 = 0 <- 
    .print("hello world ", N, " even");
    !start(N+1, M).

+!start(N, M) : N < M & N mod 2 = 1 <- 
    .print("hello world ", N, " odd");
    !start(N+1, M).