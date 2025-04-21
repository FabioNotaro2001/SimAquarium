/* Initial beliefs and rules */

/* Initial goals */
!start(1, 10).

/* Plans */

+!start(N, M) : N <= M <-
	!on_step(N);
	if (N < M) { !start(N + 1, M) }.
	
+!on_step(N) <-
	if (N mod 2 = 0) { E = "even" } else { E = "odd" };
	.print("hello world ", N, " ", E).