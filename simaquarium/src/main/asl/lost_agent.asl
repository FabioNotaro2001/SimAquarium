status(lost).

!go_around.

+!go_around : status(lost) <-
    move(random);
    !go_around.
-!go_around : status(lost) <-
    !go_around.
-!go_around : not(status(lost)) <- true.

+follow_me[source(A)] : status(lost) <-
    -+status(following(A)).